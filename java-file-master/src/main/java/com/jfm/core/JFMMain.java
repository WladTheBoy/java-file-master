package com.jfm.core;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jfm.modules.audio.AudioPlayerModule;
import com.jfm.modules.paint.MiniPaintModule;
import com.jfm.modules.text.TextEditorModule;
import com.jfm.ui.FolderIconFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JFMMain extends JFrame {

	private List<FileModule> modules = new ArrayList<>();

	public JFMMain() {
		setTitle("Java File Master");
		setSize(320, 220);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		initUI();

		// Module registrieren
		modules.add(new MiniPaintModule());
		modules.add(new AudioPlayerModule());
		modules.add(new TextEditorModule());

		setVisible(true);
	}

	private void initUI() {
		JPanel root = new JPanel(new GridBagLayout());
		root.setBackground(new Color(30, 30, 30));

		JButton openBtn = new JButton();
		openBtn.setIcon(FolderIconFactory.createFolderIcon(113));
		openBtn.setPreferredSize(new Dimension(96, 96));
		openBtn.setFocusPainted(false);
		openBtn.setBorderPainted(false);
		openBtn.setContentAreaFilled(false);
		openBtn.setToolTipText("Datei öffnen");

		// Hover-Effekt
		openBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				openBtn.setIcon(FolderIconFactory.createFolderIcon(120));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				openBtn.setIcon(FolderIconFactory.createFolderIcon(113));
			}
		});

		openBtn.addActionListener(e -> openFile());

		root.add(openBtn);
		add(root, BorderLayout.CENTER);
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

			JOptionPane.showMessageDialog(this, "Kein Modul für diesen Dateityp!", "Nicht unterstützt",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void main(String[] args) {
		BufferedImage img = FolderIconFactory.createFolderImage(512);
		try {
			ImageIO.write(img, "png", new File("JFM_Logo.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("JFM_Logo.png wurde erstellt!");

		SwingUtilities.invokeLater(JFMMain::new);

	}
}
