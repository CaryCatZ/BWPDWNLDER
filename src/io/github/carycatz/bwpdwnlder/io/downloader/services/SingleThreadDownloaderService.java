package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;

import java.util.concurrent.ExecutorService;

import static io.github.carycatz.bwpdwnlder.core.Main.LOGGER;

public class SingleThreadDownloaderService extends AbstractDownloaderService<SingleThreadDownloader> {
    public SingleThreadDownloaderService(ExecutorService executor, SingleThreadDownloader downloader) {
        super(executor, downloader);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        LOGGER.info("Start downloading: {}", file);
        super.download(file, reporterHook);
    }
}
