package com.github.johnnyjayjay.bobobot.util;

import com.github.johnnyjayjay.bobobot.IndexedCollection;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Johnny_JayJay (https://www.github.com/JohnnyJayJay)
 */
public class RandomPick {

    public static <T> T randomElement(IndexedCollection<T> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("List must not be empty");

        return list.get(randomInt(list.size()));
    }

    public static String randomCoherentLines(String text, int maxLines, int maxLength) {
        String[] paragraphs = text.split("\n\n");
        String paragraph = paragraphs[randomInt(paragraphs.length)];

        String[] lines = paragraph.split("\n");

        int startIndex = lines.length <= maxLines ? 0 : randomInt(lines.length - maxLines);
        StringBuilder builder = new StringBuilder();
        int lineCount = 0;
        for (int i = startIndex; i < lines.length; i++) {
            String line = lines[i];
            if (lineCount++ >= maxLines || builder.length() + line.length() + 1 > maxLength)
                break;

            builder.append(line).append("\n");
        }

        return builder.toString();
    }

    private static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
