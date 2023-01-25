package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.carycatz.bwpdwnlder.main.Main.LOGGER;

public class MultiThreadDownloader extends AbstractDownloader {
    private final int threadCount;

    public MultiThreadDownloader(ExecutorService executor) {
        this(executor, 8);
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
        file.lock();
        long fileSize;
        try {
            fileSize = getFileSize(file);
            if (fileSize == -1) {
                super.download(file, reporterHook);
                LOGGER.info("Downloaded: {}", file);
            } else {
                LOGGER.debug("Start multi-thread downloading: count={}", threadCount);
                downloadMultiThread(file, reporterHook, fileSize);
            }
        } finally {
            file.unlock();
        }
    }

    private <T extends DownloadableFile> void downloadMultiThread(T file, Runnable reporterHook, long fileSize) {
        AtomicInteger finished = new AtomicInteger();
        long singleChunkSize = fileSize / threadCount;

        for (int i = 0; i < threadCount; i++) {
            long startPos = i * singleChunkSize;
            long endPos = Math.min((i + 1) * singleChunkSize - 1, fileSize);
            int n = i;

            executor.execute(() -> {
                LOGGER.trace("Start downloading from {} to {} ({} - {})", file.getAddress(), file.getPath(), startPos, endPos);
                downloadAPart(file, startPos, endPos, finished, n, 0);
                finished.getAndIncrement();
                if (finished.get() == threadCount) { //is the last thread
                    LOGGER.info("Downloaded: {}", file);
                    if (reporterHook != null) {
                        reporterHook.run();
                    }
                }
            });
        }
    }

    private <T extends DownloadableFile> void downloadAPart(T file, long startPos, long endPos, AtomicInteger finished, int i, int tried) {
        InputStream in;
        try {
            URLConnection connection = new URL(file.getAddress()).openConnection();
            connection.setDoInput(true);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.addRequestProperty("RANGE", "bytes=%s-%s".formatted(startPos, endPos));
            connection.connect();
            in = connection.getInputStream();

            LOGGER.trace("Connected to {}, start waiting!", file.getAddress());
            while (finished.get() != i) {
                Thread.onSpinWait();
            }

            LOGGER.trace("Start transfer to {} ({} - {})", file.getPath(), startPos, endPos);
            Files.asByteSink(file, FileWriteMode.APPEND).writeFrom(in);
            LOGGER.trace("Successfully downloaded to {} ({} - {})", file.getPath(), startPos, endPos);
        } catch (Exception e) {
            if (++tried < 4) {
                LOGGER.warn("Exception in  download the part of file ({} - {}): {}", startPos, endPos, file, e);
                downloadAPart(file, startPos, endPos, finished, i, tried);
            } else {
                LOGGER.error("Cannot download the part of file ({} - {}): {}", startPos, endPos, file, e);
            }
        }
    }

    private <T extends DownloadableFile> long getFileSize(T file) {
        try {
            URLConnection connection = new URL(file.getAddress()).openConnection();
            connection.connect();
            long size = connection.getContentLengthLong();
            LOGGER.debug("Got the file size of {}: {} KiB", file.getAddress(), size / 1024);

            return size;
        } catch (Exception e) {
            LOGGER.warn("Fail to get file size!", e);
            return -1;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "[" +
                "thread count = " + threadCount + "]";
    }
}
