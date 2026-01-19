package com.jfm.core;

import javax.swing.*;

import com.jfm.modules.audio.AudioPlayerModule;
import com.jfm.modules.paint.MiniPaintModule;
import com.jfm.modules.text.TextEditorModule;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JFMMain extends JFrame {

    private List<FileModule> modules = new ArrayList<>();

    public JFMMain() {
        setTitle("Java File Master");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton openBtn = new JButton("Datei öffnen");
        openBtn.setFont(new Font("Arial", Font.BOLD, 18));

        openBtn.addActionListener(e -> openFile());

        add(openBtn, BorderLayout.CENTER);

        // Module registrieren
        modules.add(new MiniPaintModule());
        modules.add(new AudioPlayerModule());
        modules.add(new TextEditorModule());

        setVisible(true);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            for (FileModule m : modules) {
                if (m.supports(file)) {
                    m.open(file);
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Kein Modul für diesen Dateityp!",
                    "Nicht unterstützt",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

//    Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JFMMain::new);
    }
}

