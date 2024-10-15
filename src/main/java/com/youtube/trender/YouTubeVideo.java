package com.youtube.trender;

public class YouTubeVideo {
    private String channel;
    private String datePosted;
    private String title;
    private String description;
    private int viewCount;

    // Constructor
    public YouTubeVideo(String channel, String datePosted, String title, String description, int viewCount) {
        this.channel = channel;
        this.datePosted = datePosted;
        this.title = title;
        this.description = description;
        this.viewCount = viewCount;
    }

    // Getters
    public String getChannel() {
        return channel;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getViewCount() {
        return viewCount;
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "channel='" + channel + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", viewCount=" + viewCount +
                '}';
    }
}
