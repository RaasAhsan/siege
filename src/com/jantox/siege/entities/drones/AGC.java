package com.jantox.siege.entities.drones;

import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jantox.siege.Item;
import com.jantox.siege.Keyboard;
import com.jantox.siege.Map;
import com.jantox.siege.colsys.Circle;
import com.jantox.siege.colsys.CollisionSystem;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Living;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Entity.entity_type;
import com.jantox.siege.entities.Inventory.ItemType;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;
import com.jantox.siege.sfx.Sound;
import com.jantox.siege.sfx.Sounds;

// autonomous ground control
public class AGC extends Living implements Item {
	
	public Vector2D next_pos = null;
	private Vector2D velocity;
	
	private Entity attack = null;
	
	private Player player;
	
	private boolean inventory = true;
	private boolean held = false;

	public AGC(Player p, Vector2D pos) {
		super(pos);
		
		this.health = 100;
		this.colmask = new Circle(pos, 7);
		
		this.requestCollisions(entity_type.FENCE, entity_type.GATE, entity_type.BARRICADE);
		
		this.sprite = Assets.loadSprite("agc.png");
		sprite.setAnimation(0,7,5);
		direction = 0;
		
		if(p != null)
			this.player = p;
		else {
			inventory = false;
			Map.AGC_COUNT++;
		}
	}

	public void update() {
		super.update();
		
		if(!inventory) {
			if(next_pos == null) {
				ControlPoint cp = map.getRandomControlPoint();
				if(cp != null)
					next_pos = cp.getCloseTo(70);
				else
					next_pos = new Vector2D(Entity.rand.nextInt(6 * 32 + 55 * 32), Entity.rand.nextInt(6 * 32 + 55 * 32));
			}
			
			this.colmask.update(pos);
			
			if(attack == null)
				velocity = new Vector2D(next_pos.getX() - pos.getX(), next_pos.getY() - pos.getY());
			else
				velocity = new Vector2D(attack.getPosition().getX() - pos.getX(), attack.getPosition().getY() - pos.getY());
			
			velocity.normalize();
			velocity.multiply(0.5);
			
			ArrayList<Entity> ale = map.getEntities(entity_type.ALL);
			for(Entity e : ale) {
				if(e != null) {
					if(e != this && !(e instanceof ControlPoint) && !(e instanceof AGC) && !(e instanceof SentryGun) && !(e instanceof Barricade)) {
						if(CollisionSystem.collides(e.getMask(), this.getMask())) {
							Vector2D toa = new Vector2D(pos.x - e.pos.x, pos.y - e.pos.y);
							
							Vector2D nd = toa.copy();
							nd.normalize();
							nd.multiply(0.5);
							
							velocity = nd;
						}
					} 
					if (e instanceof Zombie || e instanceof Skeleton) {
						if(this.distanceSquared(e) <= 75 * 75) {
							attack = e;
							break;
						}
					}
					attack = null;
				}
			}
			
			pos.add(velocity);
			
			if(this.pos.distanceSquared(next_pos) <= 5 * 5) {
				ControlPoint cp = map.getRandomControlPoint();
				if(cp != null)
					next_pos = cp.getCloseTo(70);
				else
					next_pos = new Vector2D(rand.nextInt(6 * 32 + 55 * 32), rand.nextInt(6 * 32 + 55 * 32));
			}
			
			if(attack == null) {
				double angle = pos.angleTo(next_pos);
				angle = Math.toDegrees(angle);
				angle -= 270 - 45;
				while(angle < 0)
					angle += 360;
				
				int sdir = (int) ((angle / 90));
				if(sdir < 0)
					sdir += 4;
				
				if(!sprite.isAnimation(sdir * 2, sdir * 2 + 1, 12))
					sprite.setAnimation(sdir * 2, sdir * 2 + 1, 12);
			} else {
				((Living)attack).damage(2);
				
				double angle = pos.angleTo(attack.getPosition());
				angle = Math.toDegrees(angle);
				angle -= 270 - 45;
				while(angle < 0)
					angle += 360;
				
				int sdir = (int) ((angle / 90));
				if(sdir < 0)
					sdir += 4;
				
				if(!sprite.isAnimation(sdir * 2, sdir * 2 + 1, 12))
					sprite.setAnimation(sdir * 2, sdir * 2 + 1, 12);
			}
		}
		
		sprite.update();
	}
	
	@Override
	public void damage(int a) {
		health -= a;
	}
	
	@Override
	public void render(Renderer renderer) {
		if(!inventory) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 15, pos.y - 22), true);
		}
			
		if(held) {
			Vector2D pvel = new Vector2D(Keyboard.x + map.getCamera().pos.x, Keyboard.y + map.getCamera().pos.y);
			
			this.pos = pvel.copy();
			
			if(map.isFree(pvel, 15)) {
				renderer.drawSprite(sprite, new Vector2D(pos.x - 15, pos.y - 22), true);
			} else {
				float[] scales = {1f, 0f, 0f, 0.8f};
			    float[] offsets = new float[4];
			    RescaleOp rop = new RescaleOp(scales, offsets, null);
			    renderer.drawSprite(sprite, new Vector2D(pos.x - 15, pos.y - 22), rop);
			}
		}
	}
	
	@Override
	public void drop(int chance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCollision(Entity e) {
		ControlPoint cp = map.getRandomControlPoint();
		if(cp != null)
			next_pos = cp.getCloseTo(70);
		else
			next_pos = new Vector2D(rand.nextInt(6 * 32 + 55 * 32), rand.nextInt(6 * 32 + 55 * 32));// TODO Auto-generated method stub
	}

	@Override
	public int onItemUse() {
		Vector2D pvel = new Vector2D(Keyboard.x + map.getCamera().pos.x, Keyboard.y + map.getCamera().pos.y);
		
		if(map.isFree(pvel, 15)) {
			Sounds.play(new Sound.Place());
			this.pos = pvel.copy();
			map.getPlayer().getInventory().useItem(ItemType.AGC);
			map.spawn(new AGC(null, this.pos.copy()));
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
		return 5;
	}
	
}
