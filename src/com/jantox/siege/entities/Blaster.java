package com.jantox.siege.entities;

import com.jantox.siege.Keyboard;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;
import com.jantox.siege.sfx.Sound;
import com.jantox.siege.sfx.Sounds;

public class Blaster extends Weapon {

	public Blaster(Entity owner) {
		super(owner);
		
		this.sprite = Assets.loadSprite("blaster.png");
	}
	
	public void update() {
		super.update();
		
		Vector2D pvel = new Vector2D(Keyboard.x, Keyboard.y);
		Vector2D cvel = new Vector2D(map.getPlayer().pos.x - map.getCamera().pos.x, map.getPlayer().pos.y - map.getCamera().pos.y);
		
		double angle = cvel.angleTo(pvel);
		angle = Math.toDegrees(angle);
		angle -= 270 - 22.5;
		while(angle < 0)
			angle += 360;
		
		int sdir = (int) ((angle / 45));
		if(sdir < 0)
			sdir += 8;
		
		sprite.setAnimation(sdir, sdir, 0);
		sprite.update();
	}

	@Override
	public void attack() {
		Vector2D pvel = new Vector2D(Keyboard.x, Keyboard.y);
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
		Vector2D hold = owner.getHoldingPosition();
		renderer.drawSprite(sprite, new Vector2D(hold.x - 32, hold.y - 42), true);
	}

	@Override
	public int getRest() {
		return 10;
	}

}
