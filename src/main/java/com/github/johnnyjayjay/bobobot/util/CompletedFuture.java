package com.github.johnnyjayjay.bobobot.util;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class CompletedFuture<T> implements ScheduledFuture<T> {

    public static final CompletedFuture<?> INSTANCE = create();

    public static <T> CompletedFuture<T> create() {
        return new CompletedFuture<>();
    }

    private CompletedFuture() {}

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
