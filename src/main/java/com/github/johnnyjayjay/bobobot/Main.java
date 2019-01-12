package com.github.johnnyjayjay.bobobot;

import com.github.johnnyjayjay.bobobot.genius.Artist;
import com.github.johnnyjayjay.bobobot.genius.GeniusAPI;
import com.github.johnnyjayjay.bobobot.twitter.PostSchedule;
import com.github.johnnyjayjay.bobobot.twitter.TwitterStatusUpdater;
import okhttp3.OkHttpClient;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class Main {

    private static Config config;
    private static GeniusAPI geniusAPI;
    private static SongStash stash;

    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().build();
        System.out.println("Loading config...");
        config = Config.load("./config.json");
        geniusAPI = GeniusAPI.create(config.geniusConfig().token(), client);
        Config.Twitter twitterConfig = config.twitterConfig();
        TwitterStatusUpdater statusUpdater = TwitterStatusUpdater.create(twitterConfig.apiKey(), twitterConfig.accessToken(), client);
        stash = args.length > 0 && args[0].equals("stashreset")
                ? SongStash.createNew("./songs.genius")
                : SongStash.load("./songs.genius");

        System.out.println("Filling song stash");
        if (stash.isEmpty()) {
            fillStash();
            stash.save();
        }

        PostSchedule schedule = PostSchedule.builder()
                .setMaxLines(config.maxLines())
                .setGeniusAPI(geniusAPI)
                .setPostSource(config.postSource())
                .setPostsPerDay(config.postsPerDay())
                .setSongStash(stash)
                .setStatusUpdater(statusUpdater)
                .build();

        System.out.println("Starting tweet schedule");
        schedule.startSchedule();

        /*int songId = RandomPick.randomElement(stash);
        Song song = geniusAPI.getSong(songId);
        String lyrics = LyricsParser.parseLyrics(song);
        System.out.println("Song: " + song.title());
        System.out.println("Lyrics:");
        System.out.println(lyrics);
        System.out.println("\n\nRandom lines:");
        System.out.println(RandomPick.randomCoherentLines(lyrics, 280));*/
    }

    private static void fillStash() {
        Artist artist = geniusAPI.findArtistByName(config.geniusConfig().artistName());
        int pages = config.maxSongStash() / 50;
        for (int page = 1; page <= pages; page++)
            geniusAPI.getSongIds(artist, page).forEach(stash::addSong);
    }



}
