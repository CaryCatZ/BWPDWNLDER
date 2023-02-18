package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import static io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle.LOGGER;

public class SingleThreadDownloader extends AbstractDownloader {
    public SingleThreadDownloader() {
        super(null);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        file.lock();
        try {
            super.download(file, reporterHook);
            LOGGER.info("Downloaded: {}", file);
        } finally {
            file.unlock();
        }
    }
}
