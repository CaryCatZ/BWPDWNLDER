package io.github.carycatz.bwpdwnlder.main;

import io.github.carycatz.bwpdwnlder.image.downloader.ImageDownloader;
import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.image.sources.SourceFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.carycatz.bwpdwnlder.main.Main.Args;
import static io.github.carycatz.bwpdwnlder.main.Main.downloader;

public final class Application {
    public static void run(Args args) {
        final AtomicInteger finished = new AtomicInteger();

        final Source source = SourceFactory.get(args.source, args.resolution);
        ImageDownloader imageDownloader = ImageDownloader.create(source, downloader, args.output, args.format);

        imageDownloader.download(args.indexes, finished::getAndIncrement);

        while (finished.get() < args.indexes.size()) {
            Thread.onSpinWait();
        }
    }
}
