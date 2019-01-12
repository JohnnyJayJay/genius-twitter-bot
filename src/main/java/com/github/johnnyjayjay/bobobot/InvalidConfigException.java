package com.github.johnnyjayjay.bobobot;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class InvalidConfigException extends RuntimeException {

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException() {
        this("Invalid config");
    }
}
