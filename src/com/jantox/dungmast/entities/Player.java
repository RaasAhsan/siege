package com.jantox.dungmast.entities;

import java.awt.Color;

import com.jantox.dungmast.Item;
import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.Sprite;
import com.jantox.dungmast.UserInput;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.colsys.AABB;
import com.jantox.dungmast.math.Camera;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.particles.ColorParticle;
import com.jantox.dungmast.particles.TextureParticle;
import com.jantox.dungmast.scripts.Assets;

public class Player extends Living {

	UserInput input;
	
	Vector2D prevpos;
	
	private Camera cam;
	private int armor;
	public int potion;
	
	public Sprite heart, bodyarmor;
	
	private Inventory inventory;
	
	public Player(Vector2D pos, UserInput ui, Vector2D sdim) {
		super(pos);
		
		potion = 0;
		
		this.colmask = new Circle(this.pos, 10);
		this.input = ui;
		this.requestCollisions(entity_type.TREE, entity_type.FENCE, entity_type.GATE, entity_type.BARRICADE, entity_type.SPAWNER);
		this.health = 100;
		
		this.cam = new Camera(this.pos, sdim);
		
		this.sprite = Assets.loadSprite("player.png");
		this.heart = Assets.loadSprite("hearts.png");
		this.bodyarmor = Assets.loadSprite("bodyarmor.png");
		
		heart.setAnimation(2,2,0);
		heart.update();
		
		inventory = new Inventory();
		
		this.pickup(new Blaster(this));
		this.pickup(new Bow(this));
		this.pickup(new Potion(this, 1));
		this.pickup(new Potion(this, 1));
		this.pickup(new Potion(this, 0));
		this.pickup(new Potion(this, 0));
		this.pickup(new Potion(this, 3));
		this.pickup(new Potion(this, 3));
		this.pickup(new Potion(this, 2));
		/*this.pickup(new Axe(this));
		this.pickup(new Hammer(this));
		for(int i = 0; i < 128; i++) {
			this.pickup(new Projectile(new Vector2D(0, 0), new Vector2D(0, 0), 0, Projectile.ARROW));
		}
		for(int i = 0; i < 16; i++)
			this.pickup(new Barricade(this, new Vector2D()));
		this.pickup(new Sword(this));
		for(int i = 0; i < 64; i++)
			this.pickup(new SentryGun(null, new Vector2D()));*/
	}
	
	@Override
	public void handleCollision(Entity e) {
		pos = prevpos;
		
		cam.update(this.pos.copy());
	}

	@Override
	public void update() {
		prevpos = pos.copy();
		if(UserInput.up) {
			pos.y-=1;
			direction = UP;
		} else if(UserInput.down) {
			pos.y+=1;
			direction = DOWN;
		} 
		if(UserInput.right) {
			pos.x += 1;
			direction = RIGHT;
		} else if(UserInput.left) {
			pos.x -= 1;
			direction = LEFT;
		}
		
		if(potion > 0) {
			if(Entity.ticks % 15 == 0) {
				Vector2D vel = new Vector2D(rand.nextGaussian()/6, -0.5);
				Vector2D np = pos.copy();
				np.y -= 16;
				np.x += rand.nextGaussian() * 5;
				np.y += rand.nextGaussian() * 5;
				map.spawn(new TextureParticle(rand.nextInt(5) + 90, np.copy(), vel, 0, Assets.loadSprite("bubble.png")));
				
				potion --;
			}
		}
		
		double angle = this.pos.angleTo(new Vector2D(UserInput.x + this.cam.pos.x, UserInput.y + this.cam.pos.y));
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
