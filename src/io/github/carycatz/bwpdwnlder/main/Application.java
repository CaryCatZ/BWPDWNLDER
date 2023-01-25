package io.github.carycatz.bwpdwnlder.main;

import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Source;

public class Application {
    static void run(Main.Args args) {
        Source source = Source.source(args.source).resolution(args.resolution);
        Main.executor.execute(() -> source.gets(args.indexes).forEach(info -> Main.downloaderService.download(new Image(info, args.path, info.format(args.format)))));
    }
}
