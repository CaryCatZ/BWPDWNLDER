/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.io.downloader;

import com.google.common.io.FileWriteMode;
import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public class MultiThreadDownloader extends GernericDownloader {
    private final int threadCount;

    @SuppressWarnings("unused")
    public MultiThreadDownloader(ExecutorService executor) {
        this(executor, 2);
    }

    public MultiThreadDownloader(ExecutorService executor, int threadCount) {
        super(executor);
        this.threadCount = threadCount;
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        download(file, null);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        LOGGER.info("Start downloading: {}", file);
        executor.execute(() -> {
            file.lock();
            long fileSize;
            try {
                fileSize = getFileSize(file);
                if (fileSize == -1) {
                    super.download(file, reporterHook);
                    LOGGER.info("Downloaded: {}", file);
                } else {
                    LOGGER.trace("Start multi-thread downloading: count={}", threadCount);
                    download0(file, reporterHook, fileSize);
                }
            } finally {
                file.unlock();
            }
        });
    }

    private <T extends DownloadableFile> void download0(T file, Runnable reporterHook, long fileSize) {
        final AtomicInteger finished = new AtomicInteger();
        long singleChunkSize = fileSize / threadCount;

        for (int i = 0; i < threadCount; i++) {
            long startPos = i * singleChunkSize;
            long endPos = Math.min((i + 1) * singleChunkSize - 1, fileSize + 1);
            final int n = i;

            executor.execute(() -> {
                LOGGER.trace("Start downloading from {} to {} ({} - {})", file.getUrl(), file.getPath(), startPos, endPos);
                download1(file, startPos, endPos, finished, n, 0);
                if (finished.incrementAndGet() == threadCount) { //all finished
                    LOGGER.info("Downloaded: {}", file);
                    if (reporterHook != null) {
                        reporterHook.run();
                    }
                }
            });
        }
    }

    private <T extends DownloadableFile> void download1(T file, long startPos, long endPos, AtomicInteger finished, int i, int tried) {
        InputStream in;
        try {
            URLConnection connection = new URL(file.getUrl()).openConnection();
            connection.setDoInput(true);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.addRequestProperty("RANGE", "bytes=%s-%s".formatted(startPos, endPos));
            connection.connect();
            in = connection.getInputStream();

            LOGGER.trace("Connected to {}, waiting!", file.getUrl());
            while (finished.get() != i) {
                Thread.yield();
            }

            LOGGER.trace("Start transfer to {} ({} - {})", file.getPath(), startPos, endPos);
            writeFrom(file, in, FileWriteMode.APPEND);
            LOGGER.trace("Successfully downloaded to {} ({} - {})", file.getPath(), startPos, endPos);
        } catch (Exception e) {
            if (++tried < 4) {
                LOGGER.warn("Exception in  download the part of file ({} - {}): {}", startPos, endPos, file, e);
                download1(file, startPos, endPos, finished, i, tried);
            } else {
                LOGGER.error("Cannot download the part of file ({} - {}): {}", startPos, endPos, file, e);
            }
        }
    }

    private <T extends DownloadableFile> long getFileSize(T file) {
        try {
            URLConnection connection = new URL(file.getUrl()).openConnection();
            connection.connect();
            long size = connection.getContentLengthLong();
            LOGGER.debug("Got the file size of {}: {} KiB", file.getUrl(), size / 1024);

            return size;
        } catch (Exception e) {
            LOGGER.warn("Fail to get file size!", e);
            return -1;
        }
    }
}
