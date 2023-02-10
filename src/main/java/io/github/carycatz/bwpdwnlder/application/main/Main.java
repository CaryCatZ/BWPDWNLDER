package io.github.carycatz.bwpdwnlder.application.main;

import io.github.carycatz.bwpdwnlder.application.AbstractLifeCycle;
import io.github.carycatz.bwpdwnlder.application.Application;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.kohsuke.args4j.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public final class Main extends AbstractLifeCycle {
    public static void main(String[] s) {
        prepare(s);
        printDebugBlock();
        Application.run();
        cleanup();
    }

    private static void prepare(String[] s) {
        parseArgs(s);
        setupLogger();

        if (args.threadCount > 8) LOGGER.warn("Thread count may be too large: {}", args.threadCount);

        IS_SINGLE_THREAD_MODE = args.threadCount <= 0;
        executor = IS_SINGLE_THREAD_MODE ? null : (ThreadPoolExecutor) Executors.newCachedThreadPool(new ThreadFactory());
        downloader = IS_SINGLE_THREAD_MODE ? new SingleThreadDownloader() : new MultiThreadDownloader(executor, args.threadCount);
    }

    private static void parseArgs(String[] s) {
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
    }

    private static void setupLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        URL path = Objects.requireNonNull(Main.class.getResource(args.flag_debug ? "/META-INF/log4j2-debug.xml" : "/META-INF/log4j2.xml"));
        try {
            context.setConfigLocation(path.toURI());
            context.reconfigure();
        } catch (URISyntaxException ignored) { // Never happen
        }
    }

    private static void printDebugBlock() {
        LOGGER.debug("================== DEBUG ==================");
        LOGGER.debug("Arguments: {}", args);
        LOGGER.debug("Single-thread mode: {}", IS_SINGLE_THREAD_MODE);
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

    public static class Args {
        @Argument(usage = "indexes of images (0 is today's, 1 is yesterday's and so on)")
        public List<Integer> indexes = new ArrayList<>();

        @Option(name = "--debug")
        public boolean flag_debug = false;

        @Option(name = "--help", aliases = "-h")
        public boolean flag_help = false;

        @Option(name = "--format", aliases = "-F", usage = """
                The format of images
                {description} - The description of the picture.
                {name} - The name of the picture
                {date} - The date of the picture
                {resolution} - The resolution of the picture""")
        public String format = Image.DEFAULT_FORMAT;

        @Option(name = "--output", aliases = {"--out", "-o"}, usage = "Output")
        public Path output = Path.of(".");

        @Option(name = "--resolution", aliases = {"--res", "-R"}, usage = "The resolution of images")
        public Image.Resolution resolution = Image.Resolution.R_UHD;

        @Option(name = "--source", aliases = {"--src", "-S"}, usage = """
                The source to get the image url
                Official source, range: (0-15), url: https://cn.bing.com/HPImageArchive.aspx?format=js&idx=%s&n=1&mkt=zh-CN
                Ioliu source, url: https://bing.ioliu.cn/
                Bimg source, url: https://bimg.top/""")
        public Sources source = Sources.IOLIU;

        @Option(name = "--threadCount", usage = """
                The number of threads of downloading each image
                Zero is single-thread mode""")
        public int threadCount = 2;

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
        private static final AtomicInteger COUNT = new AtomicInteger();

        @Override
        public Thread newThread(@Nonnull Runnable r) {
            return new Thread(GROUP, r, "Thread-Downloading-%s".formatted(COUNT.getAndIncrement()));
        }
    }
}
