/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading;

import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.io.File;
import java.util.List;

public class ImageDownloaderImpl extends GenericImageDownloader {
    protected ImageDownloaderImpl(Source source, Downloader service, String format) {
        super(source, service, format);
    }

    @Override
    public void download(int index, File path, Runnable reporterHook) {
        download(source.get(index), path, format, reporterHook);
    }

    @Override
    public void download(List<Integer> indexes, File path, Runnable reporterHook) {
        source.getWithAction(info -> download(info, path, format, reporterHook), indexes);
    }
}
