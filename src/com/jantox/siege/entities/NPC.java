package com.jantox.siege.entities;

import com.jantox.siege.Store;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class NPC extends Entity {

	public static final int ELI = 0;
	public static final int BRENDAN = 1;
	
	private int npc;
	private Store store;
	
	public NPC(Vector2D pos, int npc) {
		super(pos);
		
		this.npc = npc;
		
		if(npc == ELI) {
			this.colmask = new Circle(this.pos, 15);
			this.sprite = Assets.loadSprite("blacksmith.png");
			
			this.store = new Store("Eli's Forge");
		} else if(npc == BRENDAN) {
			this.colmask = new Circle(this.pos, 15);
			this.sprite = Assets.loadSprite("magician.png");
			
			this.store = new Store("Xephus Potionry");
			store.addItem(new Potion(null, 0), 5);
			store.addItem(new Sword(this), 3);
			store.addItem(new Axe(this), 2);
			store.addItem(new Barricade(null, new Vector2D()), 64);
			store.addItem(new Potion(null, 2), 34);
			store.addItem(new Bow(this), 2);
			store.addItem(new Potion(null, 3), 11);
		}
	}
	
	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Renderer renderer) {
		if(npc == ELI) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 31, pos.y - 58), true);
		} else if(npc == BRENDAN) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 33, pos.y - 60), true);
		}
	}

	public Store getStore() {
		return store;
	}

}
