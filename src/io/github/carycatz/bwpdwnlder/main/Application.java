package io.github.carycatz.bwpdwnlder.main;

import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Source;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.carycatz.bwpdwnlder.main.Main.*;

public class Application {
    static void run(Args args) {
        final AtomicInteger finished = new AtomicInteger();

        Source source = Source.source(args.source).resolution(args.resolution);
        executor.execute(() -> { // async download
            source.gets(args.indexes).forEach(info -> { // download for each image
                downloaderService.download(new Image(info, args.path, info.format(args.format)), finished::getAndIncrement);
            });
        });

        while (finished.get() < args.indexes.size()) {
            Thread.onSpinWait();
        }
    }
}
