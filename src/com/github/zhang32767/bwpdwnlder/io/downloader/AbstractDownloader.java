package com.github.zhang32767.bwpdwnlder.io.downloader;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.google.common.io.ByteSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.BufferedInputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.github.zhang32767.bwpdwnlder.main.Main.LOGGER;

abstract class AbstractDownloader implements Downloader {
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54";

    @Override
    public <T extends DownloadableFile> void download(T file) {
        download(file, 0);
    }

    private <T extends DownloadableFile> void download(T file, int tried) {
        InputStream in;
        ByteSink sink = Files.asByteSink(file, FileWriteMode.APPEND);

        try {
            URLConnection connection = new URL(file.getAddress()).openConnection();
            connection.setDoInput(true);
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.connect();
            in = connection.getInputStream();
            LOGGER.trace("Start transfer to {}", file.getPath());
            try (BufferedInputStream buf = new BufferedInputStream(in); OutputStream out = sink.openBufferedStream()) {
                buf.transferTo(out);
                out.flush();
            }
        } catch (Exception e) {
            if (++tried > 4) {
                LOGGER.error("Fail to download!", e);
            } else {
                LOGGER.warn("Cannot download, try again!");
                download(file, tried);
            }
        }
    }
}
