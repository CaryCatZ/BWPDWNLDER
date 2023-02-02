package io.github.carycatz.bwpdwnlder.image;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.nio.file.Path;

public final class Image extends DownloadableFile {
    public static final String DEFAULT_FORMAT = "{date}_{name}_{resolution}.jpg";

    private final ImageInfo info;

    public Image(ImageInfo info, String path) {
        this(info, path, info.format(DEFAULT_FORMAT));
    }

    public Image(ImageInfo info, String path, String filename) {
        super(Path.of(path, filename).toString(), info.url());
        this.info = info;
        this.url = info.url;
    }

    @Override
    public String toString() {
        return info.toString() + " on " + this.getPath();
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
            return "BingWallpaper: %s %s at %s".formatted(date, description, url);
        }
    }

    public enum Resolution {
        R_UHD(-1, -1), // best quality
        R_1080P(1920, 1080),
        R_720P(1280, 780),
        R_480(400, 480);

        public final int weight;
        public final int height;
        Resolution(int weight, int height) {
            this.weight = weight;
            this.height = height;
        }

        @Override
        public String toString() {
            return this == R_UHD ? "UHD" : weight + "x" + height;
        }
    }
}
