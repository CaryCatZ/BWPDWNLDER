/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.command;

import java.lang.reflect.Field;

public abstract class GenericCommand<T extends Argument> implements Command<T> {
    public final String name;
    public final String appFQCN;

    protected GenericCommand(String name, String appFQCN) {
        this.name = name;
        this.appFQCN = appFQCN;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(T arg) {
        if (appFQCN.isEmpty()) {
            execute();
        }

        try {
            Class<?> cls = Class.forName(appFQCN);
            Field applicationField = cls.getDeclaredField("APPLICATION");
            applicationField.setAccessible(true);
            ((GenericApplication<T>) applicationField.get(null)).execute(arg);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
