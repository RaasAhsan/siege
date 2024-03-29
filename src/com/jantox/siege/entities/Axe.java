package com.jantox.siege.entities;

import java.util.List;

import com.jantox.siege.Keyboard;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;

public class Axe extends Weapon {

	public Axe(Entity owner) {
		super(owner);
	}
	
	public void update() {
		super.update();
		
	
	}
	
	@Override
	public void render(Renderer renderer) {
		
	}

	@Override
	public void attack() {
		Vector2D pvel = new Vector2D(Keyboard.x, Keyboard.y);
		Vector2D cvel = new Vector2D(this.pos.x - map.getCamera().pos.x,
				this.pos.y - map.getCamera().pos.y - 20);

		double angle = cvel.angleTo(pvel);

		double cx = Math.cos(angle);
		double cy = Math.sin(angle);

		cx *= 15; // 10 for arrow 4 for blaster
		cy *= 15;

		Vector2D cpos = new Vector2D(pos.x + cx, pos.y + cy);

		List<Entity> entities = map.getEntities(cpos, 10);
		for (Entity e : entities) {
			if (e instanceof Resource) {
				if (rand.nextInt() % 2 == 0)
					((Resource) e).useResource();
			}
		}
	}
	
	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public int getRest() {
		return 20;
	}

}
