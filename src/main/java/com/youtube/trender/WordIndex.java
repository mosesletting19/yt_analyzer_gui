package com.youtube.trender;

import java.util.ArrayList;
import java.util.List;

public class WordIndex {
    private String word;
    private int count;
    private List<String> videoTitles; // List to store associated video titles

    public WordIndex(String word) {
        this.word = word;
        this.count = 1; // Initialize count to 1
        this.videoTitles = new ArrayList<>(); // Initialize the list of video titles
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    // Method to increment the count
    public void incrementCount() {
        count++;
    }

    // Method to add a video title
    public void addVideo(String title) {
        if (!videoTitles.contains(title)) {
            videoTitles.add(title);
        }
    }

    public List<String> getVideoTitles() {
        return videoTitles; // Returns the list of video titles
    }

    @Override
    public String toString() {
        return word + " [" + count + "]"; // Format for displaying the word with its count
    }
}
