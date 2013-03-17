package com.jantox.dungmast;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Menu extends JPanel {

	private BufferedImage menu;
	
	public Menu() {
		this.setPreferredSize(new Dimension(700, 500));
		try {
			menu = ImageIO.read(this.getClass().getResourceAsStream("/res/siege_title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(menu, 0, 0, null);
		g.setColor(Color.BLACK);
		g.drawString("Copyright Jantox Inc. 2013 All Rights Reserved", 410, 495);
	}
	
}
