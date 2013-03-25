package com.jantox.siege.entities;

import java.awt.image.RescaleOp;

import com.jantox.siege.Item;
import com.jantox.siege.UserInput;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.entities.Inventory.ItemType;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Barricade extends Living implements Item {
	
	private Player player;
	
	private boolean inventory = true;
	private boolean held = false;

	public Barricade(Player player, Vector2D pos) {
		super(pos);
		
		this.maxhealth = this.health = 1000;
		
		this.sprite = Assets.loadSprite("barricade.png");
		this.colmask = new Circle(pos, 15);
		
		if(player != null)
			this.player = player;
		else
			inventory = false;
	}
	
	public void update() {
		super.update();
	}

	@Override
	public void render(Renderer renderer) {
		if(!inventory)
			renderer.drawSprite(sprite, new Vector2D(pos.x - 32, pos.y - 40), true);
		
		if(held) {
			Vector2D pvel = new Vector2D(UserInput.x + map.getCamera().pos.x, UserInput.y + map.getCamera().pos.y);
			
			this.pos = pvel.copy();
			
			if(map.isFree(pvel, 15)) {
				renderer.drawSprite(sprite, new Vector2D(pvel.x - 32, pvel.y - 40), true);
			} else {
				float[] scales = {1f, 0f, 0f, 0.8f};
			    float[] offsets = new float[4];
			    RescaleOp rop = new RescaleOp(scales, offsets, null);
			    renderer.drawSprite(sprite, new Vector2D(pvel.x - 32, pvel.y - 40), rop);
			}
		}
	}
	
	@Override
	public void drop(int chance) {
		
	}

	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public int onItemUse() {		
		Vector2D pvel = new Vector2D(UserInput.x + map.getCamera().pos.x, UserInput.y + map.getCamera().pos.y);
		
		if(map.isFree(pvel, 5)) {
			this.pos = pvel.copy();
			map.getPlayer().getInventory().useItem(ItemType.BARRICADE);
			map.spawn(new Barricade(null, this.pos.copy()));
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
	public int getRest() {
		return 10;
	}

}
