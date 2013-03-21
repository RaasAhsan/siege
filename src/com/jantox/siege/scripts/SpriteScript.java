package com.jantox.siege.scripts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.gfx.Sprite.Animation;

public class SpriteScript {
	
	private int width, height;
	private String identifier;
	private BufferedImage sheet;
	private ArrayList<Animation> defined;
	
	public SpriteScript(String file, int width, int height, ArrayList<Animation> defined) {
		this.identifier = file.replace('/', '.');
		this.width = width;
		this.height = height;
		this.defined = defined;
		
		try {
			this.sheet = ImageIO.read(this.getClass().getResourceAsStream("/res/" + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Sprite compile() {
		Sprite s = new Sprite(width, height, sheet);
		
		if(defined != null) {
			for(Animation a : defined) {
				s.defineAnimation(a);
			}
		}
		return s;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
}
