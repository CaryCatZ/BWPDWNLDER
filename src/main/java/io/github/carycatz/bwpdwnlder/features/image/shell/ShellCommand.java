/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.shell;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.annotations.Beta;
import io.github.carycatz.bwpdwnlder.application.command.Argument;
import io.github.carycatz.bwpdwnlder.application.command.GenericCommand;
import io.github.carycatz.bwpdwnlder.application.command.CommandManager;

import java.util.Optional;

@Beta
public class ShellCommand extends GenericCommand<Argument> {
    final JCommander commander = JCommander.newBuilder()
            .programName("BWPDWNLDER")
            .acceptUnknownOptions(false)
            .build();

    static final ShellCommand COMMAND = new ShellCommand();

    final ShellArgument argument = new ShellArgument(commander);

    protected ShellCommand() {
        super("shell", "io.github.carycatz.bwpdwnlder.features.image.shell.Shell");
    }

    @Override
    public Argument getArgumentInstance() {
        return argument;
    }

    static {
        CommandManager.register(COMMAND);
    }

    @Parameters(commandNames = "shell")
    static final class ShellArgument extends Argument {
        private final JCommander commander;

        ShellArgument(JCommander commander) {
            this.commander = commander;
            commander.addObject(this);
        }

        @Parameter(names = {"--help", "-h"}, help = true)
        public boolean help = false;

        public String command;

        public void parse(String[] arg) {
            commander.parse(arg);
            command = help ? "help" :
                    Optional.ofNullable(commander.getParsedCommand()).orElse("help");
        }
    }
}
