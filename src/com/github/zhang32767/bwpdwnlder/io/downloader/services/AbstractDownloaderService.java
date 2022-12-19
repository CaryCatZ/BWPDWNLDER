package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.github.zhang32767.bwpdwnlder.io.downloader.Downloader;

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
        downloader.download(file);
    }
}
