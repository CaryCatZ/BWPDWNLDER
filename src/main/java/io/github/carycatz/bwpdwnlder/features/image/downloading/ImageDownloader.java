/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading;

import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.io.File;
import java.util.List;

@SuppressWarnings("unused")
public interface ImageDownloader {
    static ImageDownloader create(Source source, Downloader downloader, String format) {
        return new ImageDownloaderImpl(source, downloader, format);
    }

    default void download(int index, File path) {
        download(index, path, null);
    }

    default void download(List<Integer> indexes, File path) {
        download(indexes, path, null);
    }

    void download(int index, File path, Runnable reporterHook);

    void download(List<Integer> indexes, File path, Runnable reporterHook);
}
