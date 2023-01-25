package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;
import io.github.carycatz.bwpdwnlder.main.Main;

public class SingleThreadDownloader extends AbstractDownloader {
    @Override
    public <T extends DownloadableFile> void download(T file) {
        file.lock();
        try {
            super.download(file);
            Main.LOGGER.info("Downloaded: {}", file);
        } finally {
            file.unlock();
        }
    }
}
