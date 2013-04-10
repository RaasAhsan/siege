package com.jantox.siege;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jantox.siege.scripts.Assets;

public class Dungeon implements ActionListener {
	
	private JFrame frame;
	
	public Dungeon() {
		frame = new JFrame();
		frame.setTitle("Siege");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		try {
			frame.setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/res/game_icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assets.init();
		
		Menu menu = new Menu();
		menu.setLayout(null);
		
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("/res/interface_button.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JButton play = new JButton("Play", icon);
		play.setForeground(Color.WHITE);
		play.setBounds(350 - 180, 320, 150, 40);
		play.setHorizontalTextPosition(JButton.CENTER);
		play.setVerticalTextPosition(JButton.CENTER);
		play.setBorder(BorderFactory.createEmptyBorder());
		play.setContentAreaFilled(false);
		play.addActionListener(this);
		
		JButton multi = new JButton("Online", icon);
		multi.setForeground(Color.WHITE);
		multi.setBounds(350 + 5, 320, 150, 40);
		multi.setHorizontalTextPosition(JButton.CENTER);
		multi.setVerticalTextPosition(JButton.CENTER);
		multi.setBorder(BorderFactory.createEmptyBorder());
		multi.setContentAreaFilled(false);
		multi.addActionListener(this);
		
		JButton help = new JButton("Help", icon);
		help.setForeground(Color.WHITE);
		help.setBounds(350 - 180, 320 + 65, 150, 40);
		help.setHorizontalTextPosition(JButton.CENTER);
		help.setVerticalTextPosition(JButton.CENTER);
		help.setBorder(BorderFactory.createEmptyBorder());
		help.setContentAreaFilled(false);
		help.addActionListener(this);
		
		JButton quit = new JButton("Quit", icon);
		quit.setForeground(Color.WHITE);
		quit.setBounds(350 + 5, 320 + 65, 150, 40);
		quit.setHorizontalTextPosition(JButton.CENTER);
		quit.setVerticalTextPosition(JButton.CENTER);
		quit.setBorder(BorderFactory.createEmptyBorder());
		quit.setContentAreaFilled(false);
		quit.addActionListener(this);
		
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
