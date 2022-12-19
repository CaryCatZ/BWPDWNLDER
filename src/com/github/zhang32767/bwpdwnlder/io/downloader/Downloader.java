package com.github.zhang32767.bwpdwnlder.io.downloader;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;

public interface Downloader {
    <T extends DownloadableFile> void download(T file);
}
