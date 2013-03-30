package com.jantox.siege;

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
		
		JButton play = new JButton("Play");
		play.setBounds(350 - 75, 320 - 20, 150, 40);
		//play.setIcon(icon);
		//play.setBorder(BorderFactory.createEmptyBorder());
		play.addActionListener(this);
		
		JButton multi = new JButton("Online");
		multi.setBounds(350 - 75, 365 - 20, 150, 40);
		
		JButton help = new JButton("Help");
		help.setBounds(350 - 75, 410 - 20, 150, 40);
		
		JButton quit = new JButton("Quit");
		quit.setBounds(350 - 75, 455 - 20, 150, 40);
		
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
