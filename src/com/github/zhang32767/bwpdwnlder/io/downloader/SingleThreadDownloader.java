package com.github.zhang32767.bwpdwnlder.io.downloader;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;

import static com.github.zhang32767.bwpdwnlder.main.Main.LOGGER;

public class SingleThreadDownloader extends AbstractDownloader {
    @Override
    public <T extends DownloadableFile> void download(T file) {
        file.lock();
        try {
            super.download(file);
            LOGGER.info("Downloaded: {}", file);
        } finally {
            file.unlock();
        }
    }
}
