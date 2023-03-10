/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.io.downloader;

import com.google.common.base.Objects;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public abstract class GernericDownloader implements Downloader {
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54";

    protected final ExecutorService executor;

    protected GernericDownloader(ExecutorService executor) {
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
            writeFrom(file, in);

            // successfully finished
            if (reporterHook != null) {
                reporterHook.run();
            }

        } catch (IOException e) {
            if (++tried > 4) {
                LOGGER.error("Fail to download!", e);
            } else {
                LOGGER.warn("Cannot download, try again!");
                download0(file, reporterHook, tried);
            }
        }
    }

    protected static void writeFrom(File file, InputStream in, FileWriteMode... modes) {
        try {
            if (!file.getParentFile().exists()) {
                Files.createParentDirs(file);
            }
            Files.asByteSink(file, modes).writeFrom(in);
        } catch (IOException e) {
            LOGGER.error("Cannot write data from {} to {}", in, file);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "[" + "executor = " + executor + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GernericDownloader that)) return false;
        return Objects.equal(executor, that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executor);
    }
}
