/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.command;

import io.github.carycatz.bwpdwnlder.misc.LazyInitializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public class CommandManager {
    private static final Map<String, Command<?>> COMMANDS = new ConcurrentHashMap<>();
    private static Consumer<Command<?>> listener = command -> {};

    public static void register(Command<?> command) {
        COMMANDS.put(command.getName(), command);
        listener.accept(command);
    }

    public static Command<?> getCommand(String name) {
        return name == null ? DefaultErrorCommand.SUPPLIER.get() : COMMANDS.get(name);
    }

    public static Map<String, Command<?>> getCommands() {
        return new HashMap<>(COMMANDS);
    }

    public static void addListener(Consumer<Command<?>> listener) {
        CommandManager.listener = CommandManager.listener.andThen(listener);
    }

    private static final class DefaultErrorCommand extends GenericCommand<Argument> {
        private static final Supplier<DefaultErrorCommand> SUPPLIER = (LazyInitializer<DefaultErrorCommand>) DefaultErrorCommand::new;

        private DefaultErrorCommand() {
            super("<default error command>", "");
        }

        @Override
        public Argument getArgumentInstance() {
            return null;
        }

        @Override
        public void execute() {
            LOGGER.error("""
                    If you see this message, that means something went wrong and the application may has some problems.
                    You can report this to the issues;
                    """);
        }
    }
}
