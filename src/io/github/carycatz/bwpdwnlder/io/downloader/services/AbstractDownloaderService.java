package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.util.concurrent.ExecutorService;

public abstract class AbstractDownloaderService<DOWNLOADER extends Downloader> implements DownloaderService {
    protected final ExecutorService executor;
    protected final DOWNLOADER downloader;

    AbstractDownloaderService(ExecutorService executor, DOWNLOADER downloader) {
        this.executor = executor;
        this.downloader = downloader;
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        download(file, null);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        downloader.download(file, reporterHook);
    }

    @Override
    public void shutdown() {
        if (!executor.isShutdown()) executor.shutdown();
        downloader.shutdown();
    }

    @Override
    public String toString() {
        return super.toString() + "[" +
                "executor = " + executor + ", " +
                "downloader = " + downloader + "]";
    }
}
