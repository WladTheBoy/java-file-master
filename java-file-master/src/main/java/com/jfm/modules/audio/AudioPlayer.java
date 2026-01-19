package com.jfm.modules.audio;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AudioPlayer extends JFrame {

    // ===== WAV =====
    private Clip clip;
    private boolean paused = false;

    // ===== MP3 =====
    private MediaPlayer fxPlayer;
    private boolean isMP3 = false;

    private JSlider progress;
    private Timer timer;

    public AudioPlayer(File audioFile) {
        setTitle("Audio Player - " + audioFile.getName());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        new JFXPanel(); // init JavaFX

        initUI(audioFile);
        setVisible(true);
    }

    private void initUI(File file) {
        JButton playBtn = new JButton("Play");
        JButton pauseBtn = new JButton("Pause");
        JButton stopBtn = new JButton("Stop");

        progress = new JSlider();

        JPanel controls = new JPanel();
        controls.add(playBtn);
        controls.add(pauseBtn);
        controls.add(stopBtn);

        add(progress, BorderLayout.NORTH);
        add(controls, BorderLayout.CENTER);

        loadAudio(file);

        playBtn.addActionListener(e -> play());
        pauseBtn.addActionListener(e -> pause());
        stopBtn.addActionListener(e -> stop());

        timer = new Timer(200, e -> updateProgress());
        timer.start();

        // === SEEK per Slider (MP3 + WAV) ===
        progress.addChangeListener(e -> {
            if (!progress.getValueIsAdjusting()) return;

            if (isMP3 && fxPlayer != null) {
                Platform.runLater(() ->
                        fxPlayer.seek(
                                javafx.util.Duration.millis(progress.getValue())
                        )
                );
            } else if (clip != null) {
                clip.setMicrosecondPosition(
                        progress.getValue() * 1000L
                );
            }
        });
    }

    // ================= LOAD =================
    private void loadAudio(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".mp3")) {
            isMP3 = true;
            loadMP3(file);
        } else {
            loadWAV(file);
        }
    }

    private void loadMP3(File file) {
        Platform.runLater(() -> {
            Media media = new Media(file.toURI().toString());
            fxPlayer = new MediaPlayer(media);

            fxPlayer.setOnReady(() -> {
                progress.setMaximum(
                        (int) fxPlayer.getTotalDuration().toMillis()
                );
            });
        });
    }

    private void loadWAV(File file) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(ais);

            progress.setMaximum(
                    (int) (clip.getMicrosecondLength() / 1000)
            );
        } catch (Exception e) {
            showError();
        }
    }

    // ================= PLAY =================
    private void play() {
        if (isMP3) {
            Platform.runLater(() -> fxPlayer.play());
            return;
        }

        if (clip != null) {
            clip.start();
        }
    }

    // ================= PAUSE =================
    private void pause() {
        if (isMP3) {
            Platform.runLater(() -> fxPlayer.pause());
            return;
        }

        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // ================= STOP =================
    private void stop() {
        if (isMP3) {
            Platform.runLater(() -> {
                fxPlayer.stop();
                fxPlayer.seek(javafx.util.Duration.ZERO);
            });
            return;
        }

        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
        }
    }

    // ================= PROGRESS =================
    private void updateProgress() {
        if (isMP3 && fxPlayer != null) {
            Platform.runLater(() ->
                    progress.setValue(
                            (int) fxPlayer.getCurrentTime().toMillis()
                    )
            );
        } else if (clip != null && clip.isOpen()) {
            progress.setValue(
                    (int) (clip.getMicrosecondPosition() / 1000)
            );
        }
    }

    private void showError() {
        JOptionPane.showMessageDialog(
                this,
                "Audio konnte nicht geladen werden!",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

