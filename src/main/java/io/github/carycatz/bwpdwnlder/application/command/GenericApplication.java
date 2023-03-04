/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.command;

import io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime;

public abstract class GenericApplication<T extends Argument> extends ApplicationRuntime implements Application<T> {
    protected GenericApplication() {
    }
}
