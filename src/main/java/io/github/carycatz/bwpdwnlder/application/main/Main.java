/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.main;

import io.github.carycatz.bwpdwnlder.application.command.CommandManager;
import io.github.carycatz.bwpdwnlder.application.controller.MainController;
import org.apache.logging.log4j.LogManager;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public final class Main {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown, "Bwpdwnlder-Shutdown-Hook"));
    }

    public static void main(String[] arg) {
        BootstrapEnv env = initialize(arg);
        printVersionInfo();
        printDebugInfo(env);
        String command = env.arguments().command;
        CommandManager.getCommand(command).execute();
    }

    private static BootstrapEnv initialize(String[] arg) {
        return PreInitializer.initialize(arg);
    }

    private static void printVersionInfo() {
        LOGGER.info(AppVersion.getInstance());
        LOGGER.info("");
    }

    private static void printDebugInfo(BootstrapEnv env) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("=".repeat(41));
            LOGGER.debug("Bootstrap env: {}", env);
            LOGGER.debug("Controller: {}", MainController.getInstance());
            LOGGER.debug("=".repeat(41));
        }
    }

    private static void shutdown() {
        LOGGER.debug("Shutting down...");
        MainController.getInstance().shutdown();
        LogManager.shutdown();
    }
}
