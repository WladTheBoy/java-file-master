package com.jfm.modules.paint;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

public class MiniPaint extends JFrame {

	enum Tool {
		PEN, ERASER, LINE, RECTANGLE, ELLIPSE
	}

	private Tool currentTool = Tool.PEN;
	private Color currentColor = Color.BLACK;
	private int brushSize = 10;

	private DrawPanel drawPanel;

	private Cursor penCursor, eraserCursor, lineCursor, rectCursor, ellipseCursor;

	public MiniPaint() {
		setTitle("MiniPaint");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 750);
		setLocationRelativeTo(null);

		drawPanel = new DrawPanel();

		JPanel centerWrapper = new JPanel(new GridBagLayout());
		centerWrapper.setBackground(new Color(180, 180, 180));
		centerWrapper.add(drawPanel);

		add(centerWrapper, BorderLayout.CENTER);

		setJMenuBar(createMenuBar());
		add(createToolBar(), BorderLayout.NORTH);

//        loadCursors();
		drawPanel.setCursor(penCursor);

		setVisible(true);
	}

	public MiniPaint(File imageFile) {
		this(); // normaler MiniPaint-Start
		openImageFromFile(imageFile);
	}

	// ================= MENU =================

	private JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem newItem = new JMenuItem("New");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveItem = new JMenuItem("Save");

		newItem.addActionListener(e -> {
			int r = JOptionPane.showConfirmDialog(this, "Aktuelles Bild verwerfen?", "New", JOptionPane.YES_NO_OPTION);
			if (r == JOptionPane.YES_OPTION) {
				drawPanel.clear();
				drawPanel.resetHistory();
			}
		});

		openItem.addActionListener(e -> openImage());
		saveItem.addActionListener(e -> saveImage());

		file.add(newItem);
		file.add(openItem);
		file.add(saveItem);

		bar.add(file);
		return bar;
	}

	// ================= TOOLBAR =================

	private JToolBar createToolBar() {
		JToolBar tb = new JToolBar();
		ButtonGroup group = new ButtonGroup();

		JToggleButton pen = new JToggleButton("Pen");
		JToggleButton eraser = new JToggleButton("Eraser");
		JToggleButton line = new JToggleButton("Line");
		JToggleButton rect = new JToggleButton("Rect");
		JToggleButton ellipse = new JToggleButton("Ellipse");

		pen.setSelected(true);

		group.add(pen);
		group.add(eraser);
		group.add(line);
		group.add(rect);
		group.add(ellipse);

		pen.addActionListener(e -> setTool(Tool.PEN));
		eraser.addActionListener(e -> setTool(Tool.ERASER));
		line.addActionListener(e -> setTool(Tool.LINE));
		rect.addActionListener(e -> setTool(Tool.RECTANGLE));
		ellipse.addActionListener(e -> setTool(Tool.ELLIPSE));

		JButton colorBtn = new JButton("Color");
		colorBtn.addActionListener(e -> {
			Color c = JColorChooser.showDialog(this, "Farbe", currentColor);
			if (c != null)
				currentColor = c;
		});

		JSlider sizeSlider = new JSlider(1, 60, brushSize);
		sizeSlider.addChangeListener(e -> brushSize = sizeSlider.getValue());

		JButton undo = new JButton("Undo");
		JButton redo = new JButton("Redo");

		undo.addActionListener(e -> drawPanel.undo());
		redo.addActionListener(e -> drawPanel.redo());

		tb.add(pen);
		tb.add(eraser);
		tb.add(line);
		tb.add(rect);
		tb.add(ellipse);
		tb.addSeparator();
		tb.add(colorBtn);
		tb.add(sizeSlider);
		tb.addSeparator();
		tb.add(undo);
		tb.add(redo);

		return tb;
	}

	private void setTool(Tool tool) {
		currentTool = tool;
		switch (tool) {
		case PEN -> drawPanel.setCursor(penCursor);
		case ERASER -> drawPanel.setCursor(eraserCursor);
		case LINE -> drawPanel.setCursor(lineCursor);
		case RECTANGLE -> drawPanel.setCursor(rectCursor);
		case ELLIPSE -> drawPanel.setCursor(ellipseCursor);
		}
	}

	// ================= FILE IO =================

	private void saveImage() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = chooser.getSelectedFile();
				if (!f.getName().toLowerCase().endsWith(".png")) {
					f = new File(f.getAbsolutePath() + ".png");
				}
				ImageIO.write(drawPanel.getImage(), "png", f);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void openImage() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage img = ImageIO.read(chooser.getSelectedFile());
				if (img != null) {
					drawPanel.loadImage(img);
					drawPanel.resetHistory();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// ================= CURSORS =================

//    private void loadCursors() {
//        penCursor = loadCursor("cursors/pen.png", "Pen", 0, 0);
//        eraserCursor = loadCursor("cursors/eraser.png", "Eraser", 8, 8);
//        lineCursor = loadCursor("cursors/line.png", "Line", 8, 8);
//        rectCursor = loadCursor("cursors/rect.png", "Rect", 8, 8);
//        ellipseCursor = loadCursor("cursors/ellipse.png", "Ellipse", 8, 8);
//    }
//
//    private Cursor loadCursor(String path, String name, int x, int y) {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Image img = tk.getImage(path);
//        MediaTracker t = new MediaTracker(this);
//        t.addImage(img, 0);
//        try { t.waitForAll(); } catch (Exception ignored) {}
//        return tk.createCustomCursor(img, new Point(x, y), name);
//    }

	private void openImageFromFile(File file) {
		try {
			BufferedImage img = ImageIO.read(file);
			if (img != null) {
				drawPanel.loadImage(img); // ✅ HIER
				drawPanel.resetHistory();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Bild konnte nicht geladen werden!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ================= DRAW PANEL =================

	class DrawPanel extends JPanel {

		private BufferedImage image;
		private Graphics2D g2;

		private int lastX, lastY, startX, startY, currentX, currentY;
		private boolean drawingShape = false;

		private Stack<BufferedImage> undo = new Stack<>();
		private Stack<BufferedImage> redo = new Stack<>();

		public DrawPanel() {
			setPreferredSize(new Dimension(800, 600));
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			initImage();

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					startX = e.getX();
					startY = e.getY();
					currentX = startX;
					currentY = startY;

					if (currentTool == Tool.LINE || currentTool == Tool.RECTANGLE || currentTool == Tool.ELLIPSE) {

						drawingShape = true;
					} else {
						saveState(); // für Pen / Eraser
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (!drawingShape)
						return;

					saveState();

					g2.setColor(currentColor);
					g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

					int x = Math.min(startX, currentX);
					int y = Math.min(startY, currentY);
					int w = Math.abs(currentX - startX);
					int h = Math.abs(currentY - startY);

					switch (currentTool) {
					case LINE -> g2.drawLine(startX, startY, currentX, currentY);
					case RECTANGLE -> g2.drawRect(x, y, w, h);
					case ELLIPSE -> g2.drawOval(x, y, w, h);
					}

					drawingShape = false;
					repaint();
				}

			});

			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					currentX = e.getX();
					currentY = e.getY();

					if (currentTool == Tool.PEN) {
						g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2.setColor(currentColor);
						g2.drawLine(startX, startY, currentX, currentY);
						startX = currentX;
						startY = currentY;
					}

					if (currentTool == Tool.ERASER) {
						g2.setComposite(AlphaComposite.Clear);
						g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2.drawLine(startX, startY, currentX, currentY);
						g2.setComposite(AlphaComposite.SrcOver);
						startX = currentX;
						startY = currentY;
					}

					repaint(); // wichtig für Vorschau
				}

				public void mouseMoved(MouseEvent e) {
					currentX = e.getX();
					currentY = e.getY();
					repaint();
				}
			});
		}

		private void initImage() {
			image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
			g2 = image.createGraphics();
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		}

		public void clear() {
			initImage();
			repaint();
		}

		public BufferedImage getImage() {
			return image;
		}

		public void loadImage(BufferedImage img) {
			image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			g2 = image.createGraphics();
			g2.drawImage(img, 0, 0, null);
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			revalidate();
			repaint();
		}

		public void saveState() {
			BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			copy.getGraphics().drawImage(image, 0, 0, null);
			undo.push(copy);
			redo.clear();
		}

		public void undo() {
			if (!undo.isEmpty()) {
				redo.push(image);
				image = undo.pop();
				g2 = image.createGraphics();
				repaint();
			}
		}

		public void redo() {
			if (!redo.isEmpty()) {
				undo.push(image);
				image = redo.pop();
				g2 = image.createGraphics();
				repaint();
			}
		}

		public void resetHistory() {
			undo.clear();
			redo.clear();
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);

			if (drawingShape) {
				Graphics2D g2d = (Graphics2D) g.create();

				g2d.setColor(currentColor);
				g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

				int x = Math.min(startX, currentX);
				int y = Math.min(startY, currentY);
				int w = Math.abs(currentX - startX);
				int h = Math.abs(currentY - startY);

				switch (currentTool) {
				case LINE -> g2d.drawLine(startX, startY, currentX, currentY);
				case RECTANGLE -> g2d.drawRect(x, y, w, h);
				case ELLIPSE -> g2d.drawOval(x, y, w, h);
				}

				g2d.dispose();
			}
		}
	}
}
