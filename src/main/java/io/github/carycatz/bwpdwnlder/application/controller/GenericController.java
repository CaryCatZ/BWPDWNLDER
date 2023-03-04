/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.controller;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GenericController implements ControllerWarp {
    protected final ExecutorService executorService;

    protected GenericController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    protected static ThreadFactory defaultThreadFactory() {
        return DefaultThreadFactory.INSTANCE;
    }

    @Override
    public ExecutorService getImpl() {
        return executorService;
    }

    @Override
    public String toString() {
        return super.toString() + '{' +
                "executorService=" + executorService +
                '}';
    }

    protected static final class DefaultThreadFactory implements ThreadFactory {
        private static final ThreadGroup GROUP = new ThreadGroup("Group-Worker");
        private static final AtomicInteger COUNT = new AtomicInteger();

        private static final ThreadFactory INSTANCE = new DefaultThreadFactory();

        @Override
        public Thread newThread(@Nonnull Runnable r) {
            Thread thread = new Thread(GROUP, r, "Worker-%s".formatted(COUNT.getAndIncrement()));
            thread.setDaemon(true);
            return thread;
        }
    }
}
