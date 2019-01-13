package com.github.johnnyjayjay.bobobot.twitter;

import com.github.johnnyjayjay.bobobot.genius.LyricsParser;
import com.github.johnnyjayjay.bobobot.genius.Song;
import com.github.johnnyjayjay.bobobot.util.Checks;
import com.github.johnnyjayjay.bobobot.util.CompletedFuture;
import com.github.johnnyjayjay.bobobot.util.RandomPick;
import com.github.johnnyjayjay.bobobot.SongStash;
import com.github.johnnyjayjay.bobobot.genius.GeniusAPI;
import twitter4j.Twitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class PostSchedule {

    private final Twitter twitterAPI;
    private final GeniusAPI geniusAPI;
    private final SongStash songStash;
    private final long periodInSeconds;
    private final boolean postSource;
    private final boolean allCaps;
    private final int maxLines;

    private final ScheduledExecutorService scheduler;

    private ScheduledFuture<?> task;

    private PostSchedule(Twitter twitterAPI, GeniusAPI geniusAPI, SongStash songStash, long periodInSeconds, boolean postSource, boolean allCaps, int maxLines) {
        this.twitterAPI = twitterAPI;
        this.geniusAPI = geniusAPI;
        this.songStash = songStash;
        this.periodInSeconds = periodInSeconds;
        this.postSource = postSource;
        this.allCaps = allCaps;
        this.maxLines = maxLines;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.task = CompletedFuture.INSTANCE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void startSchedule() {
        this.task = scheduler.scheduleAtFixedRate(this::post, 0, periodInSeconds, TimeUnit.SECONDS);
    }

    public boolean isScheduled() {
        return !task.isDone();
    }

    public void cancel() {
        task.cancel(false);
    }

    private void post() {
        try {
            int songId = RandomPick.randomElement(songStash);
            Song song = geniusAPI.getSong(songId);
            String lyrics = LyricsParser.parseLyrics(song);
            int maxLength = postSource ? 280 - 7 - song.title().length() - song.artist().name().length() : 280;
            String content = RandomPick.randomCoherentLines(lyrics, maxLines, maxLength);

            if (allCaps)
                content = content.toUpperCase();

            if (postSource)
                content = content + "\n- " + song.artist().name() + ", \"" + song.title() + "\"";

            System.out.printf("Posting tweet: %n%s%n", content);
            twitterAPI.tweets().updateStatus(content);
        } catch (Exception e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public static class Builder {

        private Twitter twitterAPI;
        private GeniusAPI geniusAPI;
        private SongStash songStash;
        private int maxLines;
        private int postsPerDay;
        private boolean postSource;
        private boolean allCaps;

        public Builder() {
            maxLines = 4;
            postsPerDay = 2;
            postSource = true;
            allCaps = false;
        }

        public Builder setTwitterAPI(Twitter twitterAPI) {
            this.twitterAPI = twitterAPI;
            return this;
        }

        public Builder setGeniusAPI(GeniusAPI geniusAPI) {
            this.geniusAPI = geniusAPI;
            return this;
        }

        public Builder setSongStash(SongStash songStash) {
            this.songStash = songStash;
            return this;
        }

        public Builder setPostsPerDay(int postsPerDay) {
            this.postsPerDay = postsPerDay;
            return this;
        }

        public Builder setPostSource(boolean postSource) {
            this.postSource = postSource;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public Builder setAllCaps(boolean allCaps) {
            this.allCaps = allCaps;
            return this;
        }

        public PostSchedule build() {
            Checks.checkNotNull(twitterAPI, "Status updater");
            Checks.checkNotNull(geniusAPI, "Genius API");
            Checks.checkNotNull(songStash, "Song stash");
            Checks.check(postsPerDay > 0, "Posts per day must be more than 0");
            Checks.check(maxLines > 0, "Max lines must be bigger than 0");

            long periodInSeconds = (24 * 60* 60) / postsPerDay;
            return new PostSchedule(twitterAPI, geniusAPI, songStash, periodInSeconds, postSource, allCaps, maxLines);
        }
    }
}
