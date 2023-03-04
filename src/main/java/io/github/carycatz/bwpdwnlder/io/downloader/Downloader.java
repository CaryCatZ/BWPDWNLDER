/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.io.downloader;

import io.github.carycatz.bwpdwnlder.io.DownloadableFile;

@SuppressWarnings("unused")
public interface Downloader {
    <T extends DownloadableFile> void download(T file);

    <T extends DownloadableFile> void download(T file, Runnable reporterHook);

    void shutdown();
}
