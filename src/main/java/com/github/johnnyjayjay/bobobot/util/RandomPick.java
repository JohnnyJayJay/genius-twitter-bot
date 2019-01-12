package com.github.johnnyjayjay.bobobot.util;

import com.github.johnnyjayjay.bobobot.IndexedCollection;

import java.util.List;
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

        int index = lines.length <= maxLines ? 0 : randomInt(lines.length - maxLines);
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < lines.length && i < maxLines; i++) {
            String line = lines[i];
            if (builder.length() + line.length() + 1 > maxLength)
                break;

            builder.append(line).append("\n");
        }

        return builder.toString();
    }

    private static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

}
