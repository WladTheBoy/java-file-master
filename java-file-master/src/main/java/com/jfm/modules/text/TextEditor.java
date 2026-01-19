package com.jfm.modules.text;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.jfm.util.SimpleDocumentListener;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TextEditor extends JFrame {

    private RSyntaxTextArea textArea;
    private File currentFile;
    private boolean modified = false;

    public TextEditor(File file) {
        this.currentFile = file;

        setTitle("Text Editor - " + file.getName());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadFile(file);

        setVisible(true);
    }

    // ================= UI =================
    private void initUI() {
        textArea = new RSyntaxTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setTabSize(4);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);

        detectSyntax(currentFile);

        textArea.getDocument().addDocumentListener(
                new SimpleDocumentListener(() -> {
                    modified = true;
                    updateTitle();
                })
        );

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setFoldIndicatorEnabled(true);

        add(scrollPane, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
    }

    // ================= MENU =================
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As");

        save.addActionListener(e -> save());
        saveAs.addActionListener(e -> saveAs());

        fileMenu.add(save);
        fileMenu.add(saveAs);
        bar.add(fileMenu);

        return bar;
    }

    // ================= SYNTAX =================
    private void detectSyntax(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".java")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        } else if (name.endsWith(".xml")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        } else if (name.endsWith(".html")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        } else if (name.endsWith(".json")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        } else if (name.endsWith(".css")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
        } else {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }
    }

    // ================= LOAD =================
    private void loadFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            textArea.read(reader, null);
            modified = false;
            updateTitle();
        } catch (IOException e) {
            showError("Datei konnte nicht geladen werden.");
        }
    }

    // ================= SAVE =================
    private void save() {
        if (currentFile == null) {
            saveAs();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(writer);
            modified = false;
            updateTitle();
        } catch (IOException e) {
            showError("Datei konnte nicht gespeichert werden.");
        }
    }

    private void saveAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(currentFile);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            detectSyntax(currentFile);
            save();
        }
    }

    // ================= HELPERS =================
    private void updateTitle() {
        setTitle("Text Editor - " + currentFile.getName() + (modified ? "*" : ""));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
