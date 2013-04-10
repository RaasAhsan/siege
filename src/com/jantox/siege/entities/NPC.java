package com.jantox.siege.entities;

import com.jantox.siege.Store;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class NPC extends Entity {

	public static final int ELI = 0;
	public static final int BRENDAN = 1;
	public static final int UPGRADE_STATION = 2;
	
	private int npc;
	private Store store;
	
	public NPC(Vector2D pos, int npc) {
		super(pos);
		
		this.npc = npc;
		
		if(npc == ELI) {
			this.colmask = new Circle(this.pos, 50);
			this.sprite = Assets.loadSprite("blacksmith.png");
			
			this.store = new Store("Eli's Forge");
			store.addItem(new Bow(this), 2, 300, "Bow", 1);
			store.addItem(new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW), 64, 300, "Arrow", 32);
			store.addItem(new SentryGun(Entity.map.getPlayer(), new Vector2D()), 64, 500, "Sentry Gun", 5);
			store.addItem(new AGC(Entity.map.getPlayer(), new Vector2D()), 64, 750, "AGC", 5);
			store.addItem(new Barricade(Entity.map.getPlayer(), new Vector2D()), 64, 250, "Barricade", 16);
			store.addItem(new Claymore(Entity.map.getPlayer(), new Vector2D(), 2), 64, 500, "Claymore", 16);
		} else if(npc == BRENDAN) {
			this.colmask = new Circle(this.pos, 50);
			this.sprite = Assets.loadSprite("magician.png");
			
			this.store = new Store("Potionry Shop");
			store.addItem(new Potion(Entity.map.getPlayer(), 0), 5, 50, "Potion of Health", 1);
			store.addItem(new Potion(Entity.map.getPlayer(), 1), 34, 100, "Potion of Agileness", 1);
			store.addItem(new Potion(Entity.map.getPlayer(), 2), 34, 100, "Potion of Strength", 1);
			store.addItem(new Potion(Entity.map.getPlayer(), 3), 34, 150, "Potion of Wealth", 1);
		} else if(npc == UPGRADE_STATION) {
			this.colmask = new Circle(this.pos, 15);
			this.sprite = Assets.loadSprite("upgrade_station.png");
			sprite.setAnimation(0, 3, 4);
			
			this.store = new Store("Upgrade Station");
			store.addItem(new Bow(this), 2, 300, "Bow", 1);
			store.addItem(new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW), 64, 300, "Arrow", 32);
			store.addItem(new SentryGun(Entity.map.getPlayer(), new Vector2D()), 64, 500, "Sentry Gun", 5);
			store.addItem(new AGC(Entity.map.getPlayer(), new Vector2D()), 64, 750, "AGC", 5);
			store.addItem(new Barricade(Entity.map.getPlayer(), new Vector2D()), 64, 250, "Barricade", 16);
			store.addItem(new Claymore(Entity.map.getPlayer(), new Vector2D(), 2), 64, 500, "Claymore", 16);
		}
	}
	
	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Renderer renderer) {
		sprite.update();
		if(npc == ELI) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 31, pos.y - 58), true);
		} else if(npc == BRENDAN) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 33, pos.y - 60), true);
		} else if(npc == UPGRADE_STATION) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 30, pos.y - 52), true);
		}
	}

	public Store getStore() {
		return store;
	}

}
