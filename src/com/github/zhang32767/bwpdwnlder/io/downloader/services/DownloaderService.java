package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;


public interface DownloaderService {
    <T extends DownloadableFile> void download(T file);
}
