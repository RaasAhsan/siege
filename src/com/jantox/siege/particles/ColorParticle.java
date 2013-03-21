package com.jantox.siege.particles;

import java.awt.Color;
import java.awt.Rectangle;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;

public class ColorParticle extends Particle {

	Color c;
	Vector2D size;
	
	public ColorParticle(int life, Color c, Vector2D pos, Vector2D vel, Vector2D size, double gravity) {
		super(life, pos, vel, gravity);
		this.c = c;
		this.size = size;
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.setColor(c);
		renderer.fill(new Rectangle(pos.getX(), pos.getY(), size.getX(), size.getY()), true);
	}
	
	@Override
	public void handleCollision(Entity e) {
		
	}

}
