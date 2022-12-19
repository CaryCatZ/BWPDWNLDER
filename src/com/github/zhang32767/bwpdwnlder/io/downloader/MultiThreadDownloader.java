package com.github.zhang32767.bwpdwnlder.io.downloader;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.zhang32767.bwpdwnlder.main.Main.LOGGER;

public class MultiThreadDownloader extends AbstractDownloader {
    private final ExecutorService executor;
    private int threadCount = 4;

    public MultiThreadDownloader(ExecutorService executor) {
        this.executor = executor;
    }

    public MultiThreadDownloader(ExecutorService executor, int threadCount) {
        this.executor = executor;
        this.threadCount = threadCount;
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        file.lock();
        long fileSize;
        try {
            fileSize = getFileSize(file);
            if (fileSize == -1) {
                super.download(file);
                LOGGER.info("Downloaded: {}", file);
            } else {
                downloadMultiThread(file, fileSize);
            }
        } finally {
            file.unlock();
        }
    }

    private <T extends DownloadableFile> void downloadMultiThread(T file, long fileSize) {
        AtomicInteger finished = new AtomicInteger();
        long singleChunkSize = fileSize / threadCount;

        for (int i = 0; i < threadCount; i++) {
            long startPos = i * singleChunkSize;
            long endPos = Math.min((i + 1) * singleChunkSize - 1, fileSize);
            int n = i;

            Runnable task = () -> {
                downloadAPart(file, startPos, endPos, finished, n, 0);
                finished.getAndIncrement();
                if (finished.get() == threadCount) { //is the last thread
                    LOGGER.info("Downloaded: {}", file);
                }
            };
            executor.execute(task);
        }
    }

    private <T extends DownloadableFile> void downloadAPart(T file, long from, long to, AtomicInteger finished, int i, int tried) {
        InputStream in;
        try {
            URLConnection connection = new URL(file.getAddress()).openConnection();
            connection.setDoInput(true);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.addRequestProperty("RANGE", "bytes=%s-%s".formatted(from, to));
            connection.connect();
            in = connection.getInputStream();

            while (finished.get() != i) {
                Thread.onSpinWait();
            }

            Files.asByteSink(file, FileWriteMode.APPEND).writeFrom(in);
        } catch (Exception e) {
            if (++tried > 4) {
                LOGGER.warn("Cannot download the part of file ({} - {}): {}", from, to, file);
                downloadAPart(file, from, to, finished, i, tried);
            } else {
                LOGGER.error("Cannot download the part of file ({} - {}): {}", from, to, file);
            }
        }
    }

    private <T extends DownloadableFile> long getFileSize(T file) {
        try {
            URLConnection connection = new URL(file.getAddress()).openConnection();
            connection.connect();
            return connection.getContentLengthLong();

        } catch (Exception e) {
            LOGGER.warn("Fail to get file size!", e);
            return -1;
        }
    }
}
