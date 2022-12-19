package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.github.zhang32767.bwpdwnlder.io.downloader.MultiThreadDownloader;

import java.util.concurrent.ExecutorService;

import static com.github.zhang32767.bwpdwnlder.main.Main.LOGGER;

public class MultiThreadDownloaderService extends AbstractDownloaderService<MultiThreadDownloader> {
    public MultiThreadDownloaderService(ExecutorService executor, MultiThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        executor.execute(() -> {
            LOGGER.info("Start downloading: {}", file);
            downloader.download(file);
        });
    }
}
