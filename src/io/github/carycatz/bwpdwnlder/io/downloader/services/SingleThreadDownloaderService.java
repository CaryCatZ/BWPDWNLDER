package io.github.carycatz.bwpdwnlder.io.downloader.services;


import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;
import io.github.carycatz.bwpdwnlder.main.Main;

import java.util.concurrent.ExecutorService;

public class SingleThreadDownloaderService extends AbstractDownloaderService<SingleThreadDownloader> {
    public SingleThreadDownloaderService(ExecutorService executor, SingleThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        Main.LOGGER.info("Start downloading: {}", file);
        super.download(file);
    }
}
