package com.github.johnnyjayjay.bobobot.genius;

import com.google.gson.JsonObject;

import java.util.Objects;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Artist {

    private final String name;
    private final int id;

    private Artist(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Artist of(String name, int id) {
        return new Artist(name, id);
    }

    public static Artist fromJsonObject(JsonObject jsonObject) {
        return of(jsonObject.get("name").getAsString(), jsonObject.get("id").getAsInt());
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id &&
                Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
