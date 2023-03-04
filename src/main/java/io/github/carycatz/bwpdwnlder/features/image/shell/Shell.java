/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.shell;

import com.beust.jcommander.ParameterException;
import com.google.common.annotations.Beta;
import io.github.carycatz.bwpdwnlder.application.command.GenericApplication;
import io.github.carycatz.bwpdwnlder.application.command.Argument;
import io.github.carycatz.bwpdwnlder.application.command.GenericCommand;
import io.github.carycatz.bwpdwnlder.application.command.CommandManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

@Beta
@SuppressWarnings("unused")
public class Shell extends GenericApplication<Argument> { // This is a bad thing
    private static final Shell APPLICATION = new Shell();

    public Shell() {
        super();
        CommandManager.getCommands().forEach((s, command) -> {
            if (!Objects.equals(s, "shell")) {
                ShellCommand.COMMAND.commander.addCommand(s, command.getArgumentInstance());
            }
        });
    }

    @Override
    public void execute(Argument arg) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("shell>");
            System.out.flush();
            if (scanner.hasNext()) {
                String s = scanner.next();
                if (!(executeInternalCommand(s) || executeOuterCommand(s))) {
                    System.err.println("Unknown command!");
                }
            } else {
                break;
            }
        }
    }

    private static boolean executeInternalCommand(String arg) {
        try {
            ShellCommand.COMMAND.argument.parse(arg.split(" "));
            CommandManager.getCommand(ShellCommand.COMMAND.argument.command).execute();
            return true;
        } catch (ParameterException ignored) {
        }
        return false;
    }

    private static boolean executeOuterCommand(String arg) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(arg);
            try (InputStream processIn = process.getInputStream();
                 OutputStream processOut = process.getOutputStream()) {

                while (process.isAlive()) {
                    if (System.in.available() > 0) {
                        processOut.write(System.in.readNBytes(System.in.available()));
                        processOut.flush();
                    } else if (processIn.available() > 0) {
                        String str = StandardCharsets.UTF_8.decode(
                                ByteBuffer.wrap(processIn.readNBytes(processIn.available()))
                        ).toString();
                        System.out.print(str);
                        System.out.flush();
                    }
                }
                return true;
            } catch (Exception ex) {
                System.err.printf("Exception in executing: %s%n", ex);
            }
        } catch (IOException | IllegalArgumentException ignored) {
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }

    @Override
    public GenericCommand<Argument> getCommand() {
        return ShellCommand.COMMAND;
    }
}
