/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.StandardLevel;

import java.net.URI;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;

public final class LoggingUtil {
    public static void reconfigure(StandardLevel lvl, URI configPath) {
        Configurator.reconfigure(configPath);
        LOGGER.setLevel(Level.getLevel(lvl.name()));
    }

    private LoggingUtil() {
        throw new InstantiationError();
    }
}
