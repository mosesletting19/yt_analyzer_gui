package com.youtube.trender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class YouTubeTrenderPhase1 extends JFrame {

    private JTextField filePathField;
    private JList<YouTubeVideo> videoList;
    private JTextField channelField;
    private JTextField dateField;
    private JTextField titleField;
    private JTextField viewCountField;
    private JTextArea descriptionArea;

    public YouTubeTrenderPhase1() {
        setTitle("YouTube Trender");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10)); // Set spacing for better visuals

        // Input field and load button (with a regular size input field)
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        filePathField = new JTextField("src/resources/youtubedata_15_50.json", 30);
        JButton loadButton = new JButton("Load");

        inputPanel.add(new JLabel("File Path:"), BorderLayout.WEST);
        inputPanel.add(filePathField, BorderLayout.CENTER);
        inputPanel.add(loadButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.NORTH);

        // List of videos
        videoList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(videoList);
        scrollPane.setPreferredSize(new Dimension(150, 300)); // Regular-sized scrollable area
        add(scrollPane, BorderLayout.WEST);

        // Video details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Channel label and text field
        detailsPanel.add(new JLabel("Channel:"), gbc);
        gbc.gridx = 1;
        channelField = new JTextField(20);
        detailsPanel.add(channelField, gbc);

        // Date posted label and text field
        gbc.gridy++;
        gbc.gridx = 0;
        detailsPanel.add(new JLabel("Date Posted:"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(20);
        detailsPanel.add(dateField, gbc);

        // Title label and text field
        gbc.gridy++;
        gbc.gridx = 0;
        detailsPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        detailsPanel.add(titleField, gbc);

        // View count label and text field
        gbc.gridy++;
        gbc.gridx = 0;
        detailsPanel.add(new JLabel("View Count:"), gbc);
        gbc.gridx = 1;
        viewCountField = new JTextField(20);
        detailsPanel.add(viewCountField, gbc);

        // Description label and text area
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Description:"), gbc);

        gbc.gridy++;
        descriptionArea = new JTextArea(4, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        detailsPanel.add(descriptionScrollPane, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // Load button functionality
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVideos();
            }
        });

        // Video list selection functionality
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

        // Improve visual styles for the fields
        customizeComponents();
    }

    // Customizing visual aspects for better design
    private void customizeComponents() {
        Font font = new Font("Arial", Font.PLAIN, 14);
        filePathField.setFont(font);
        channelField.setFont(font);
        dateField.setFont(font);
        titleField.setFont(font);
        viewCountField.setFont(font);
        descriptionArea.setFont(font);

        // Adding some color to buttons
        UIManager.put("Button.background", Color.decode("#d92552"));
        UIManager.put("Button.foreground", Color.white);
    }

    private void loadVideos() {
        try {
            String filePath = filePathField.getText();
            YouTubeDataParser parser = new YouTubeDataParser();
            List<YouTubeVideo> videos = parser.parse(filePath);
            videoList.setListData(videos.toArray(new YouTubeVideo[0]));
        } catch (YouTubeDataParserException e) {
            JOptionPane.showMessageDialog(this, "Error loading videos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new YouTubeTrenderPhase1().setVisible(true);
        });
    }
}
