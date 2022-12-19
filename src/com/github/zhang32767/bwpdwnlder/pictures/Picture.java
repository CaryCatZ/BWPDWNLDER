package com.github.zhang32767.bwpdwnlder.pictures;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;

import java.nio.file.Path;

public class Picture extends DownloadableFile {
    private final PictureInfo info;

    public Picture(PictureInfo info, String path) {
        super(Path.of(path, info.date() + "_" + info.url().split("=")[1].split("&")[0]).toString(), info.url());
        this.info = info;
    }

    public Picture(PictureInfo info, String path, String filename) {
        super(Path.of(path, filename).toString(), info.url());
        this.info = info;
    }

    @Override
    public String toString() {
        return info.toString();
    }

    public record PictureInfo(String url, String description, String date, int num) {
        @Override
        public String toString() {
            return "BingWallpaper: %s %s from %s".formatted(date, description, url);
        }
    }
}
