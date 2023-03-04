/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.runtime;

import com.google.gson.Gson;
import io.github.carycatz.bwpdwnlder.application.main.BootstrapEnv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**<h2>Base Runtime Environments</h2>
 * */
public abstract class ApplicationRuntime {
    public static final String APPLICATION_NAME = "BWPDWNLDER";
    public static final Logger LOGGER = (Logger) LogManager.getLogger(APPLICATION_NAME);
    public static boolean SYNCHRONOUS_MODE = false;
    public static Gson GSON;

    @SuppressWarnings("unused")
    private static void configure(BootstrapEnv env) { // call with reflection
        SYNCHRONOUS_MODE = env.SYNCHRONOUS_MODE();
        GSON = env.gson();
    }
}
