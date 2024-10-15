package com.youtube.trender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YouTubeTrenderPhase2 extends JFrame {

    private JTextField filePathField;
    private JList<YouTubeVideo> videoList;
    private JTextField channelField;
    private JTextField dateField;
    private JTextField titleField;
    private JTextField viewCountField;
    private JTextArea descriptionArea;
    private List<YouTubeVideo> videos;

    public YouTubeTrenderPhase2() {
        setTitle("YouTube Trender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        // Input field, load button, and sorting options
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1)); // Adjusting layout for normal size

        JPanel fileInputPanel = new JPanel();
        fileInputPanel.setLayout(new BorderLayout());
        filePathField = new JTextField("src/resources/youtubedata_15_50.json");
        JButton loadButton = new JButton("Load");

        fileInputPanel.add(filePathField, BorderLayout.CENTER);
        fileInputPanel.add(loadButton, BorderLayout.EAST);
        inputPanel.add(fileInputPanel);

        // Sort Criteria
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout());

        JRadioButton sortByChannel = new JRadioButton("Channel");
        JRadioButton sortByDate = new JRadioButton("Date");
        JRadioButton sortByViews = new JRadioButton("Views");
        JRadioButton sortByDescriptionLength = new JRadioButton("Description Length");

        ButtonGroup sortGroup = new ButtonGroup();
        sortGroup.add(sortByChannel);
        sortGroup.add(sortByDate);
        sortGroup.add(sortByViews);
        sortGroup.add(sortByDescriptionLength);

        sortPanel.add(sortByChannel);
        sortPanel.add(sortByDate);
        sortPanel.add(sortByViews);
        sortPanel.add(sortByDescriptionLength);
        inputPanel.add(sortPanel);

        add(inputPanel, BorderLayout.NORTH);

        // List of videos
        videoList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(videoList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.WEST);

        // Video details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(6, 2));

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

        // Action listeners for buttons
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVideos();
            }
        });

        videoList.addListSelectionListener(e -> {
            YouTubeVideo selectedVideo = videoList.getSelectedValue();
            if (selectedVideo != null) {
                channelField.setText(selectedVideo.getChannel());
                dateField.setText(selectedVideo.getDatePosted());
                titleField.setText(selectedVideo.getTitle());
                viewCountField.setText(String.valueOf(selectedVideo.getViewCount()));
                descriptionArea.setText(selectedVideo.getDescription());
            }
        });

        // Sorting logic
        ActionListener sortActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videos != null) {
                    if (sortByChannel.isSelected()) {
                        Collections.sort(videos, Comparator.comparing(YouTubeVideo::getChannel));
                    } else if (sortByDate.isSelected()) {
                        Collections.sort(videos, Comparator.comparing(YouTubeVideo::getDatePosted));
                    } else if (sortByViews.isSelected()) {
                        Collections.sort(videos, Comparator.comparingInt(YouTubeVideo::getViewCount));
                    } else if (sortByDescriptionLength.isSelected()) {
                        Collections.sort(videos, Comparator.comparingInt(video -> video.getDescription().length()));
                    }
                    videoList.setListData(videos.toArray(new YouTubeVideo[0]));
                }
            }
        };

        sortByChannel.addActionListener(sortActionListener);
        sortByDate.addActionListener(sortActionListener);
        sortByViews.addActionListener(sortActionListener);
        sortByDescriptionLength.addActionListener(sortActionListener);
    }

    private void loadVideos() {
        try {
            String filePath = filePathField.getText();
            YouTubeDataParser parser = new YouTubeDataParser();
            videos = parser.parse(filePath);
            videoList.setListData(videos.toArray(new YouTubeVideo[0]));
        } catch (YouTubeDataParserException e) {
            JOptionPane.showMessageDialog(this, "Error loading videos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new YouTubeTrenderPhase2().setVisible(true);
        });
    }
}
