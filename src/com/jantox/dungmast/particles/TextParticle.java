package com.jantox.dungmast.particles;

import java.awt.Color;
import java.awt.Font;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.math.Vector2D;

public class TextParticle extends Particle {
	
	String text;
	Color c;
	Font f;

	public TextParticle(int life, Vector2D pos, Vector2D vel, Font f, Color c, String text) {
		super(life, pos, vel, 0);
		this.text = text;
		this.c = c;
		this.f = f;
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.setColor(c);
		renderer.setFont(f);
		renderer.drawText(text, pos);
	}
	
	@Override
	public void handleCollision(Entity e) {}

}
