package io.github.carycatz.bwpdwnlder.image;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.nio.file.Path;

public class Image extends DownloadableFile {
    private final ImageInfo info;

    public Image(ImageInfo info, String path) {
        this(info, path, info.date() + "_" + info.url().split("=")[1].split("&")[0]);
    }

    public Image(ImageInfo info, String path, String filename) {
        super(Path.of(path, filename).toString(), info.url());
        this.info = info;
        this.url = info.url;
    }

    @Override
    public String toString() {
        return info.toString();
    }

    public record ImageInfo(String url, String description, String name, String date, Resolution resolution) {
        public String format(String pattern) {
            return pattern.replace("{url}", url)
                    .replace("{description}", description)
                    .replace("{name}", name)
                    .replace("{date}", date)
                    .replace("{resolution}", resolution.toString());
        }

        @Override
        public String toString() {
            return "BingWallpaper: %s %s from %s".formatted(date, description, url);
        }
    }
}
