package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.main.Main;

import java.util.concurrent.ExecutorService;

public class SingleThreadDownloader extends AbstractDownloader {
    public SingleThreadDownloader(ExecutorService executor) {
        super(executor);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        file.lock();
        try {
            super.download(file, reporterHook);
            Main.LOGGER.info("Downloaded: {}", file);
        } finally {
            file.unlock();
        }
    }
}
