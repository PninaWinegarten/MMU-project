package com.hit.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class CacheUnitView{

    private final JFrame frame;
    private final CacheUnitPanel panel;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    JTextPane textPanel = new JTextPane();

    public CacheUnitView() {
        frame = new JFrame();
        panel = new CacheUnitPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public void start() {
        panel.run();
    }

    public <T> void updateUIData(T status){
        // Succeeded
        if (status.toString().equals("true") || status.toString().charAt(0) == '['){
            textPanel.setForeground(Color.BLACK);
            textPanel.setFont(new Font("Segoe UI Semilight", Font.TRUETYPE_FONT, 30));
            textPanel.setText("\n    Succeeded!! ");
        }
        // Failed
        else if (status.toString().equals("false") || status.toString().equals("Empty")){
            textPanel.setForeground(Color.BLACK);
            textPanel.setFont(new Font("Segoe UI Semilight", Font.TRUETYPE_FONT, 30));
            textPanel.setText("\n    Failed!! \n");
        }
        // Statistics
        else {
            textPanel.setFont(new Font("Segoe UI Semilight", Font.TRUETYPE_FONT, 20));
            textPanel.setForeground(Color.BLACK);
            String[] statistics = ((String) status).split("-");
            textPanel.setText(" Capacity: " + statistics[0]
                    + "\n Algorithm: " + statistics[1]
                    + "\n Total Numbers Of Requests: " + statistics[2]
                    + "\n Total Number Of DataModels(GET/DELETE/UPDATE Requests): " + statistics[3]
                    + "\n Total Number Of DataModel Swaps(From Cache To Disk): " + statistics[4]);
        }

        textPanel.validate();
        panel.revalidate();
        panel.repaint();
    }

    public class CacheUnitPanel extends JPanel implements ActionListener{
        private static final long serialVersionUID = 1L;

        JLabel label;
        JLabel statisticsLabel;
        JLabel requestLabel;
        JButton statisticsButton;
        JButton requestButton;

        @Override
        public void actionPerformed(ActionEvent arg0) {}

        public <T> void updateUIData(T t)
        {
            if (t.toString().equals("true"))
            {
                textPanel.setText("Succeeded!! ");
                textPanel.setSelectedTextColor(Color.BLACK);
            }
            else if (t.toString().equals("false"))
            {
                textPanel.setText("Failed!! ");
                textPanel.setSelectedTextColor(Color.BLACK);
            }
            else textPanel.setText(t.toString());
            textPanel.invalidate();
        }


        public void run() {
            // Frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("MMU Project");
            frame.setBounds(750, 750, 750, 750);
            frame.setContentPane(new JLabel(new ImageIcon("img\\mmu.JPG")));

            panel.setLayout(null);
            Border border = BorderFactory.createLineBorder(Color.RED);
            textPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(20, 20, 20, 20)));
            textPanel.setBounds(80, 480, 600, 180);
            textPanel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 30));
            frame.add(textPanel);

            // Label
            label = new JLabel("MMU");
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Segoe UI Semilight", Font.BOLD, 80));
            label.setBounds(265, 40, 400, 80);
            frame.add(label);

            // Show statistics
            statisticsLabel = new JLabel("Press to see the cache statistics:");
            statisticsLabel.setForeground(Color.BLACK);
            statisticsLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 25));
            statisticsLabel.setBounds(80, 120, 500, 80);
            frame.add(statisticsLabel);
            statisticsButton = new JButton("Show Statistics");
            statisticsButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 18));
            statisticsButton.setBackground(Color.RED);
            statisticsButton.setFocusPainted(false);
            statisticsButton.setBounds(80, 200, 300, 80);
            statisticsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PropertyChangeEvent change;
                    change = new PropertyChangeEvent(CacheUnitView.this, "stats", null, "showStatistics");
                    propertyChangeSupport.firePropertyChange(change);
                }
            });
            frame.add(statisticsButton);

            // Load request
            requestLabel = new JLabel("Press to load a cache request:");
            requestLabel.setForeground(Color.BLACK);
            requestLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 25));
            requestLabel.setBounds(80, 270, 500, 80);
            frame.add(requestLabel);
            requestButton = new JButton("Load A Request");
            requestButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 18));
            requestButton.setFocusPainted(false);
            requestButton.setBackground(Color.RED);
            requestButton.setBounds(80, 350, 300, 80);
            requestButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                    int result = fileChooser.showOpenDialog(new JFrame());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (selectedFile != null) {
                            try {
                                PropertyChangeEvent change;
                                change = new PropertyChangeEvent(CacheUnitView.this,"load",null,readFileContent(selectedFile.getPath()));
                                propertyChangeSupport.firePropertyChange(change);

                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

            });
            frame.add(requestButton);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public String readFileContent(String filePath) throws IOException
        {
            File file = new File(filePath);
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(file));

            StringBuilder output = new StringBuilder();
            String currentLine;

            currentLine = reader.readLine();
            while (currentLine != null) {
                output.append(currentLine);
                currentLine = reader.readLine();
            }

            reader.close();
            return output.toString();
        }
    }
}
