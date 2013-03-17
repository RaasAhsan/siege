package com.jantox.dungmast;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import com.jantox.dungmast.math.Camera;
import com.jantox.dungmast.math.Vector2D;

public class Renderer {

	Graphics g;
	Camera camera;
	
	private Renderer(Graphics g, Camera c) {
		this.g = g;
		this.camera = c;
		g.setColor(new Color(100, 200, 14));
	}
	
	public void drawLine(Vector2D a, Vector2D b, boolean cam) {
		if(cam)
			g.drawLine(a.getX() - (int)camera.interpos.getX(), a.getY() - (int)camera.interpos.getY(), b.getX() - (int)camera.interpos.getX(), b.getY() - (int)camera.interpos.getY());
		else
			g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	public void drawText(String text, Vector2D pos) {
		g.drawString(text, pos.getX(), pos.getY());
	}
	
	public static Renderer create(Graphics g, Camera c) {
		return new Renderer(g, c);
	}

	public void drawSprite(Sprite sprite, Vector2D pos, boolean b) {
		if(b) {
			if(pos.x > camera.interpos.x - 256 && pos.x < camera.interpos.x + camera.sdim.x && pos.y > camera.interpos.y - 256 && pos.y < camera.interpos.y + camera.sdim.y + 10)
				g.drawImage(sprite.getCurrentImage(), pos.getX() - camera.interpos.getX(), pos.getY() - camera.interpos.getY(), null);
		} else {
			g.drawImage(sprite.getCurrentImage(), pos.getX(), pos.getY(), null);
		}
	}

	public void fill(Rectangle rectangle, boolean b) {
		if(b)
			g.fillRect(rectangle.x - (int)camera.interpos.x, rectangle.y - (int)camera.interpos.y, rectangle.width, rectangle.height);
		else
			g.fillRect(rectangle.x , rectangle.y, rectangle.width, rectangle.height);
	}
	
	public void setFont(Font f) {
		g.setFont(f);
	}
	
	public void setColor(Color c) {
		g.setColor(c);
	}

	public void drawImage(BufferedImage img, Vector2D pos) {
		g.drawImage(img, pos.getX(), pos.getY(), null);
	}
	
	public void drawRect(Rectangle r) {
		g.drawRect(r.x, r.y, r.width, r.height);
	}

	public void drawSprite(Sprite sprite, Vector2D pos, RescaleOp rop) {
		((Graphics2D)g).drawImage(sprite.getCurrentImage(), rop, pos.getX() - (int)camera.interpos.x, pos.getY() - (int)camera.interpos.y);
	}

	public void fillRect(int i, int j, int k, int l) {
		g.fillRect(i, j, k, l);
	}
	
	public void drawCircle(Vector2D pos, Vector2D dim) {
		g.drawOval(pos.getX(), pos.getY(), dim.getX(), dim.getY());
	}
	
	public void fillCircle(Vector2D pos, Vector2D dim) {
		g.fillOval(pos.getX(), pos.getY(), dim.getX(), dim.getY());
	}
	
}
