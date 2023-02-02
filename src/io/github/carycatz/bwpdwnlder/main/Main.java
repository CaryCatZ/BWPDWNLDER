package io.github.carycatz.bwpdwnlder.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public final class Main {
    static ThreadPoolExecutor executor;
    static Downloader downloader;
    static Args args;
    public static final Logger LOGGER = LogManager.getLogger("bwpdwnlder");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static boolean isSingleThreadMode = false;

    public static void main(String[] s) {
        prepare(s);
        printDebugBlock();
        Application.run(args);
        cleanup();
    }

    private static void prepare(String[] s) {
        args = new Args();

        try {
            CmdLineParser parser = new CmdLineParser(args);
            parser.parseArgument(s);
            if (s.length == 0 || args.flag_help) {
                printUsage();
                System.exit(0);
            }
        } catch (CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            System.out.println("Use --help or -h to get help");
            System.exit(1);
        }

        if (args.threadCount > 8) LOGGER.warn("Thread count may be too large: {}", args.threadCount);

        isSingleThreadMode = args.threadCount <= 0;
        executor = isSingleThreadMode ? null : (ThreadPoolExecutor) Executors.newCachedThreadPool(new ThreadFactory());
        downloader = isSingleThreadMode ? new SingleThreadDownloader() : new MultiThreadDownloader(executor, args.threadCount);
    }

    private static void printDebugBlock() {
        LOGGER.debug("================== DEBUG ==================");
        LOGGER.debug("Arguments: {}", args);
        LOGGER.debug("single-thread mode: {}", isSingleThreadMode);
        LOGGER.debug("ThreadPoolExecutor: {}", executor);
        LOGGER.debug("Downloader: {}", downloader);
        LOGGER.debug("================== DEBUG ==================");
    }

    private static void cleanup() {
        downloader.shutdown();
        args = null;
        downloader = null;
        executor = null;
    }

    private static void printUsage() {
        new CmdLineParser(args).printUsage(System.out);
    }

    static class Args {
        @Argument(usage = "indexes of images (0 is today's, 1 is yesterday's and so on)")
        public List<Integer> indexes = new ArrayList<>();

        @Option(name = "--help", aliases = "-h", hidden = true)
        public boolean flag_help = false;

        @Option(name = "--output", aliases = {"--out", "-o"}, usage = "Output")
        public Path output = Path.of(".");

        /**
         * <p>{url} - The url of the picture</p>
         * <p>{description} - The description of the picture.</p>
         * <p>{name} - The name of the picture</p>
         * <p>{date} - The date of the picture</p>
         * <p>{resolution} - The resolution of the picture</p>
         **/
        @Option(name = "--format", aliases = "-F", usage = """
                The format of images
                {description} - The description of the picture.
                {name} - The name of the picture
                {date} - The date of the picture
                {resolution} - The resolution of the picture""")
        public String format = Image.DEFAULT_FORMAT;

        @Option(name = "--threadCount", usage = """
                The number of threads of downloading each image
                Zero is single-thread mode""")
        public int threadCount = 2;

        @Option(name = "--source", aliases = {"--src", "-S"}, usage = """
                The source to get the image url
                Official source, range: (0-15), url: https://cn.bing.com/HPImageArchive.aspx?format=js&idx=%s&n=1&mkt=zh-CN
                Ioliu source, url: https://bing.ioliu.cn/
                Bimg source, url: https://bimg.top/""")
        public Sources source = Sources.IOLIU;

        @Option(name = "--resolution", aliases = {"--res", "-R"}, usage = "The resolution of images")
        public Image.Resolution resolution = Image.Resolution.R_UHD;

        @Override
        public String toString() {
            try {
                StringBuilder s = new StringBuilder(super.toString()).append("[");
                for (Field field : Args.class.getDeclaredFields()) {
                    s.append(field.getName());
                    s.append(" = ");
                    s.append(field.get(this));
                    s.append(", ");
                }
                s.delete(s.length()-2, s.length()-1).append("]");
                return s.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }

    private static final class ThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final ThreadGroup GROUP = new ThreadGroup("Group-Downloading");
        private static final AtomicInteger count = new AtomicInteger();

        @Override
        public Thread newThread(@Nonnull Runnable r) {
            return new Thread(GROUP, r, "Thread-Downloading-%s".formatted(count.getAndIncrement()));
        }
    }
}
