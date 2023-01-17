package io.github.carycatz.bwpdwnlder.core;

import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.io.downloader.services.MultiThreadDownloaderService;
import io.github.carycatz.bwpdwnlder.pictures.Picture;
import io.github.carycatz.bwpdwnlder.pictures.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    public static final Logger LOGGER = LogManager.getLogger("bwpdwnlder");

    public static void main(String[] s) {
        Args args = new Args();
        ThreadPoolExecutor executor;
        MultiThreadDownloaderService downloaderService;
        AtomicInteger finished = new AtomicInteger();
        int nPictures = 0;

        try {
            CmdLineParser parser = new CmdLineParser(args);
            parser.parseArgument(s);
        } catch (CmdLineException e) {
            System.out.println("Wrong args!");
            System.out.println("Use --help or -h to get help.");
            System.exit(1);
        }

        if (args.threadCount > 8) {
            LOGGER.warn("Thread count may be too large: {}", args.threadCount);
        }

        executor = new ThreadPoolExecutor(args.threadCount * 6, args.threadCount * 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        downloaderService = new MultiThreadDownloaderService(executor, new MultiThreadDownloader(executor, args.threadCount));

        /* DEBUG BLOCK */
        LOGGER.debug("================== DEBUG ==================");
        LOGGER.debug("Arguments: {}", args);
        LOGGER.debug("Executor: {}", executor);
        LOGGER.debug("DownloaderService: {}", downloaderService);
        LOGGER.debug("================== DEBUG ==================");
        /* DEBUG BLOCK */


        LinkedHashMap<Integer, LinkedList<Integer>> pages = new LinkedHashMap<>();
        args.indexes.sort(Comparator.comparingInt(o -> o));

        iteratePages:
        for (int p = 1; true; ++p) {
            for (int i : new LinkedList<>(args.indexes)) {
                if (i <= 12 * p) {
                    pages.putIfAbsent(p, new LinkedList<>());
                    pages.get(p).add(i - 12 * p + 11);
                    args.indexes.remove((Integer) i);
                    nPictures += 1;
                } else {
                    continue iteratePages;
                }
            }
            if (args.indexes.isEmpty()) {
                args.indexes = null;
                break;
            }
        }


        for (Map.Entry<Integer, LinkedList<Integer>> entry : pages.entrySet()) {
            executor.execute(() -> {
                try {
                    LOGGER.info("Looking in https://bing.ioliu.cn/?p=%s".formatted(entry.getKey()));
                    Document doc = Jsoup.connect("https://bing.ioliu.cn/?p=%s".formatted(entry.getKey())).get();
                    LinkedList<Picture.PictureInfo> info = Parser.parse(doc, entry.getValue());
                    info.forEach(i -> {
                        String name = args.format
                                .replace("{description}", i.description())
                                .replace("{url}", i.url())
                                .replace("{date}", i.date())
                                .replace("{name}", i.date() + "_" + i.url().split("=")[1].split("&")[0])
                                .replace("{index}", String.valueOf(entry.getKey()*12+i.num()-11))
                                .replace("{num}", String.valueOf(i.num()))
                                .replace("{page}", String.valueOf(entry.getKey()));
                        Picture picture = new Picture(i, args.path, name);
                        downloaderService.download(picture, finished::getAndIncrement); // report when finished downloading
                    });

                    info.clear();
                    entry.getValue().clear();
                } catch (Exception e) {
                    LOGGER.error("Exception in getting index", e);
                }
            });
        }
        pages.clear();

        // wait until all finished
        while (finished.get() < nPictures) {
            Thread.onSpinWait();
        }

        // shutdown and clean
        executor.shutdown();
        downloaderService.shutdown();
    }

    private static class Args {
        @Argument()
        private List<Integer> indexes = new ArrayList<>(List.of(1));

        @Option(name = "--path")
        private String path = System.getProperty("user.dir");

        /**
         * <p>{description} - The description of the picture.</p>
         * <p>{url} - The url of the picture.</p>
         * <p>{date} - The date of the picture.</p>
         * <p>{name} - The name of the picture</p>
         * <p>{index} - The index of the picture.</p>
         * <p>{num} - The number of the picture in a page.</p>
         * <p>{page} - The page with picture.</p>
         **/
        @Option(name = "--format")
        private String format = "{date}_{name}";

        @Option(name = "--threadCount")
        private int threadCount = 6;

        @Override
        public String toString() {
            return super.toString() + "[" +
                    "indexes = " + indexes + ", " +
                    "path = " + path + ", " +
                    "filename format = " + format + ", " +
                    "thread count = " + threadCount + "]";
        }
    }
}
