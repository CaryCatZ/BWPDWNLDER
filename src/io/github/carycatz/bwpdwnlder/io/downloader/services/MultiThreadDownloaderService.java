package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import io.github.carycatz.bwpdwnlder.main.Main;

import java.util.concurrent.ExecutorService;

public class MultiThreadDownloaderService extends AbstractDownloaderService<MultiThreadDownloader> {
    public MultiThreadDownloaderService(ExecutorService executor, MultiThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file) {
        executor.execute(() -> {
            Main.LOGGER.info("Start downloading: {}", file);
            downloader.download(file);
        });
    }
}
