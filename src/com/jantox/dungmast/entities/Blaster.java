package com.jantox.dungmast.entities;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.UserInput;
import com.jantox.dungmast.math.Vector2D;

public class Blaster extends Weapon {

	public Blaster(Entity owner) {
		super(owner);
	}
	
	public void update() {
		super.update();
	}

	@Override
	public void attack() {
		Vector2D pvel = new Vector2D(UserInput.x, UserInput.y);
		Vector2D cvel = new Vector2D(this.pos.x - map.getCamera().pos.x,
				this.pos.y - map.getCamera().pos.y - 20);

		double angle = cvel.angleTo(pvel);

		double cx = Math.cos(angle);
		double cy = Math.sin(angle);
		Vector2D vel = new Vector2D(cx, cy);

		for (int i = 0; i < 1; i++) {
			Vector2D np = new Vector2D(this.pos.x + rand.nextGaussian() * 4,
					this.pos.y + rand.nextGaussian() * 4);
			map.spawn(new Projectile(np, vel, angle, Projectile.BLAST));
		}
	}

	@Override
	public void render(Renderer renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRest() {
		return 10;
	}

}
