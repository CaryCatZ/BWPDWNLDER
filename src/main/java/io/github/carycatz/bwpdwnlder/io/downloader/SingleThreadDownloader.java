/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

@Deprecated(since = "0.3")
public class SingleThreadDownloader extends GernericDownloader {
    public SingleThreadDownloader() {
        super(null);
    }

    @Override
    public <T extends DownloadableFile> void download(T file, Runnable reporterHook) {
        file.lock();
        try {
            super.download(file, reporterHook);
            LOGGER.info("Downloaded: {}", file);
        } finally {
            file.unlock();
        }
    }
}
