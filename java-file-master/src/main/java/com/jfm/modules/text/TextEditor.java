package com.jfm.modules.text;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jfm.util.SimpleDocumentListener;

import java.awt.*;
import java.io.*;

public class TextEditor extends JFrame {

	private JTextArea textArea;
	private File currentFile;
	private boolean modified = false;

	public TextEditor(File file) {
		this.currentFile = file;

		setTitle("Text Editor - " + file.getName());
		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initUI();
		loadFile(file);

		setVisible(true);
	}

	// ================= UI =================
	private void initUI() {
		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		textArea.setTabSize(4);

		textArea.getDocument().addDocumentListener(new SimpleDocumentListener(() -> {
			modified = true;
			updateTitle();
		}));

		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);

		setJMenuBar(createMenuBar());
	}

	// ================= MENU =================
	private JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As...");

		save.addActionListener(e -> save());
		saveAs.addActionListener(e -> saveAs());

		fileMenu.add(save);
		fileMenu.add(saveAs);

		bar.add(fileMenu);
		return bar;
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
			save();
		}
	}

	// ================= HELPERS =================
	private void updateTitle() {
		String star = modified ? "*" : "";
		setTitle("Text Editor - " + currentFile.getName() + star);
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
