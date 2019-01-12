package com.github.johnnyjayjay.bobobot.genius;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class GeniusAPI {

    private static final String BASE_URL = "https://api.genius.com/";
    private static final String SEARCH = "search";


    private final JsonParser responseParser;
    private final String accessToken;
    private final OkHttpClient client;

    private GeniusAPI(String accessToken, OkHttpClient client) {
        this.accessToken = accessToken;
        this.client = client;
        this.responseParser = new JsonParser();
    }

    public static GeniusAPI create(String accessToken, OkHttpClient client) {
        return new GeniusAPI(accessToken, client);
    }

    public Song getSong(int songId) {
        Song song = null;

        Call call = client.newCall(
                this.createRequest("", Endpoint.SONG, songId)
        );

        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                JsonObject jsonObject = responseParser.parse(body.charStream())
                        .getAsJsonObject()
                        .getAsJsonObject("response")
                        .getAsJsonObject("song");

                song = Song.fromJsonObject(jsonObject);
            }
        } catch (IOException e) {
            logException(e);
        }

        return song;
    }

    public List<Integer> getSongIds(Artist artist, int page) {
        List<Integer> songIds = new ArrayList<>();

        Call call = client.newCall(
                this.createRequest("?per_page=50&sort=popularity&page=" + page, Endpoint.ARTIST_SONGS, artist.id())
        );

        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                JsonObject jsonObject = responseParser.parse(body.charStream()).getAsJsonObject().getAsJsonObject("response");
                JsonArray songs = jsonObject.getAsJsonArray("songs");
                for (JsonElement song : songs) {
                    int id = song.getAsJsonObject().get("id").getAsInt();
                    songIds.add(id);
                }
            }
        } catch (IOException e) {
            logException(e);
        }

        return Collections.unmodifiableList(songIds);
    }

    public Artist findArtistByName(String name) {
        Artist foundArtist = null;

        Call call = client.newCall(
                this.createRequest("?q=" + name.replaceAll("\\s", "%20"), Endpoint.SEARCH)
        );

        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                JsonObject jsonObject = responseParser.parse(body.charStream()).getAsJsonObject().getAsJsonObject("response");
                JsonArray results = jsonObject.getAsJsonArray("hits");
                for (JsonElement result : results) {
                    JsonObject resultObject = result.getAsJsonObject();
                    if (resultObject.get("type").getAsString().equals("song")) {
                        Artist artist = Artist.fromJsonObject(resultObject.getAsJsonObject("result").getAsJsonObject("primary_artist"));
                        if (artist.name().equalsIgnoreCase(name)) {
                            foundArtist = artist;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logException(e);
        }

        return foundArtist;
    }

    private void logException(Throwable throwable) {
        System.err.println("Something went wrong");
        throwable.printStackTrace();
    }

    private Request createRequest(String urlAddition, Endpoint endpoint, Object... parameters) {
        return new Request.Builder()
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .url(endpoint.toURL(parameters) + urlAddition)
                .build();
    }

}
