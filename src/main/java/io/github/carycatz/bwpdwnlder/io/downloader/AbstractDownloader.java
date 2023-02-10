package io.github.carycatz.bwpdwnlder.io.downloader;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ThreadPoolExecutor;

import static io.github.carycatz.bwpdwnlder.application.main.Main.LOGGER;

abstract class AbstractDownloader implements Downloader {
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54";

    protected final ThreadPoolExecutor executor;

    protected AbstractDownloader(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        download0(file, null, 0);
    }
    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        download0(file, reporterHook, 0);
    }

    @Override
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) executor.shutdown();
    }

    private <T extends DownloadableFile> void download0(T file, Runnable reporterHook, int tried) {
        InputStream in;

        try {
            URLConnection connection = new URL(file.getUrl()).openConnection();
            connection.setDoInput(true);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.connect();
            in = connection.getInputStream();
            LOGGER.trace("Start transfer to {}", file.getPath());
            try (BufferedInputStream buf = new BufferedInputStream(in); OutputStream out = Files.asByteSink(file, FileWriteMode.APPEND).openBufferedStream()) {
                buf.transferTo(out);
                out.flush();
            }

            // successfully finished
            if (reporterHook != null) {
                reporterHook.run();
            }

        } catch (Exception e) {
            if (++tried > 4) {
                LOGGER.error("Fail to download!", e);
            } else {
                LOGGER.warn("Cannot download, try again!");
                download0(file, reporterHook, tried);
            }
        }
    }
}
