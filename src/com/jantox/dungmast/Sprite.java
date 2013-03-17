package com.jantox.dungmast;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {
	
	private Rectangle frame;
	private BufferedImage sheet;
	
	private int cframe, start, end, delay;
	private int cdelay;
	private boolean completed;
	
	private ArrayList<Animation> def;

	public Sprite(int width, int height, BufferedImage sheet) {
		frame = new Rectangle(0, 0, width, height);
		cframe = start = end = delay = cdelay = 0;
		this.sheet = sheet;
		
		completed = false;
		
		def = new ArrayList<Animation>();
	}
	
	public BufferedImage getCurrentImage() {
		return sheet.getSubimage((int)frame.getX(), 0, (int)frame.getWidth(), (int)frame.getHeight());
	}
	
	public boolean isAnimation(int a, int b, int c) {
		return (start == a && end == b & delay == c);
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void update() {
		if(start <= end) {
			cdelay++;
			if(cframe >= end && cdelay >= delay) {
				if(start <= end) {
					completed = true;
					cframe = start;
					frame.x = cframe * frame.width;
					cdelay = 0;
				}
			} else if(cframe < end) {
				if(cdelay >= delay) {
					cframe++;
					cdelay = 0;
					
					frame.x = cframe * frame.width;
				}
			} 
		} else {
			cdelay++;
			if(cframe <= end && cdelay >= delay) {
				completed = true;
				cframe = start;
				frame.x = cframe * frame.width;
				cdelay = 0;
			} else if(cframe > end) {
				if(cdelay >= delay) {
					cframe--;
					cdelay = 0;
					
					frame.x = cframe * frame.width;
				}
			} 
		}
	}
	
	public void render(Graphics g, int x, int y) {
		BufferedImage iframe = sheet.getSubimage((int)frame.getX(), (int)frame.getY(), (int)frame.getWidth(), (int)frame.getHeight());
		
		g.drawImage(iframe, x, y, null);
	}
	
	public void setAnimation(int id) {
		Animation a = null;
		for(int i = 0; i < def.size(); i++) {
			if(def.get(i).id == id) {
				a = def.get(i);
				break;
			}
		}
		
		if(a != null)
			this.setAnimation(a.start, a.end, a.delay);
	}
	
	public void setAnimation(int start, int end, int delay) {
		this.start = this.cframe = start;
		this.end = end;
		this.delay = delay;
		completed = false;
	}
	
	public int getCurrentFrame() {
		return cframe;
	}
	
	public void setCurrentFrame(int f) {
		cframe = f;
	}
	
	public int getStartFrame() {
		return start;
	}
	
	public int getEndFrame() {
		return end;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void defineAnimation(int id, int a, int b, int c) {
		def.add(new Animation(id, a, b, c));
	}
	
	public void defineAnimation(Animation a) {
		def.add(a);
	}
	
	public class Animation {
		
		int id, start, end, delay;
		
		public Animation(int a, int b, int c, int d) {
			id = a;
			start = b;
			end = c;
			delay = d;
		}
		
	}

	public int getWidth() {
		return (int) frame.getWidth();
	}
	
	public int getHeight() {
		return (int) frame.getHeight();
	}
	
}