package com.jantox.siege;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.jantox.siege.colsys.AABB;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.entities.Axe;
import com.jantox.siege.entities.Blaster;
import com.jantox.siege.entities.Bow;
import com.jantox.siege.entities.Claymore;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Hammer;
import com.jantox.siege.entities.Inventory;
import com.jantox.siege.entities.Log;
import com.jantox.siege.entities.Potion;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.Sword;
import com.jantox.siege.entities.Inventory.ItemType;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Store {
	
	public enum ItemType {

		WOOD, BOW, ARROW, BLASTER, AXE, HAMMER, BARRICADE, SWORD, SENTRY_GUN, POTION_HEALTH, POTION_SWIFT, POTION_STRENGTH, POTION_WEALTH, CLAYMORE, AGC
		
	}
	
	int breaktime = 0;
	int selected;
	
	public Sprite[] item_sprites = new Sprite[16];
	
	private String name;
	private Sprite store;
	private Sprite selecti;
	
	private ArrayList<StoreItem> items;
	
	private Vector2D itmstart = new Vector2D(50 + 65, 50 + 60);
	
	public Store(String name) {
		this.name = name;
		
		items = new ArrayList<StoreItem>();
		
		this.store = Assets.loadSprite("interface_store.png");
		selecti = Assets.loadSprite("inv_select.png");
		selected = 0;
		
		item_sprites[ItemType.WOOD.ordinal()] = Assets.loadSprite("log.png");
		item_sprites[ItemType.BOW.ordinal()] = Assets.loadSprite("bow.png");
		
		item_sprites[ItemType.ARROW.ordinal()] = Assets.loadSprite("arrow.png");
		item_sprites[ItemType.ARROW.ordinal()].setAnimation(1, 1, 0);
		item_sprites[ItemType.ARROW.ordinal()].update();
		
		item_sprites[ItemType.BLASTER.ordinal()] = Assets.loadSprite("log.png");
		item_sprites[ItemType.AXE.ordinal()] = Assets.loadSprite("axe.png");
		item_sprites[ItemType.HAMMER.ordinal()] = Assets.loadSprite("hammer.png");
		item_sprites[ItemType.BARRICADE.ordinal()] = Assets.loadSprite("item_barricade.png");
		item_sprites[ItemType.SWORD.ordinal()] = Assets.loadSprite("item_sword.png");
		
		item_sprites[ItemType.SENTRY_GUN.ordinal()] = Assets.loadSprite("sentry_gun.png");
		item_sprites[ItemType.SENTRY_GUN.ordinal()].setAnimation(0,0,0);
		item_sprites[ItemType.SENTRY_GUN.ordinal()].update();
		
		item_sprites[ItemType.POTION_HEALTH.ordinal()] = Assets.loadSprite("potions.png");
		item_sprites[ItemType.POTION_HEALTH.ordinal()].setAnimation(0,0,0);
		item_sprites[ItemType.POTION_HEALTH.ordinal()].update();
		
		item_sprites[ItemType.POTION_SWIFT.ordinal()] = Assets.loadSprite("potions.png");
		item_sprites[ItemType.POTION_SWIFT.ordinal()].setAnimation(1,1,0);
		item_sprites[ItemType.POTION_SWIFT.ordinal()].update();
		
		item_sprites[ItemType.POTION_STRENGTH.ordinal()] = Assets.loadSprite("potions.png");
		item_sprites[ItemType.POTION_STRENGTH.ordinal()].setAnimation(2,2,0);
		item_sprites[ItemType.POTION_STRENGTH.ordinal()].update();
		
		item_sprites[ItemType.POTION_WEALTH.ordinal()] = Assets.loadSprite("potions.png");
		item_sprites[ItemType.POTION_WEALTH.ordinal()].setAnimation(3,3,0);
		item_sprites[ItemType.POTION_WEALTH.ordinal()].update();
		
		item_sprites[ItemType.CLAYMORE.ordinal()] = Assets.loadSprite("claymore.png");
		item_sprites[ItemType.CLAYMORE.ordinal()].setAnimation(2,2,0);
		item_sprites[ItemType.CLAYMORE.ordinal()].update();
		
		item_sprites[ItemType.AGC.ordinal()] = Assets.loadSprite("agc.png");
		item_sprites[ItemType.AGC.ordinal()].setAnimation(4,4,0);
		item_sprites[ItemType.AGC.ordinal()].update();
	}
	
	public void addItem(Item i, int amount, int cost, String name, int get) {
		items.add(new StoreItem(i, amount, itmstart.copy(), cost, name, get));
		itmstart.x += 50;
		if(items.size() % 10 == 0) {
			itmstart.x = 115;
			itmstart.y += 50;
		}
	}
	
	public void update() {
		if(breaktime > 0)
			breaktime --;
		
		Vector2D mouse = new Vector2D(Entity.map.getPlayer().input.x, Entity.map.getPlayer().input.y);
		for(StoreItem si : items) {
			if (CollisionSystem.collides(new Circle(new Vector2D(si.itempos.x + 16, si.itempos.y + 16), 16),
					new Circle(mouse,3))) {
				if(selected != items.indexOf(si)) {
					selected = items.indexOf(si);
				}
			}
		}
	}
	
	public void render(Renderer renderer) {
		renderer.drawSprite(store, new Vector2D(50, 50), false);
		renderer.setColor(Color.BLACK);
		renderer.drawText(name, new Vector2D(300, 75));
		
		int t = 0;
		for(StoreItem i : items) {
			Vector2D itempos = i.itempos;
			if(selected == t) {
				renderer.setColor(Color.BLACK);
				renderer.drawRect(new Rectangle(itempos.getX(), itempos.getY() - 3, 32, 32));
			}
			renderer.setColor(Color.WHITE);
			renderer.setFont(new Font("Lucida Console", Font.BOLD, 11));
			renderer.drawSprite(item_sprites[this.getItemTypeOf(i.i).ordinal()], new Vector2D(itempos.x, itempos.y - 2), false);
			t++;
		}
		
		renderer.setColor(Color.GREEN);
		renderer.drawText("You have: " + DungeonGame.coins + "cn", new Vector2D(70, 430));
		
		if (this.canAfford(items.get(selected).cost))
			renderer.setColor(Color.GREEN);
		else
			renderer.setColor(Color.RED);
		renderer.drawText("Item costs: " + items.get(selected).cost + "cn",
				new Vector2D(505, 430));

		renderer.setColor(new Color(0, 255, 255));
		renderer.drawText(
				"" + items.get(selected).itemname + " (x"
						+ items.get(selected).get + ")", new Vector2D(295, 430));

	}
	
	public String getShopName() {
		return name;
	}
	
	public ItemType getItemTypeOf(Item i) {
		if(i instanceof Log) {
			return ItemType.WOOD;
		} else if(i instanceof Projectile) {
			if(((Projectile)i).getType() == Projectile.ARROW) {
				return ItemType.ARROW;
			}
		} else if(i instanceof Bow) {
			return ItemType.BOW;
		} else if(i instanceof Blaster) {
			return ItemType.BLASTER;
		} else if(i instanceof Axe) {
			return ItemType.AXE;
		} else if(i instanceof Hammer) {
			return ItemType.HAMMER;
		} else if(i instanceof Barricade) {
			return ItemType.BARRICADE;
		} else if(i instanceof Sword) {
			return ItemType.SWORD;
		} else if(i instanceof SentryGun) {
			return ItemType.SENTRY_GUN;
		} else if(i instanceof Potion) {
			Potion p = (Potion) i;
			if(p.getType() == 0)
				return ItemType.POTION_HEALTH;
			else if(p.getType() == 1)
				return ItemType.POTION_SWIFT;
			else if(p.getType() == 2)
				return ItemType.POTION_STRENGTH;
			else if(p.getType() == 3)
				return ItemType.POTION_WEALTH;
		} else if(i instanceof AGC) {
			return ItemType.AGC;
		} else if(i instanceof Claymore) {
			return ItemType.CLAYMORE;
		}
		
		return null;
	}

	public void buy() {
		if(breaktime <= 0) {
			if(this.canAfford(items.get(selected).cost)) {
				DungeonGame.coins -= items.get(selected).cost;
				breaktime = 5;
				items.get(selected).amount--;
				for(int i = 0; i < items.get(selected).get; i++)
					Entity.map.getPlayer().getInventory().addItem(Inventory.getItemOfType(Inventory.getItemTypeOf(this.items.get(selected).i)));
			}
		}
	}
	
	public boolean canAfford(int cost) {
		if(DungeonGame.coins < cost)
			return false;
		return true;
	}
	
	public class StoreItem {
		
		Item i;
		int amount;
		Vector2D itempos;
		int cost;
		String itemname;
		int get;
		
		public StoreItem(Item i, int amount, Vector2D pos, int cost, String itemname, int get) {
			this.i = i;
			this.amount = amount;
			this.itempos = pos;
			this.cost = cost;
			this.itemname = itemname;
			this.get = get;
		}
		
	}
	
}
