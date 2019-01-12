package com.github.johnnyjayjay.bobobot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class SongStash implements IndexedCollection<Integer> {

    private final Path file;
    private final List<Integer> songIds;

    private SongStash(Path file, List<Integer> songIds) {
        this.file = file;
        this.songIds = songIds;
    }

    public boolean isEmpty() {
        return songIds.isEmpty();
    }

    public boolean removeSong(int id) {
        return songIds.remove(Integer.valueOf(id));
    }

    public void addSong(int id) {
        songIds.add(id);
    }

    public void save() throws IOException {
        List<String> fileContent = songIds.stream().map(String::valueOf).collect(Collectors.toList());
        Files.write(file, fileContent, StandardOpenOption.WRITE);
    }

    public static SongStash createNew(String path) throws IOException {
        Path file = Paths.get(path);
        Files.deleteIfExists(file);
        Files.createFile(file);
        return new SongStash(file, new ArrayList<>());
    }

    public static SongStash load(String path) throws IOException {
        Path file = Paths.get(path);
        if (!Files.exists(file))
            Files.createFile(file);

        List<Integer> ids = Files.lines(file).map(Integer::valueOf).collect(Collectors.toCollection(ArrayList::new));

        return new SongStash(file, ids);
    }

    @Override
    public Integer get(int index) {
        return songIds.get(index);
    }

    @Override
    public int size() {
        return songIds.size();
    }
}
