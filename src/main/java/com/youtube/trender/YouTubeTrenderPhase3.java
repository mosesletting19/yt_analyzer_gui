package com.youtube.trender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class YouTubeTrenderPhase3 extends JFrame {

    private JTextField filePathField;
    private JList<YouTubeVideo> videoList;
    private JTextField channelField;
    private JTextField dateField;
    private JTextField titleField;
    private JTextField viewCountField;
    private JTextArea descriptionArea;

    private JList<WordIndex> wordList;  // List to display trending words
    private JButton indexButton;        // Button to trigger the indexing
    private JLabel mostUsedWordLabel;   // Label to display the most used word

    public YouTubeTrenderPhase3() {
        setTitle("YouTube Trender - Phase 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Input field and load button
        JPanel inputPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField("src/resources/youtubedata_15_50.json");
        JButton loadButton = new JButton("Load");

        inputPanel.add(filePathField, BorderLayout.CENTER);
        inputPanel.add(loadButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.NORTH);

        // List of videos
        videoList = new JList<>();
        JScrollPane videoScrollPane = new JScrollPane(videoList);
        videoScrollPane.setPreferredSize(new Dimension(200, 300));
        add(videoScrollPane, BorderLayout.WEST);

        // Video details panel
        JPanel detailsPanel = new JPanel(new GridLayout(6, 2));
        detailsPanel.add(new JLabel("Channel:"));
        channelField = new JTextField();
        detailsPanel.add(channelField);

        detailsPanel.add(new JLabel("Date Posted:"));
        dateField = new JTextField();
        detailsPanel.add(dateField);

        detailsPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        detailsPanel.add(titleField);

        detailsPanel.add(new JLabel("View Count:"));
        viewCountField = new JTextField();
        detailsPanel.add(viewCountField);

        detailsPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        detailsPanel.add(descriptionScrollPane);

        add(detailsPanel, BorderLayout.CENTER);

        // Trending words section
        JPanel trendingPanel = new JPanel(new BorderLayout());
        indexButton = new JButton("Index Trending Words");
        wordList = new JList<>();
        JScrollPane wordScrollPane = new JScrollPane(wordList);

        // Improve UI for trending words section
        mostUsedWordLabel = new JLabel("Most Used Word: ");
        trendingPanel.add(indexButton, BorderLayout.NORTH);
        trendingPanel.add(wordScrollPane, BorderLayout.CENTER);
        trendingPanel.add(mostUsedWordLabel, BorderLayout.SOUTH);

        add(trendingPanel, BorderLayout.EAST);

        // Action listeners
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVideos();
            }
        });

        videoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                YouTubeVideo selectedVideo = videoList.getSelectedValue();
                if (selectedVideo != null) {
                    channelField.setText(selectedVideo.getChannel());
                    dateField.setText(selectedVideo.getDatePosted());
                    titleField.setText(selectedVideo.getTitle());
                    viewCountField.setText(String.valueOf(selectedVideo.getViewCount()));
                    descriptionArea.setText(selectedVideo.getDescription());
                }
            }
        });

        indexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexTrendingWords();
            }
        });
    }

    private void loadVideos() {
        try {
            String filePath = filePathField.getText();
            YouTubeDataParser parser = new YouTubeDataParser();
            List<YouTubeVideo> videos = parser.parse(filePath);
            videoList.setListData(videos.toArray(new YouTubeVideo[0]));
        } catch (YouTubeDataParserException e) {
            JOptionPane.showMessageDialog(this, "Error loading videos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void indexTrendingWords() {
        try {
            String filePath = filePathField.getText();
            YouTubeDataParser parser = new YouTubeDataParser();
            List<YouTubeVideo> videos = parser.parse(filePath);
            Map<String, WordIndex> wordIndexMap = parser.indexWords(videos);

            // Find the most used word
            WordIndex mostUsedWord = wordIndexMap.values().stream()
                    .max((w1, w2) -> Integer.compare(w1.getCount(), w2.getCount()))
                    .orElse(null);

            if (mostUsedWord != null) {
                mostUsedWordLabel.setText("Most Used Word: " + mostUsedWord.getWord() + " (" + mostUsedWord.getCount() + ")");
            } else {
                mostUsedWordLabel.setText("Most Used Word: None");
            }

            // Display the words and their counts in the wordList
            wordList.setListData(wordIndexMap.values().toArray(new WordIndex[0]));

        } catch (YouTubeDataParserException e) {
            JOptionPane.showMessageDialog(this, "Error indexing words: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new YouTubeTrenderPhase3().setVisible(true);
        });
    }
}
