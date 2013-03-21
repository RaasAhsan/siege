package com.jantox.siege.entities;

import java.util.List;

import com.jantox.siege.Item;
import com.jantox.siege.UserInput;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Sword extends Weapon implements Item {

	public Sword(Entity owner) {
		super(owner);
		
		this.sprite = Assets.loadSprite("iron_sword.png");
	}

	@Override
	public int getRest() {
		return 5;
	}

	@Override
	public void attack() {
		Vector2D pvel = new Vector2D(UserInput.x, UserInput.y);
		Vector2D cvel = new Vector2D(map.getPlayer().pos.x - map.getCamera().pos.x, map.getPlayer().pos.y - map.getCamera().pos.y);

		double angle = cvel.angleTo(pvel);

		double cx = Math.cos(angle);
		double cy = Math.sin(angle);

		cx *= 15;
		cy *= 15;

		Vector2D cpos = new Vector2D(pos.x + cx, pos.y + cy);

		List<Entity> entities = map.getEntities(cpos, 10);
		for (Entity e : entities) {
			if (e instanceof Zombie || e instanceof Skeleton) {
				((Living)e).damage(500);
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		Vector2D hold = map.getPlayer().getHoldingPosition();
		renderer.drawSprite(sprite, new Vector2D(hold.x - 31, hold.y - 44), true);
	}

}
