/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.downloading.application;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import io.github.carycatz.bwpdwnlder.application.command.Argument;
import io.github.carycatz.bwpdwnlder.application.command.GenericCommand;
import io.github.carycatz.bwpdwnlder.application.command.CommandManager;
import io.github.carycatz.bwpdwnlder.features.image.Image;
import io.github.carycatz.bwpdwnlder.features.image.source.Sources;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class DownloadCommand extends GenericCommand<DownloadCommand.DownloadCommandArgument> {
    static final DownloadCommand COMMAND = new DownloadCommand();

    private final DownloadCommandArgument argument = new DownloadCommandArgument();

    private DownloadCommand() {
        super("download", "io.github.carycatz.bwpdwnlder.features.image.downloading.application.DownloadApplication");
    }

    @Override
    public DownloadCommandArgument getArgumentInstance() {
        return argument;
    }

    static {
        CommandManager.register(COMMAND);
    }

    @Parameters(commandNames = "download")
    public static final class DownloadCommandArgument extends Argument {
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
    }
}
