/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

public final class Image extends DownloadableFile {
    public static final String DEFAULT_FORMAT = "{date}_{name}_{resolution}.jpg";

    private final ImageInfo info;

    public Image(ImageInfo info, String path, String filename) {
        super(Path.of(path, filename).toString(), info.url());
        this.info = info;
        this.url = info.url;
    }

    @Override
    public String toString() {
        return info.toString() + " on " + this.getPath();
    }

    public record ImageInfo(String url, String description, String name, String date, Resolution resolution) implements Serializable {
        @java.io.Serial
        private static final long serialVersionUID = 7257673156073390123L;

        public String format(String pattern) {
            return pattern.replace("{url}", url)
                    .replace("{description}", description.replace("/", "~"))
                    .replace("{name}", name)
                    .replace("{date}", date)
                    .replace("{resolution}", resolution.getString());
        }

        @Override
        public String toString() {
            return "BingWallpaper: %s %s at %s".formatted(date, description, url);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ImageInfo info
                    && Objects.equals(info.url, this.url)
                    && Objects.equals(info.description, this.description)
                    && Objects.equals(info.name, this.name)
                    && Objects.equals(info.date, this.date)
                    && Objects.equals(info.resolution, this.resolution);
        }

        @Override
        public int hashCode() {
            int hashCode = 0;
            hashCode += 31 * url.hashCode();
            hashCode += 31 * description.hashCode();
            hashCode += 31 * name.hashCode();
            hashCode += 31 * date.hashCode();
            hashCode += 31 * resolution.hashCode();
            return hashCode;
        }
    }

    public enum Resolution {
        R_UHD(-1, -1), // best quality
        R_1080P(1920, 1080),
        R_720P(1280, 720);

        public final int weight;
        public final int height;

        Resolution(int weight, int height) {
            this.weight = weight;
            this.height = height;
        }

//        """@Override
//        public String toString() {
//            return this == R_UHD ? "UHD" : weight + "x" + height;
//        }"""
        public String getString() {
            return this == R_UHD ? "UHD" : weight + "x" + height;
        }

        public static Resolution defaultValue() {
            return R_UHD;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && obj instanceof Image image
                && Objects.equals(image.info, this.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this, info);
    }
}
