package io.github.carycatz.bwpdwnlder.application.main;

import io.github.carycatz.bwpdwnlder.application.Application;
import io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle;
import io.github.carycatz.bwpdwnlder.application.lifecycle.logging.LoggingUtil;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public final class Main extends LifeCycle {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown, "Bwpdwnlder-Shutdown-Hook"));
    }

    public static void main(String[] s) {
        initialize(s);
        printDebugBlock();
        Application.run();
    }

    private static void initialize(String[] s) {
        arguments.parse(s.length == 0 ?
                Optional.ofNullable(System.getProperty("bwpdwnlder.args")).orElse("").split(" ") : s
        );

        LoggingUtil.reconfigure(arguments.logLvl);

        if (arguments.threadCount > 8) {
            LOGGER.warn("Thread count may be too large: {}", arguments.threadCount);
        }

        IS_SINGLE_THREAD_MODE = arguments.threadCount <= 0;

        executor = IS_SINGLE_THREAD_MODE ?
                null : (ThreadPoolExecutor) Executors.newCachedThreadPool(new ThreadFactory());
        downloader = IS_SINGLE_THREAD_MODE ?
                new SingleThreadDownloader() : new MultiThreadDownloader(executor, arguments.threadCount);
    }

    private static void printDebugBlock() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{}{}{}", "=".repeat(17), " DEBUG ", "=".repeat(17));
            LOGGER.debug("Java version: {}", Runtime.version());
            LOGGER.debug("Arguments: {}", arguments);
            LOGGER.debug("Single-thread mode: {}", IS_SINGLE_THREAD_MODE);
            LOGGER.debug("ThreadPoolExecutor: {}", executor);
            LOGGER.debug("Downloader: {}", downloader);
            LOGGER.debug("{}{}{}", "=".repeat(17), " DEBUG ", "=".repeat(17));
        }
    }

    private static void shutdown() {
        LOGGER.debug("Shutting down...");
        LOGGER.debug("{}", downloader);
        downloader.shutdown();
        LogManager.shutdown();
        arguments = null;
        downloader = null;
        executor = null;
    }

    private static final class ThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final ThreadGroup GROUP = new ThreadGroup("Group-Downloading");
        private static final AtomicInteger COUNT = new AtomicInteger();

        @Override
        public Thread newThread(@Nonnull Runnable r) {
            Thread thread = new Thread(GROUP, r, "Thread-Downloading-%s".formatted(COUNT.getAndIncrement()));
            thread.setDaemon(true);
            return thread;
        }
    }
}
