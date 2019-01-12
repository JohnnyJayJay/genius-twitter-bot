package com.github.johnnyjayjay.bobobot.genius;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class LyricsParser {

    private LyricsParser() {
    }

    public static String parseLyrics(Song song) {
        URL url = geniusURLOf(song);
        String lyrics = "";

        try (InputStream stream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            lyrics = findLyrics(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return formatLyrics(lyrics);
    }

    private static String formatLyrics(String lyrics) {
        return lyrics.replaceAll("(<!--/?sse-->)|(</?p>)|(\\[[^\\[\\]]+])", "")
                .replaceAll("(<br>){2}(<br>)+", "<br><br>")
                .replaceAll("<br>", "\n").trim();
    }

    private static String findLyrics(BufferedReader reader) throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("<div class=\"lyrics\">")) {
                StringBuilder lyrics = new StringBuilder();
                while (!(line = reader.readLine().trim()).equals("</div>"))
                    lyrics.append(line);

                return lyrics.toString();
            }
        }

        return "";
    }

    private static URL geniusURLOf(Song song) {
        URL url = null;
        try {
            url = new URL(song.geniusURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
