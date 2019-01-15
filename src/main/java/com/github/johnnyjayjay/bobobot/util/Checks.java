package com.github.johnnyjayjay.bobobot.util;

import java.util.function.Supplier;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Checks {

    private Checks() {}

    public static void checkNotNull(Object o, String name) {
        if (o == null)
            throw new IllegalArgumentException(name + " must not be null");
    }

    public static void check(boolean expression, Supplier<? extends RuntimeException> exceptionFactory) {
        if (!expression)
            throw exceptionFactory.get();
    }

    public static void check(boolean expression, String message) {
        check(expression, () -> new IllegalArgumentException(message));
    }



}
