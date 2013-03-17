package com.jantox.dungmast.entities;

import java.util.List;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.UserInput;
import com.jantox.dungmast.entities.Inventory.ItemType;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Hammer extends Weapon {
	
	int use = 0;

	public Hammer(Entity owner) {
		super(owner);
		
		this.sprite = Assets.loadSprite("tool_hammer.png");
	}
	
	public void update() {
		super.update();
		
		Vector2D pvel = new Vector2D(UserInput.x, UserInput.y);
		Vector2D cvel = new Vector2D(map.getPlayer().pos.x - map.getCamera().pos.x, map.getPlayer().pos.y - map.getCamera().pos.y);
		
		double angle = cvel.angleTo(pvel);
		angle = Math.toDegrees(angle);
		angle -= 270 - 22.5;
		while(angle < 0)
			angle += 360;
		
		int sdir = (int) ((angle / 90));
		if(sdir < 0)
			sdir += 4;
		
		sprite.setAnimation(sdir, sdir, 0);
		sprite.update();
	}
	
	@Override
	public void render(Renderer renderer) {
		Vector2D hold = map.getPlayer().getHoldingPosition();
		renderer.drawSprite(sprite, new Vector2D(hold.x - 32, hold.y - 45), true);
	}

	@Override
	public void attack() {
		if (((Player) owner).getInventory().containsItem(ItemType.WOOD)) {
			Vector2D pvel = new Vector2D(UserInput.x, UserInput.y);
			Vector2D cvel = new Vector2D(this.pos.x - map.getCamera().pos.x,
					this.pos.y - map.getCamera().pos.y - 20);

			double angle = cvel.angleTo(pvel);

			double cx = Math.cos(angle);
			double cy = Math.sin(angle);

			cx *= 15;
			cy *= 15;

			Vector2D cpos = new Vector2D(pos.x + cx, pos.y + cy);

			List<Entity> entities = map.getEntities(cpos, 15);
			for (Entity e : entities) {
				if (e instanceof Gate) {
					((Gate) e).heal(1000);
					use += 20;
				}
			}
		}
		if (use >= 100) {
			((Player) owner).getInventory().useItem(ItemType.WOOD);
			use = 0;
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
