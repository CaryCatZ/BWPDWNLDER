/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import io.github.carycatz.bwpdwnlder.application.command.Argument;
import io.github.carycatz.bwpdwnlder.application.command.Command;
import io.github.carycatz.bwpdwnlder.application.command.GenericCommand;
import io.github.carycatz.bwpdwnlder.application.command.CommandManager;
import io.github.carycatz.bwpdwnlder.application.controller.Controllers;
import io.github.carycatz.bwpdwnlder.application.logging.LoggingStyle;
import org.apache.logging.log4j.spi.StandardLevel;

import java.util.Optional;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public final class Arguments {
    private static final JCommander COMMANDER = JCommander.newBuilder()
            .programName("BWPDWNLDER")
            .acceptUnknownOptions(false)
            .allowAbbreviatedOptions(false)
            .build();

    static {
        CommandManager.register(new HelpCommand());
        CommandManager.register(new TellOfWrongCommand());
        CommandManager.addListener(Arguments::addCommand);
        CommandManager.getCommands().values().forEach(Arguments::addCommand);
    }

    public Arguments() {
        COMMANDER.addObject(this);
    }

    @Parameter(names = {"--controller", "--ctrl-lr"}, description = "The main controller")
    public Controllers controller = Controllers.ASYNC;

    @Parameter(names = "--log-level", description = "The logging level")
    public StandardLevel logLevel = StandardLevel.INFO;

    @Parameter(names = "--logging-style", description = "The style of logging")
    public LoggingStyle loggingStyle = LoggingStyle.LESS;

    @Parameter(names = {"--help", "-h"}, help = true)
    public boolean help = false;

    public String command;

    public void parse(String[] args) {
        try {
            COMMANDER.parse(args);
            command = help ? "_help" :
                    Optional.ofNullable(COMMANDER.getParsedCommand()).orElse("_help");
        } catch (ParameterException e) {
            LOGGER.error(e.getMessage());
            command = "_wrong";
        }
    }

    private static void addCommand(Command<?> command) {
        if (command.getName().startsWith("_")) return;
        COMMANDER.addCommand(command.getName(), command.getArgumentInstance());
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "controller=" + controller +
                ", logLevel=" + logLevel +
                ", loggingStyle=" + loggingStyle +
                ", help=" + help +
                ", command='" + command + '\'' +
                '}';
    }

    public static final class HelpCommand extends GenericCommand<Argument> {
        private HelpCommand() {
            super("_help", "");
        }

        @Override
        public void execute(Argument arg) {
            COMMANDER.usage();
        }

        @Override
        public Argument getArgumentInstance() {
            return new Argument() {};
        }
    }

    public static final class TellOfWrongCommand extends GenericCommand<Argument> {

        private TellOfWrongCommand() {
            super("_wrong", "");
        }

        @Override
        public void execute() {
            LOGGER.error("Wrong command! Use --help/-h to see the usage.");
        }

        @Override
        public Argument getArgumentInstance() {
            return new Argument() {};
        }
    }
}
