/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.misc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.function.Supplier;

@FunctionalInterface
public interface LazyInitializer<T> extends Supplier<T> {
    T getInitializedInstance();

    @Override
    default T get() {
        if (!InitializerHolder.has(this)) {
            synchronized (this) {
                if (!InitializerHolder.has(this)) {
                    InitializerHolder.set(this, getInitializedInstance());
                }
            }
        }
        return InitializerHolder.get(this);
    }

    final class InitializerHolder {
        private static final Cache<LazyInitializer<?>, Object> CACHE = CacheBuilder.newBuilder()
                .weakKeys()
                .weakValues()
                .build();

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        private static boolean has(LazyInitializer<?> lazyInitializer) {
            return CACHE.getIfPresent(lazyInitializer) != null;
        }

        private static <T> void set(LazyInitializer<T> key, T value) {
            CACHE.put(key, value);
        }

        private static <T> T get(LazyInitializer<T> key) {
            //noinspection unchecked
            return (T) CACHE.getIfPresent(key);
        }
    }
}
