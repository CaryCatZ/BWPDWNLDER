package com.github.zhang32767.bwpdwnlder.io;

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
