/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading.application;

import io.github.carycatz.bwpdwnlder.application.controller.MainController;
import io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;
import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;

public abstract class DownloadApplicationRuntime extends ApplicationRuntime {
    public static Downloader downloader = new MultiThreadDownloader(MainController.getInstance());
}
