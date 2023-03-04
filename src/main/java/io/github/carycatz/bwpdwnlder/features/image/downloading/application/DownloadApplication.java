/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading.application;

import io.github.carycatz.bwpdwnlder.application.command.GenericApplication;
import io.github.carycatz.bwpdwnlder.application.command.GenericCommand;
import io.github.carycatz.bwpdwnlder.features.image.downloading.ImageDownloader;
import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.features.image.source.SourceFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.carycatz.bwpdwnlder.features.image.downloading.application.DownloadApplicationRuntime.downloader;

@SuppressWarnings("unused")
public final class DownloadApplication extends GenericApplication<DownloadCommand.DownloadCommandArgument> {
    static final DownloadApplication APPLICATION = new DownloadApplication();

    private DownloadApplication() {
        super();
    }

    @Override
    public void execute(DownloadCommand.DownloadCommandArgument arg) {
        final AtomicInteger finished = new AtomicInteger();

        final Source source = SourceFactory.get(arg.source, arg.resolution);
        ImageDownloader imageDownloader = ImageDownloader.create(source, downloader, arg.format);

        imageDownloader.download(arg.indexes, arg.output, finished::getAndIncrement);

        while (finished.get() < arg.indexes.size()) {
            Thread.yield();
        }
    }

    @Override
    public GenericCommand<DownloadCommand.DownloadCommandArgument> getCommand() {
        return DownloadCommand.COMMAND;
    }

}
