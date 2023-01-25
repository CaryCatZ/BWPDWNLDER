package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

public interface DownloaderService {
    <T extends DownloadableFile> void download(T file);
}
