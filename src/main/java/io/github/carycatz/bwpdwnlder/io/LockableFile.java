package io.github.carycatz.bwpdwnlder.io;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface LockableFile {
    Lock lock = new ReentrantLock();

    default void lock() {
        lock.lock();
    }

    default void unlock() {
        lock.unlock();
    }
}
