/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.application.controller.MainController;
import io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime;

import java.util.concurrent.ExecutorService;

public abstract class SourceRuntime extends ApplicationRuntime {
    public static final ExecutorService executor = MainController.getInstance();
    public static final String BING = "https://cn.bing.com";
}
