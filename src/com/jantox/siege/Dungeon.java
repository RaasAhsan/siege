package com.jantox.siege;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dungeon implements ActionListener {
	
	private JFrame frame;
	
	public Dungeon() {
		frame = new JFrame();
		frame.setTitle("Siege");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		Menu menu = new Menu();
		menu.setLayout(null);
		
		JButton play = new JButton("Play");
		play.setBounds(350 - 75, 320, 150, 40);
		play.addActionListener(this);
		
		JButton multi = new JButton("Online");
		multi.setBounds(350 - 75, 365, 150, 40);
		
		JButton help = new JButton("Help");
		help.setBounds(350 - 75, 410, 150, 40);
		
		JButton quit = new JButton("Quit");
		quit.setBounds(350 - 75, 455, 150, 40);
		
		menu.add(play);
		menu.add(multi);
		menu.add(help);
		menu.add(quit);
		
		frame.getContentPane().add(menu);
		frame.pack();
		
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new Dungeon();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		frame.getContentPane().removeAll();
		DungeonGame game = new DungeonGame(false);
		frame.getContentPane().add(game);
		frame.pack();
	}
	
}
