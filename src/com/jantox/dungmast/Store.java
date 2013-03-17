package com.jantox.dungmast;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.jantox.dungmast.entities.Axe;
import com.jantox.dungmast.entities.Barricade;
import com.jantox.dungmast.entities.Blaster;
import com.jantox.dungmast.entities.Bow;
import com.jantox.dungmast.entities.Hammer;
import com.jantox.dungmast.entities.Log;
import com.jantox.dungmast.entities.Potion;
import com.jantox.dungmast.entities.Projectile;
import com.jantox.dungmast.entities.SentryGun;
import com.jantox.dungmast.entities.Sword;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Store {
	
	enum ItemType {

		WOOD, BOW, ARROW, BLASTER, AXE, HAMMER, BARRICADE, SWORD, SENTRY_GUN, POTION_HEALTH, POTION_SWIFT, POTION_STRENGTH, POTION_WEALTH
		
	}
	
	public Sprite[] item_sprites = new Sprite[16];
	
	private String name;
	private Sprite store;
	
	private ArrayList<Item> items;
	private ArrayList<Integer> amounts;
	
	public Store(String name) {
		this.name = name;
		
		items = new ArrayList<Item>();
		amounts = new ArrayList<Integer>();
		
		this.store = Assets.loadSprite("interface_store.png");
		
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
		item_sprites[ItemType.SENTRY_GUN.ordinal()] = Assets.loadSprite("log.png");
		
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
	}
	
	public void addItem(Item i, int amount) {
		items.add(i);
		amounts.add(amount);
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		
		renderer.drawSprite(store, new Vector2D(50, 50), false);
		renderer.setColor(Color.BLACK);
		renderer.drawText(name, new Vector2D(300, 75));
		
		Vector2D itempos = new Vector2D(50 + 75, 50 + 60);
		int t = 0;
		for(Item i : items) {
			renderer.setColor(Color.BLACK);
			renderer.drawRect(new Rectangle(itempos.getX(), itempos.getY(), 32, 32));
			renderer.setColor(Color.WHITE);
			renderer.setFont(new Font("Lucida Console", Font.BOLD, 11));
			renderer.drawSprite(item_sprites[this.getItemTypeOf(i).ordinal()], itempos, false);
			renderer.drawText(amounts.get(t) + "", new Vector2D(itempos.x + 25, itempos.y + 32));
			itempos.x += 50;
			t++;
		}
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
		}
		
		return null;
	}
	
}
