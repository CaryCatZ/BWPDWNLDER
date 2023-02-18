package io.github.carycatz.bwpdwnlder.application.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import org.apache.logging.log4j.spi.StandardLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public final class Arguments {
    @Parameter
    public List<Integer> indexes = new LinkedList<>(List.of(0));

    @Parameter(names = {"--format", "-f"}, description = """
                The format of images
                        {description} - The description of the picture.
                        {name} - The name of the picture
                        {date} - The date of the picture
                        {resolution} - The resolution of the picture""")
    public String format = Image.DEFAULT_FORMAT;

    @Parameter(names = {"--output", "-out", "-o"}, description = "Output path")
    public File output = Path.of(".").toFile();

    @Parameter(names = {"--resolution", "-res", "-R"}, description = "The resolution of each image")
    public Image.Resolution resolution = Image.Resolution.R_UHD;

    @Parameter(names = {"--source", "-src", "-S"}, description = "The source to get the image url")
    public Sources source = Sources.IOLIU;

    @Parameter(names = {"--thread-count", "-tc"}, description = "The number of threads of downloading each image. Zero is single-thread mode")
    public int threadCount = 2;

    @Parameter(names = {"--log-level", "-L"})
    public StandardLevel logLvl = StandardLevel.INFO;

    @Parameter(names = {"--help", "-h"})
    public boolean help = false;

    public void parse(String[] args) {
        JCommander commander = JCommander.newBuilder()
                .programName("BWPDWNLDER")
                .addObject(this)
                .acceptUnknownOptions(false)
                .build();
        commander.parse(args);
        if (this.help) {
            commander.usage();
            System.exit(0);
        }
    }
}
