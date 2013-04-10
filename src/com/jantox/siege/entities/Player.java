package com.jantox.siege.entities;

import java.awt.Color;
import java.util.ArrayList;

import com.jantox.siege.Item;
import com.jantox.siege.Keyboard;
import com.jantox.siege.PotionEffect;
import com.jantox.siege.colsys.AABB;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.monsters.MonsterFactory;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Camera;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.particles.ColorParticle;
import com.jantox.siege.particles.TextureParticle;
import com.jantox.siege.scripts.Assets;

public class Player extends Living {

	public Keyboard input;
	
	Vector2D prevpos;
	
	private Camera cam;
	private int armor;
	
	public Sprite heart, bodyarmor;
	public ArrayList<PotionEffect> peffects;
	
	private Inventory inventory;
	
	public Player(Vector2D pos, Keyboard ui, Vector2D sdim) {
		super(pos);
		
		peffects = new ArrayList<PotionEffect>();
		
		this.colmask = new Circle(this.pos, 10);
		this.input = ui;
		this.requestCollisions(entity_type.TREE, entity_type.FENCE, entity_type.GATE, entity_type.BARRICADE, entity_type.SPAWNER);
		this.health = this.maxhealth = 100;
		
		this.cam = new Camera(this.pos, sdim);
		
		this.sprite = Assets.loadSprite("player.png");
		this.heart = Assets.loadSprite("hearts.png");
		this.bodyarmor = Assets.loadSprite("bodyarmor.png");
		
		heart.setAnimation(2,2,0);
		heart.update();
		
		inventory = new Inventory();
	}
	
	public void init() {
		this.pickup(new Blaster(this));
		/*this.pickup(new Bow(this));
		this.pickup(new Potion(this, 1));
		for(int i = 0; i < 64; i++) {
			this.pickup(new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW));
		}
		for(int i = 0; i < 8; i++)
			this.pickup(new Claymore(this, new Vector2D(), 0));*/
	}
	
	@Override
	public void handleCollision(Entity e) {
		if(e instanceof Tree || e instanceof Fence || e instanceof Barricade || e instanceof MonsterFactory) {
			pos = prevpos;
			cam.update(this.pos.copy());
		}
		if(e instanceof Decoration) {
			if(((Decoration)e).getType() != Decoration.FOOTPATH) {
				pos = prevpos;
				cam.update(this.pos.copy());
			}
		}
	}

	@Override
	public void update() {
		prevpos = pos.copy();
		
		double speed = 1;
		for(int i = 0; i < peffects.size(); i++) {
			PotionEffect pe = peffects.get(i);
			if(pe.timeleft <= 0) {
				peffects.remove(pe);
			} else {
				pe.update();
				if(pe.potionid == 1)
					speed = 2;
				if(Entity.ticks % 15 == 0) {
					Vector2D vel = new Vector2D(rand.nextGaussian()/6, -0.5);
					Vector2D np = pos.copy();
					np.y -= 16;
					np.x += rand.nextGaussian() * 5;
					np.y += rand.nextGaussian() * 5;
					map.spawn(new TextureParticle(rand.nextInt(5) + 90, np.copy(), vel, 0, Assets.loadSprite("bubble.png")));
				}
			}
		}
		
		if(Keyboard.up) {
			pos.y-=1*speed;
			direction = UP;
		} else if(Keyboard.down) {
			pos.y+=1*speed;
			direction = DOWN;
		} 
		if(Keyboard.right) {
			pos.x += 1*speed;
			direction = RIGHT;
		} else if(Keyboard.left) {
			pos.x -= 1*speed;
			direction = LEFT;
		}
		
		/*if(potiontime > 0) {
			if(Entity.ticks % 15 == 0) {
				Vector2D vel = new Vector2D(rand.nextGaussian()/6, -0.5);
				Vector2D np = pos.copy();
				np.y -= 16;
				np.x += rand.nextGaussian() * 5;
				np.y += rand.nextGaussian() * 5;
				map.spawn(new TextureParticle(rand.nextInt(5) + 90, np.copy(), vel, 0, Assets.loadSprite("bubble.png")));
				
				potiontime --;
			}
		}*/
		
		double angle = this.pos.angleTo(new Vector2D(Keyboard.x + this.cam.pos.x, Keyboard.y + this.cam.pos.y));
		angle = Math.toDegrees(angle);
		angle -= 270 - 22.5;
		while(angle < 0)
			angle += 360;
		
		int sdir = (int) ((angle / 45));
		if(sdir < 0)
			sdir += 8;
		
		sprite.setAnimation(sdir, sdir, 0);
		sprite.update();
		
		this.direction = sdir;
		
		cam.update(this.pos.copy());
		colmask.update(this.pos);
		
		sprite.update();
		inventory.update();
	}

	@Override
	public void render(Renderer renderer) {
		if(direction == 7 || direction == 0 || direction == 1) {
			inventory.renderItem(renderer);
		}
		renderer.drawSprite(sprite, new Vector2D(pos.x - 31, pos.y - 44), true);
		if(direction >= 2 && direction < 7) {
			inventory.renderItem(renderer);
		}
	}

	@Override
	public void drop(int chance) {
		
	}
	
	public Camera getCamera() {
		return cam;
	}
	
	public boolean affectedByWealth() {
		for(PotionEffect pe : peffects) {
			if(pe.potionid == 3) 
				return true;
		}
		return false;
	}
	
	public boolean affectedByStrength() {
		for(PotionEffect pe : peffects) {
			if(pe.potionid == 2) 
				return true;
		}
		return false;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public boolean pickup(Item i) {
		return this.inventory.addItem(i);
	}
	
	public Vector2D getHoldingPosition() {
		if(direction == 0) {
			return new Vector2D(pos.x, pos.y - 5);
		} else if(direction == 1) {
			return new Vector2D(pos.x + 10, pos.y - 5);
		} else if(direction == 2) {
			return new Vector2D(pos.x + 16, pos.y);
		} else if(direction == 3) {
			return new Vector2D(pos.x + 10, pos.y + 5);
		} else if(direction == 4) {
			return new Vector2D(pos.x, pos.y + 8);
		} else if(direction == 5) {
			return new Vector2D(pos.x - 10, pos.y + 5);
		} else if(direction == 6) {
			return new Vector2D(pos.x - 16, pos.y);
		} else if(direction == 7) {
			return new Vector2D(pos.x - 10, pos.y - 5);
		}
		
		return new Vector2D(pos.x, pos.y);
	}

}
