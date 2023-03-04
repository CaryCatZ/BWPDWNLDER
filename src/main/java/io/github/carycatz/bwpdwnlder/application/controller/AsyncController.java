/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.controller;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

public class AsyncController extends GenericController {
    AsyncController() {
        super(MoreExecutors.listeningDecorator(
                Executors.newCachedThreadPool(defaultThreadFactory())
        ));
    }
}
