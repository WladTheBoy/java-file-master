package com.jfm.util;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Monitor {

	private static Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

	public static int WIDTH = (int) monitorSize.getWidth();
	public static int HEIGHT = (int) monitorSize.getHeight();

	public static int AMOUNT = localGraphicsEnvironment.getScreenDevices().length;

	public Dimension getMonitorSize() {
		return this.monitorSize;
	}

	public GraphicsEnvironment getLocalGraphicsEnvironment() {
		return this.localGraphicsEnvironment;
	}

}
