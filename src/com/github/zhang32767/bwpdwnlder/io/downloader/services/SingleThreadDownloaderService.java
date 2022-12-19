package com.github.zhang32767.bwpdwnlder.io.downloader.services;


import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.github.zhang32767.bwpdwnlder.io.downloader.SingleThreadDownloader;

import java.util.concurrent.ExecutorService;

import static com.github.zhang32767.bwpdwnlder.main.Main.LOGGER;

public class SingleThreadDownloaderService extends AbstractDownloaderService<SingleThreadDownloader> {
    public SingleThreadDownloaderService(ExecutorService executor, SingleThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        LOGGER.info("Start downloading: {}", file);
        super.download(file);
    }
}
