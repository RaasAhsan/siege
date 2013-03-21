package com.jantox.siege.particles;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.math.Vector2D;

public abstract class Particle extends Entity {
	
	private int life;
	private Vector2D vel;
	private double gravity;
	
	private int curlife;
	
	public Particle(int life, Vector2D pos, Vector2D vel, double gravity) {
		super(pos);
		
		this.life = life;
		this.vel = vel;
		this.curlife = 0;
		this.gravity = gravity;
	}

	@Override
	public void update() {
		pos.add(vel);
		vel.y += gravity;
		
		curlife++;
		if(curlife >= life) {
			this.expired = true;
		}
	}
	
}
