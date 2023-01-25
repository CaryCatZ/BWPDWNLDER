package io.github.carycatz.bwpdwnlder.main;

import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.io.downloader.services.MultiThreadDownloaderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    static ThreadPoolExecutor executor;
    static MultiThreadDownloaderService downloaderService;
    static Args args;
    public static final Logger LOGGER = LogManager.getLogger("bwpdwnlder");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] s) {
        prepare(s);

        /* DEBUG BLOCK */
        LOGGER.debug("================== DEBUG ==================");
        LOGGER.debug("Arguments: {}", args);
        LOGGER.debug("Executor: {}", executor);
        LOGGER.debug("DownloaderService: {}", downloaderService);
        LOGGER.debug("================== DEBUG ==================");
        /* DEBUG BLOCK */

        Application.run(args);
        cleanup();
    }

    private static void prepare(String[] s) {
        args = new Args();

        try {
            CmdLineParser parser = new CmdLineParser(args);
            parser.parseArgument(s);
        } catch (CmdLineException e) {
            System.out.println("Wrong command!");
            System.out.println("Use --help or -h to get use help.");
            System.exit(1);
        }

        if (args.threadCount > 8) LOGGER.warn("Thread count may be too large: {}", args.threadCount);
        executor = new ThreadPoolExecutor(args.threadCount*4, args.threadCount*8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        downloaderService = new MultiThreadDownloaderService(executor, new MultiThreadDownloader(executor, args.threadCount));
    }

    private static void cleanup() {
        executor.shutdown();
        downloaderService.shutdown();
        args = null;
        downloaderService = null;
        executor = null;
    }

    static class Args {
        @Argument()
        public List<Integer> indexes = new ArrayList<>();

        @Option(name = "--path")
        public String path = System.getProperty("user.dir");

        /**
         * <p>{url} - The url of the picture.</p>
         * <p>{description} - The description of the picture.</p>
         * <p>{name} - The name of the picture</p>
         * <p>{date} - The date of the picture.</p>
         * <p>{resolution} - The resolution of the picture</p>
         **/
        @Option(name = "--format")
        public String format = "{date}_{name}_{resolution}.jpg";

        @Option(name = "--threadCount")
        public int threadCount = 2;

        @Option(name = "--source")
        public int source = 1;

        @Option(name = "--resolution")
        public int resolution = 0;

        @Override
        public String toString() {
            try {
                StringBuilder s = new StringBuilder(Args.class.getSimpleName()).append("[");
                for (Field field : Args.class.getDeclaredFields()) {
                    s.append(field.getName());
                    s.append(" = ");
                    s.append(field.get(this));
                    s.append(", ");
                }
                return s.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}
