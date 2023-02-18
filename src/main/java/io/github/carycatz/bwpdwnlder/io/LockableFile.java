package io.github.carycatz.bwpdwnlder.io;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface LockableFile {
    Lock _lock = new ReentrantLock();

    default void lock() {
        _lock.lock();
    }

    default void unlock() {
        _lock.unlock();
    }
}
