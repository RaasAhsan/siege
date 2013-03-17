package com.jantox.dungmast.particles;

import java.awt.Color;
import java.awt.Rectangle;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.entities.Entity;
import com.jantox.dungmast.math.Vector2D;

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
