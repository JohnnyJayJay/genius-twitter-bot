package com.github.johnnyjayjay.bobobot.genius;

import com.google.gson.JsonObject;

import java.util.Objects;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Song {

    private final Artist artist;
    private final String title, geniusURL;
    private final int id;

    private Song(Artist artist, String title, String geniusURL, int id) {
        this.artist = artist;
        this.title = title;
        this.geniusURL = geniusURL;
        this.id = id;
    }

    public static Song of(Artist artist, String title, String geniusURL, int id) {
        return new Song(artist, title, geniusURL, id);
    }

    public static Song fromJsonObject(JsonObject jsonObject) {
        return of(Artist.fromJsonObject(jsonObject.getAsJsonObject("primary_artist")), jsonObject.get("title").getAsString(),
                jsonObject.get("url").getAsString(), jsonObject.get("id").getAsInt());
    }

    public Artist artist() {
        return artist;
    }

    public int id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String geniusURL() {
        return geniusURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(title, song.title) &&
                Objects.equals(geniusURL, song.geniusURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, title, geniusURL, id);
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist=" + artist +
                ", title='" + title + '\'' +
                ", geniusURL='" + geniusURL + '\'' +
                ", id=" + id +
                '}';
    }
}
