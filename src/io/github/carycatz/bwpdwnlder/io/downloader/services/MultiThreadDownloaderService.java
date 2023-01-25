package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;

import java.util.concurrent.ExecutorService;

import static io.github.carycatz.bwpdwnlder.main.Main.LOGGER;

public class MultiThreadDownloaderService extends AbstractDownloaderService<MultiThreadDownloader> {
    public MultiThreadDownloaderService(ExecutorService executor, MultiThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        executor.execute(() -> {
            LOGGER.info("Start downloading: {}", file);
            downloader.download(file, reporterHook);
        });
    }
}
