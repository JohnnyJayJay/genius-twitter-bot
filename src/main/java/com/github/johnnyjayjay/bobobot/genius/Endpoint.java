package com.github.johnnyjayjay.bobobot.genius;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public enum Endpoint {

    SEARCH("search"),
    SONG("songs/%d"),
    ARTIST("artists/%d"),
    ARTIST_SONGS("artists/%d/songs");

    public static final String BASE_URL = "https://api.genius.com/";

    private final String endpoint;

    Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String toURL(Object... parameters) {
        return BASE_URL + String.format(endpoint, parameters);
    }

}
