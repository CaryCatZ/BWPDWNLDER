package io.github.carycatz.bwpdwnlder.application;

import io.github.carycatz.bwpdwnlder.image.downloader.ImageDownloader;
import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.image.sources.SourceFactory;

import java.util.concurrent.atomic.AtomicInteger;

public final class Application extends AbstractLifeCycle {
    public static void run() {
        final AtomicInteger finished = new AtomicInteger();

        final Source source = SourceFactory.get(args.source, args.resolution);
        ImageDownloader imageDownloader = ImageDownloader.create(source, downloader, args.output.toPath(), args.format);

        imageDownloader.download(args.indexes, finished::getAndIncrement);

        while (finished.get() < args.indexes.size()) {
            Thread.onSpinWait();
        }
    }
}
