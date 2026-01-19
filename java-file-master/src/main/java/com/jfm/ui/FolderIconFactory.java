package com.jfm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FolderIconFactory {

	public static ImageIcon createFolderIcon(int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Colors
		Color main = new Color(255, 193, 7);
		Color dark = new Color(230, 170, 0);
		Color outline = new Color(120, 90, 0);

		// Folder-Top
		g.setColor(dark);
		g.fillRoundRect(size / 8, size / 4, size / 2, size / 4, 10, 10);

		// Folder-Body
		g.setColor(main);
		g.fillRoundRect(size / 8, size / 3, size * 3 / 4, size / 2, 14, 14);

		// Outline
		g.setColor(outline);
		g.setStroke(new BasicStroke(2.5f));
		g.drawRoundRect(size / 8, size / 3, size * 3 / 4, size / 2, 14, 14);

		// JFM Text
		String text = "JFM";
		Font font = new Font("Arial", Font.BOLD, size / 4);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getAscent();

		int x = (size - textWidth) / 2;
		int y = size / 2 + textHeight / 2;

		// Text-Shadow (subtle)
		g.setColor(new Color(0, 0, 0, 80));
		g.drawString(text, x + 2, y + 2);

		// Text
		g.setColor(Color.DARK_GRAY);
		g.drawString(text, x, y);

		g.dispose();
		return new ImageIcon(img);
	}

	public static BufferedImage createFolderImage(int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Color main = new Color(255, 193, 7);
		Color dark = new Color(230, 170, 0);
		Color outline = new Color(120, 90, 0);

		// Folder
		g.setColor(dark);
		g.fillRoundRect(size / 8, size / 4, size / 2, size / 4, 10, 10);

		g.setColor(main);
		g.fillRoundRect(size / 8, size / 3, size * 3 / 4, size / 2, 14, 14);

		g.setColor(outline);
		g.setStroke(new BasicStroke(2.5f));
		g.drawRoundRect(size / 8, size / 3, size * 3 / 4, size / 2, 14, 14);

		// Text
		String text = "JFM";
		Font font = new Font("Arial", Font.BOLD, size / 4);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();
		int x = (size - fm.stringWidth(text)) / 2;
		int y = size / 2 + fm.getAscent() / 2;

		g.setColor(new Color(0, 0, 0, 80));
		g.drawString(text, x + 2, y + 2);

		g.setColor(Color.DARK_GRAY);
		g.drawString(text, x, y);

		g.dispose();
		return img;
	}

}
