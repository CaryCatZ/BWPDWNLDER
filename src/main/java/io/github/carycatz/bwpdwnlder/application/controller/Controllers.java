/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.controller;

import io.github.carycatz.bwpdwnlder.misc.LazyInitializer;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum Controllers implements ControllerWarp {
    ASYNC((LazyInitializer<Controller>) AsyncController::new),
    SYNC((LazyInitializer<Controller>) SynchronousController::new);

    private final Supplier<Controller> supplier;

    Controllers(Supplier<Controller> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Controller getImpl() {
        return supplier.get();
    }
}
