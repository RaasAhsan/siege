package com.jantox.siege.entities;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import com.jantox.siege.Item;
import com.jantox.siege.Keyboard;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Inventory extends Entity {
	
	public enum ItemType {

		WOOD, BOW, ARROW, BLASTER, AXE, HAMMER, BARRICADE, SWORD, SENTRY_GUN, POTION_HEALTH, POTION_SWIFT, POTION_STRENGTH, POTION_WEALTH
		
	}
	
	public static final int MAX_SLOT_AMOUNT = 256;
	
	public Sprite[] item_sprites = new Sprite[16];

	private ArrayList<ArrayList<Item>> slots;
	
	private Sprite selsprite;
	
	public int selected;
	private int rest;
	
	public Inventory() {
		super(new Vector2D(3, 334));
	
		slots = new ArrayList<ArrayList<Item>>();
		
		for(int i = 0; i < 9; i++) {
			slots.add(new ArrayList<Item>());
		}
		
		this.selected = 0;
		this.rest = 0;
		
		sprite = Assets.loadSprite("inventory.png");
		selsprite = Assets.loadSprite("inv_select.png");
		
		// load item sprites
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
	}
	
	public void update() {
		if(rest > 0)
			rest --;
		
		Item select = this.getSelectedItem();
		if(select != null) {
			select.update();
		}
		if(Keyboard.space || Keyboard.left_mouse) {
			if(select != null) {
				if(rest <= 0 && map.currentstore == null) {
					int res = select.onItemUse();
					rest = select.getRest();
					
					if(res == 0) {
						this.useItem(this.getItemTypeOf(select));
					}
				}
			}
		}
	}
	
	public void onHeldItem() {
		Item i = this.getSelectedItem();
		if(i != null) {
			i.onHeld();
		}
	}
	
	public Item getSelectedItem() {
		if(slots.get(selected).size() > 0) {
			return slots.get(selected).get(slots.get(selected).size() - 1);
		}
		return null;
	}
	
	public int getAmountOfItemsInSlot(int i) {
		return slots.get(i).size();
	}
	
	public Item getItemAtSlot(int i) {
		if(slots.get(i).size() > 0) {
			return slots.get(i).get(slots.get(i).size() - 1);
		}
		return null;
	}
	
	public void render(Renderer renderer) {
		//renderer.drawSprite(sprite, pos, false);
		//renderer.drawSprite(selsprite, new Vector2D(pos.x + selected * 35, pos.y), false);
		
		renderer.setColor(Color.WHITE);
		renderer.setFont(new Font("Lucida Console", Font.BOLD, 9));
		
		for(int i = 0; i < 4; i++) {
			Item item = this.getItemAtSlot(i);
			if(item != null) {
				renderer.drawSprite(item_sprites[this.getItemTypeOf(item).ordinal()], new Vector2D(pos.x + (32 * i) + 3, pos.y + 3), false);
				renderer.drawText(this.getAmountOfItemsInSlot(i) + "", new Vector2D(pos.x + (32 * i) + 3 , pos.y + 11));
			} else {
				continue;
			}
		}
	}
	
	public void renderItem(Renderer renderer) {
		Item select = this.getSelectedItem();
		if(select != null) {
			select.render(renderer);
		}
	}
	
	public boolean addItem(Item i) {
		int s = this.getFirstIncompleteSlotOf(this.getItemTypeOf(i));
		
		if(s == -1) {
			int e = this.getFirstEmptySlot();
			if(e == -1) {
				return false;
			} else {
				slots.get(e).add(i);
			}
		} else {
			slots.get(s).add(i);
		}
		
		return true;
	}
	
	public int getFirstIncompleteSlotOf(ItemType it) {
		for(int i = 0; i < 9; i++) {
			int a = this.getAmountOfItemsInSlot(i);
			if(a > 0 && a < 64) {
				if(this.getItemTypeOf(this.getItemAtSlot(i)) == it) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public int getFirstEmptySlot() {
		for(int i = 0; i < 9; i++) {
			if(slots.get(i).size() == 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean containsItem(ItemType type) {
		for(int i = 0; i < 9; i++) {
			if(this.getAmountOfItemsInSlot(i) > 0) {
				if(this.getItemTypeOf(this.getItemAtSlot(i)) == type) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void handleCollision(Entity e) {
		
	}
	
	public static ItemType getItemTypeOf(Item i) {
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
	
	public static Item getItemOfType(ItemType it) {
		if(it == ItemType.ARROW) {
			return new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW);
		} else if(it == ItemType.BARRICADE) {
			return new Barricade(Entity.map.getPlayer(), new Vector2D(0, 0));
		} else if(it == ItemType.POTION_HEALTH) {
			return new Potion(Entity.map.getPlayer(), Potion.POTION_HEALTH);
		} else if(it == ItemType.POTION_STRENGTH) {
			return new Potion(Entity.map.getPlayer(), Potion.POTION_STRENGTH);
		} else if(it == ItemType.POTION_SWIFT) {
			return new Potion(Entity.map.getPlayer(), Potion.POTION_SWIFT);
		} else if(it == ItemType.BOW) {
			return new Bow(Entity.map.getPlayer());
		} else if(it == ItemType.SENTRY_GUN) {
			return new SentryGun(null, new Vector2D(0,0));
		}
		return null;
	}

	public void useItem(ItemType type) {
		for(int i = 0; i < 9; i++) {
			if(this.getAmountOfItemsInSlot(i) > 0) {
				if(this.getItemTypeOf(this.getItemAtSlot(i)) == type) {
					slots.get(i).remove(slots.get(i).size() - 1);
					if(i == this.selected) {
						this.onHeldItem();
					}
					break;
				}
			}
		}
	}
	
}
