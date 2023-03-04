/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.controller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("NullableProblems")
public interface ControllerWarp extends Controller {
    ExecutorService getImpl();

    @Override
    default void shutdown() {
        getImpl().shutdown();
    }

    @Override
    default List<Runnable> shutdownNow() {
        return getImpl().shutdownNow();
    }

    @Override
    default boolean isShutdown() {
        return getImpl().isShutdown();
    }

    @Override
    default boolean isTerminated() {
        return getImpl().isTerminated();
    }

    @Override
    default boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return getImpl().awaitTermination(timeout, unit);
    }

    @Override
    default <T> Future<T> submit(Callable<T> task) {
        return getImpl().submit(task);
    }

    @Override
    default <T> Future<T> submit(Runnable task, T result) {
        return getImpl().submit(task, result);
    }

    @Override
    default Future<?> submit(Runnable task) {
        return getImpl().submit(task);
    }

    @Override
    default <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return getImpl().invokeAll(tasks);
    }

    @Override
    default <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return getImpl().invokeAll(tasks, timeout, unit);
    }

    @Override
    default <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return getImpl().invokeAny(tasks);
    }

    @Override
    default <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return getImpl().invokeAny(tasks, timeout, unit);
    }

    @Override
    default void execute(Runnable command) {
        getImpl().execute(command);
    }
}
