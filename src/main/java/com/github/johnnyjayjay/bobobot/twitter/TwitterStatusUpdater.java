package com.github.johnnyjayjay.bobobot.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class TwitterStatusUpdater {

    private static final String BASE_URL = "https://api.twitter.com/1.1/";
    private static final String UPDATE_STATUS = "statuses/update.json";

    private final String apiKey, accessToken;
    private final OkHttpClient client;

    private TwitterStatusUpdater(String apiKey, String accessToken, OkHttpClient client) {
        this.apiKey = apiKey;
        this.accessToken = accessToken;
        this.client = client;
    }

    public void sendTweet(String content) {
        Request request = new Request.Builder()
                .url(BASE_URL + UPDATE_STATUS)
                .post(RequestBody.create(MediaType.parse("application/json"), "{\"status\": \"" + content + "\"}"))
                .addHeader("authorization", authorizationHeader())
                .addHeader("content-type", "application/json")
                .build();

        Call call = client.newCall(request);

        try (Response response = call.execute()) {
            System.out.println(response.body().string());
            System.out.printf("Result: %d", response.code());
        } catch (IOException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private String authorizationHeader() {
        return "OAuth oauth_consumer_key=\"" + apiKey
                + "\", oauth_nonce=\"AUTO_GENERATED_NONCE\", oauth_signature=\"AUTO_GENERATED_SIGNATURE\", " +
                "oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"AUTO_GENERATED_TIMESTAMP\", " +
                "oauth_token=\"" + accessToken + "\", oauth_version=\"1.0\"";
    }

    public static TwitterStatusUpdater create(String apiKey, String accessToken, OkHttpClient client) {
        return new TwitterStatusUpdater(apiKey, accessToken, client);
    }

}
