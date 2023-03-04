/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.main;

import com.google.gson.GsonBuilder;
import io.github.carycatz.bwpdwnlder.application.controller.Controller;
import io.github.carycatz.bwpdwnlder.application.controller.MainController;
import io.github.carycatz.bwpdwnlder.application.controller.SynchronousController;
import io.github.carycatz.bwpdwnlder.application.logging.LoggingUtil;
import io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime;

import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public final class PreInitializer {
    public static final String[] COMMAND_FQCN = new String[]{
            "io.github.carycatz.bwpdwnlder.features.image.downloading.application.DownloadCommand",
//            "io.github.carycatz.bwpdwnlder.features.image.shell.ShellCommand"
    };

    private PreInitializer() {
        throw new InstantiationError();
    }

    static BootstrapEnv initialize(String[] arg) {
        Arguments arguments = new Arguments();

        for (String reflection : COMMAND_FQCN) {
            try {
                Class.forName(reflection);
            } catch (ClassNotFoundException ignored) {
            }
        }

        arguments.parse(arg.length == 0 ?
                Optional.ofNullable(System.getProperty("bwpdwnlder.args")).orElse("").split(" ") : arg
        );

        boolean SYNCHRONOUS_MODE = arguments.controller.getImpl() instanceof SynchronousController;

        BootstrapEnv env = new BootstrapEnv(
                arguments,
                SYNCHRONOUS_MODE,
                new GsonBuilder()
                        .setPrettyPrinting()
                        .disableHtmlEscaping()
                        .create()
        );

        try {
            Method initializeMethod = MainController.class.getDeclaredMethod("initialize", Controller.class);
            initializeMethod.setAccessible(true);
            initializeMethod.invoke(null, arguments.controller.getImpl());
        } catch (ReflectiveOperationException e) {
            fatal(e);
        }


        try {
            Method configureMethod = ApplicationRuntime.class.getDeclaredMethod("configure", BootstrapEnv.class);
            configureMethod.setAccessible(true);
            configureMethod.invoke(null, env);
        } catch (ReflectiveOperationException e) {
            fatal(e);
        }

        LoggingUtil.reconfigure(arguments.logLevel, arguments.loggingStyle.uri);

        return env;
    }

    private static void fatal(Throwable cause) {
        LOGGER.fatal("Cannot initializer application!", cause);
        throw new ExceptionInInitializerError(cause);
    }
}
