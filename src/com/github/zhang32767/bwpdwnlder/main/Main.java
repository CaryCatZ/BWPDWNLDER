package com.github.zhang32767.bwpdwnlder.main;

import com.github.zhang32767.bwpdwnlder.io.downloader.MultiThreadDownloader;
import com.github.zhang32767.bwpdwnlder.io.downloader.services.MultiThreadDownloaderService;
import com.github.zhang32767.bwpdwnlder.pictures.Picture;
import com.github.zhang32767.bwpdwnlder.pictures.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(12, 24, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64));
    private static final MultiThreadDownloaderService downloaderService = new MultiThreadDownloaderService(executor, new MultiThreadDownloader(executor));
    public static final Logger LOGGER = LogManager.getLogger("bwpdwnlder");

    public static void main(String[] s) {
        Args args = new Args();

        try {
            CmdLineParser parser = new CmdLineParser(args);
            parser.parseArgument(s);
        } catch (CmdLineException e) {
            System.out.println("Wrong command!");
            System.out.println("Use --help or -h to get use help.");
            System.exit(1);
        }

        LinkedHashMap<Integer, LinkedList<Integer>> pages = new LinkedHashMap<>();
        args.indexes.sort(Comparator.comparingInt(o -> o));

        iteratePages:
        for (int p = 1; true; ++p) {
            for (int i : new LinkedList<>(args.indexes)) {
                if (i <= 12 * p) {
                    pages.putIfAbsent(p, new LinkedList<>());
                    pages.get(p).add(i - 12 * p + 11);
                    args.indexes.remove((Integer) i);
                } else {
                    continue iteratePages;
                }
            }
            if (args.indexes.isEmpty()) {
                break;
            }
        }

        for (Map.Entry<Integer, LinkedList<Integer>> entry : pages.entrySet()) {
            executor.execute(() -> {
                try {
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
                        downloaderService.download(picture);
                    });
                } catch (Exception e) {
                    LOGGER.error("Fail to get the index", e);
                }
            });
        }
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    private static class Args {
        @Argument(required = true)
        private List<Integer> indexes = null;

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
        private String format = "{date}_{name}.jpg";
    }
}
