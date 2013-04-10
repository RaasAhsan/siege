package com.jantox.siege.entities;

import java.awt.image.RescaleOp;

import com.jantox.siege.Item;
import com.jantox.siege.Keyboard;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.entities.Inventory.ItemType;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Claymore extends Entity implements Item {
	
	private Player player;
	
	private boolean inventory = true;
	private boolean held = false;

	public Claymore(Player player, Vector2D pos, int dir) {
		super(pos);
		
		this.direction = dir;
		
		this.sprite = Assets.loadSprite("claymore.png");
		this.colmask = new Circle(pos, 20);
		
		if(player != null)
			this.player = player;
		else
			inventory = false;
		
		this.requestCollisions(entity_type.ZOMBIE, entity_type.SKELETON);
	}
	
	public void update() {
		if(inventory && held) {
			Vector2D pvel = new Vector2D(Keyboard.x, Keyboard.y);
			Vector2D cvel = new Vector2D(map.getPlayer().pos.x - map.getCamera().pos.x, map.getPlayer().pos.y - map.getCamera().pos.y);
			
			double angle = cvel.angleTo(pvel);
			angle = Math.toDegrees(angle);
			angle -= 270 - 45;
			while(angle < 0)
				angle += 360;
			
			direction = (int) ((angle / 90));
			if(direction < 0)
				direction += 4;
			
			sprite.setAnimation(direction, direction, 0);
			sprite.update();
		} else {
			sprite.setAnimation(direction, direction, 0);
			sprite.update();
		}
	}

	@Override
	public void handleCollision(Entity e) {
		((Living)e).damage(1000);
		this.expired = true;
	}

	@Override
	public int onItemUse() {
		Vector2D pvel = new Vector2D(Keyboard.x + map.getCamera().pos.x, Keyboard.y + map.getCamera().pos.y);
		
		if(map.isFree(pvel, 5)) {
			this.pos = pvel.copy();
			map.getPlayer().getInventory().useItem(ItemType.CLAYMORE);
			map.spawn(new Claymore(null, this.pos.copy(), this.direction));
			held = false;
		}
		
		return 1;
	}

	@Override
	public void onHeld() {
		held = true;
	}

	@Override
	public void onNotHeld() {
		held = false;
	}

	@Override
	public void render(Renderer renderer) {
		if(!inventory)
			renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16), true);
		
		if(held) {
			Vector2D pvel = new Vector2D(Keyboard.x + map.getCamera().pos.x, Keyboard.y + map.getCamera().pos.y);
			
			this.pos = pvel.copy();
			
			if(map.isFree(pvel, 15)) {
				renderer.drawSprite(sprite, new Vector2D(pvel.x -16, pvel.y - 16), true);
			} else {
				float[] scales = {1f, 0f, 0f, 0.8f};
			    float[] offsets = new float[4];
			    RescaleOp rop = new RescaleOp(scales, offsets, null);
			    renderer.drawSprite(sprite, new Vector2D(pvel.x - 16, pvel.y - 16), rop);
			}
		}
	}

	@Override
	public int getRest() {
		return 15;
	}

}
