/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.controller;

import java.util.concurrent.ExecutorService;

public final class MainController implements ControllerWarp {
    private static Controller instance;

    public static Controller getInstance() {
        return instance;
    }

    @SuppressWarnings("unused")
    private synchronized static void initialize(Controller controller) { // call this with reflection
        if (instance != null) {
            throw new ExceptionInInitializerError("Already initialized!");
        }
        instance = controller;
    }

    @Override
    public ExecutorService getImpl() {
        return instance;
    }
}
