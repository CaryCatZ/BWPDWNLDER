package io.github.carycatz.bwpdwnlder.application;

import io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle;
import io.github.carycatz.bwpdwnlder.image.downloader.ImageDownloader;
import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.image.sources.SourceFactory;

import java.util.concurrent.atomic.AtomicInteger;

public final class Application extends LifeCycle {
    public static final String APPLICATION_NAME = "BWPDWNLDER";

    public static void run() {
        final AtomicInteger finished = new AtomicInteger();

        final Source source = SourceFactory.get(arguments.source, arguments.resolution);
        ImageDownloader imageDownloader = ImageDownloader.create(source, downloader, arguments.output.toPath(), arguments.format);

        imageDownloader.download(arguments.indexes, finished::getAndIncrement);

        while (finished.get() < arguments.indexes.size()) {
            Thread.yield();
        }
    }
}
