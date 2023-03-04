/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.io.File;

public abstract class GenericImageDownloader implements ImageDownloader {
    protected final Source source;
    protected final Downloader downloader;
    protected final String format;

    protected GenericImageDownloader(Source source, Downloader downloader, String format) {
        this.source = source;
        this.downloader = downloader;
        this.format = format;
    }

    protected void download(Image.ImageInfo info, File path, String format, Runnable reporterHook) {
        downloader.download(new Image(info, path.toString(), info.format(format)), reporterHook);
    }
}
