package com.youtube.trender;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

public class YouTubeDataParser {

    private final Gson gson = new Gson();

    public List<YouTubeVideo> parse(String filePath) throws YouTubeDataParserException {
        List<YouTubeVideo> videos = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            // Parse the JSON file
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonElement videosElement = jsonObject.get("videos");

            if (videosElement.isJsonArray()) {
                for (JsonElement element : videosElement.getAsJsonArray()) {
                    // Convert each JSON element to a YouTubeVideo object
                    YouTubeVideo video = gson.fromJson(element, YouTubeVideo.class);
                    videos.add(video);
                }
            } else {
                throw new YouTubeDataParserException("Expected a JSON array for videos.");
            }
        } catch (Exception e) {
            throw new YouTubeDataParserException("Failed to parse YouTube data: " + e.getMessage(), e);
        }
        return videos;
    }

    public Map<String, WordIndex> indexWords(List<YouTubeVideo> videos) {
        Map<String, WordIndex> wordIndexMap = new HashMap<>();

        for (YouTubeVideo video : videos) {
            // Get words from the video's title and description
            List<String> words = extractWords(video.getTitle(), video.getDescription());

            for (String word : words) {
                // Check if the word is already in the map
                WordIndex wordIndex = wordIndexMap.get(word.toLowerCase(Locale.ROOT));
                if (wordIndex != null) {
                    // Word already exists, increment its count and add the video title
                    wordIndex.incrementCount();
                    wordIndex.addVideo(video.getTitle());
                } else {
                    // Word does not exist, create a new WordIndex
                    wordIndex = new WordIndex(word);
                    wordIndex.addVideo(video.getTitle());
                    wordIndexMap.put(word.toLowerCase(Locale.ROOT), wordIndex);
                }
            }
        }

        return wordIndexMap;
    }

    // Method to extract words from title and description
    private List<String> extractWords(String title, String description) {
        // Combine title and description
        String combinedText = title + " " + description;

        // Split the combined text into words, remove punctuation, and convert to lowercase
        return Arrays.stream(combinedText.split("\\W+"))
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }
}
