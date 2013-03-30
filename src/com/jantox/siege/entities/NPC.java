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
			store.addItem(new Potion(Entity.map.getPlayer(), 0), 5, 30, "Potion of Health");
			store.addItem(new Sword(this), 3, 100, "Steel Sword");
			store.addItem(new Axe(this), 2, 100, "Axe");
			store.addItem(new Hammer(this), 2, 100, "Hammer");
			store.addItem(new Potion(Entity.map.getPlayer(), 1), 34, 30, "Potion of Agileness");
			store.addItem(new Barricade(Entity.map.getPlayer(), new Vector2D()), 64, 150, "Barricade (x16)");
			store.addItem(new Potion(Entity.map.getPlayer(), 2), 34, 30, "Potion of Strength");
			store.addItem(new Bow(this), 2, 100, "Bow");
			store.addItem(new Potion(Entity.map.getPlayer(), 3), 11, 130, "Potion of Wealth");
			store.addItem(new SentryGun(null, new Vector2D()), 64, 200, "Sentry Gun (x4)");
			store.addItem(new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW), 64, 150, "Arrow (x16)");
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
