package com.github.johnnyjayjay.bobobot.twitter;

import com.github.johnnyjayjay.bobobot.genius.LyricsParser;
import com.github.johnnyjayjay.bobobot.genius.Song;
import com.github.johnnyjayjay.bobobot.util.Checks;
import com.github.johnnyjayjay.bobobot.util.RandomPick;
import com.github.johnnyjayjay.bobobot.SongStash;
import com.github.johnnyjayjay.bobobot.genius.GeniusAPI;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class PostSchedule {

    private final TwitterStatusUpdater statusUpdater;
    private final GeniusAPI geniusAPI;
    private final SongStash songStash;
    private final long periodInSeconds;
    private final boolean postSource;
    private final int maxLines;

    private final ScheduledExecutorService scheduler;

    private ScheduledFuture<?> task;

    private PostSchedule(TwitterStatusUpdater statusUpdater, GeniusAPI geniusAPI, SongStash songStash, long periodInSeconds, boolean postSource, int maxLines) {
        this.statusUpdater = statusUpdater;
        this.geniusAPI = geniusAPI;
        this.songStash = songStash;
        this.periodInSeconds = periodInSeconds;
        this.postSource = postSource;
        this.maxLines = maxLines;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.task = scheduler.schedule(() -> {}, 1, TimeUnit.MILLISECONDS);
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
            content = postSource ? content + "\n- " + song.artist().name() + ", \"" + song.title() + "\"" : content;
            System.out.printf("Posting tweet: %n%s%n", content);
            statusUpdater.sendTweet(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Builder {

        private TwitterStatusUpdater statusUpdater;
        private GeniusAPI geniusAPI;
        private SongStash songStash;
        private int maxLines;
        private int postsPerDay;
        private boolean postSource;

        public Builder() {
            maxLines = 4;
            postsPerDay = 2;
            postSource = true;
        }

        public Builder setStatusUpdater(TwitterStatusUpdater statusUpdater) {
            this.statusUpdater = statusUpdater;
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

        public PostSchedule build() {
            Checks.checkNotNull(statusUpdater, "Status updater");
            Checks.checkNotNull(geniusAPI, "Genius API");
            Checks.checkNotNull(songStash, "Song stash");
            Checks.check(postsPerDay > 0, "Posts per day must be more than 0");
            Checks.check(maxLines > 0, "Max lines must be bigger than 0");

            long periodInSeconds = (24 * 60* 60) / postsPerDay;
            return new PostSchedule(statusUpdater, geniusAPI, songStash, periodInSeconds, postSource, maxLines);
        }
    }
}
