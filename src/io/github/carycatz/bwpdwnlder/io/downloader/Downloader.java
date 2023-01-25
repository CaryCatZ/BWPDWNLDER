package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

public interface Downloader {
    <T extends DownloadableFile> void download(T file);
}
