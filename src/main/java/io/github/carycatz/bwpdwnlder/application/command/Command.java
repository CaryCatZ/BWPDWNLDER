/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.command;

import java.util.function.Consumer;

public interface Command<T extends Argument> extends Consumer<T> {
    default void execute() {
        execute(getArgumentInstance());
    }

    void execute(T arg);

    T getArgumentInstance();

    String getName();

    @Override
    default void accept(T t) {
        execute(t);
    }
}
