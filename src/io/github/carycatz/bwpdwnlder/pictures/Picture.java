package io.github.carycatz.bwpdwnlder.pictures;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.nio.file.Path;

public final class Picture extends DownloadableFile {
    private final PictureInfo info;

    public Picture(PictureInfo info, String path) {
        this(info, path, info.date() + "_" + info.url().split("=")[1].split("&")[0]);
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
