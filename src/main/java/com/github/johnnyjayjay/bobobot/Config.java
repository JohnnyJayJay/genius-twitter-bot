package com.github.johnnyjayjay.bobobot;

import com.github.johnnyjayjay.bobobot.util.Checks;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Config {

    private final JsonObject configObject;

    private final Twitter twitterConfig;
    private final Genius geniusConfig;

    private Config(JsonObject configObject) {
        this.configObject = configObject;
        this.twitterConfig = new Twitter(configObject.getAsJsonObject("twitter"));
        this.geniusConfig = new Genius(configObject.getAsJsonObject("genius"));
    }

    public static Config load(String path) throws IOException {
        JsonParser parser = new JsonParser();

        Path file = Paths.get(path);
        if (!Files.exists(file))
            Files.createFile(file);

        JsonObject configObject = parser.parse(Files.newBufferedReader(file)).getAsJsonObject();
        validate(configObject);
        return new Config(configObject);
    }

    public boolean postSource() {
        return configObject.get("post_source").getAsBoolean();
    }

    public int postsPerDay() {
        return configObject.get("posts_per_day").getAsInt();
    }

    public int maxSongStash() {
        return configObject.get("max_stash_size").getAsInt();
    }

    public int maxLines() {
        return configObject.get("max_lines").getAsInt();
    }

    public Twitter twitterConfig() {
        return twitterConfig;
    }

    public Genius geniusConfig() {
        return geniusConfig;
    }

    private static void validate(JsonObject configObject) {
        Supplier<InvalidConfigException> supplier = () -> new InvalidConfigException("Invalid config: elements are missing. Please look at the config example.");

        Checks.check(configObject.has("max_stash_size"), supplier);
        Checks.check(configObject.has("posts_per_day"), supplier);
        Checks.check(configObject.has("max_lines"), supplier);
        Checks.check(configObject.has("post_source"), supplier);
        Checks.check(configObject.has("twitter"), supplier);
        Checks.check(configObject.has("genius"), supplier);

        JsonObject twitter = configObject.getAsJsonObject("twitter");
        JsonObject genius = configObject.getAsJsonObject("genius");

        Checks.check(genius.has("token"), supplier);
        Checks.check(genius.has("artist"), supplier);
        Checks.check(twitter.has("api_key"), supplier);
        Checks.check(twitter.has("access_token"), supplier);
        Checks.check(twitter.has("api_secret_key"), supplier);
        Checks.check(twitter.has("access_token_secret"), supplier);
    }

    public static class Twitter {
        private final JsonObject configObject;

        private Twitter(JsonObject configObject) {
            this.configObject = configObject;
        }

        public String apiKeySecret() {
            return configObject.get("api_secret_key").getAsString();
        }

        public String apiKey() {
            return configObject.get("api_key").getAsString();
        }

        public String accessToken() {
            return configObject.get("access_token").getAsString();
        }

        public String accessTokenSecret() {
            return configObject.get("access_token_secret").getAsString();
        }
    }

    public static class Genius {
        private final JsonObject configObject;

        private Genius(JsonObject configObject) {
            this.configObject = configObject;
        }

        public String artistName() {
            return configObject.get("artist").getAsString();
        }

        public String token() {
            return configObject.get("token").getAsString();
        }
    }
}
