package com.github.johnnyjayjay.bobobot;

import com.github.johnnyjayjay.bobobot.genius.Artist;
import com.github.johnnyjayjay.bobobot.genius.GeniusAPI;
import com.github.johnnyjayjay.bobobot.twitter.PostSchedule;
import okhttp3.OkHttpClient;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

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
        TwitterFactory twitterFactory = createTwitterFactory();
        Twitter twitterAPI = twitterFactory.getInstance();

        stash = args.length > 0 && args[0].equals("stashreset")
                ? SongStash.createNew("./songs.genius")
                : SongStash.load("./songs.genius");

        if (stash.isEmpty()) {
            System.out.println("Filling song stash");
            fillStash();
            stash.save();
        }

        PostSchedule schedule = PostSchedule.builder()
                .setMaxLines(config.maxLines())
                .setGeniusAPI(geniusAPI)
                .setPostSource(config.postSource())
                .setPostsPerDay(config.postsPerDay())
                .setSongStash(stash)
                .setTwitterAPI(twitterAPI)
                .build();

        System.out.println("Starting tweet schedule");
        schedule.startSchedule();
    }

    private static TwitterFactory createTwitterFactory() {
        Config.Twitter twitterConfig = config.twitterConfig();
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(twitterConfig.apiKey())
                .setOAuthConsumerSecret(twitterConfig.apiKeySecret())
                .setOAuthAccessToken(twitterConfig.accessToken())
                .setOAuthAccessTokenSecret(twitterConfig.accessTokenSecret());
        return new TwitterFactory(builder.build());
    }

    private static void fillStash() {
        Artist artist = geniusAPI.findArtistByName(config.geniusConfig().artistName());
        int pages = config.maxSongStash() / 50;
        for (int page = 1; page <= pages; page++)
            geniusAPI.getSongIds(artist, page).forEach(stash::addSong);
    }



}
