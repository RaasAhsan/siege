package com.jantox.dungmast.entities;

import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jantox.dungmast.Item;
import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.Sprite;
import com.jantox.dungmast.UserInput;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.entities.Inventory.ItemType;
import com.jantox.dungmast.math.Vector2D;

public class SentryGun extends Living implements Item {
	
	int direction;
	
	private Player player;
	
	private boolean inventory = true;
	private boolean held = false;
	
	public SentryGun(Player p, Vector2D pos) {
		super(pos);
		
		this.health = 10;
		this.colmask = new Circle(pos, 15);
		
		try {
			this.sprite = new Sprite(32, 32, ImageIO.read(this.getClass().getResourceAsStream("/res/sentry_gun.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		direction = 0;
		
		sprite.setAnimation(0,7,5);
		
		if(player != null)
			this.player = p;
		else
			inventory = false;
	}

	@Override
	public void update() {
		super.update();
		if(!inventory) {
			Entity e = map.getClosestMonster(this);
			if(e != null) {
				if(distanceSquared(e) < 35 * 35) {
					if(ticks % 20 == 0) {
						((Living)e).damage(5);
					}
					
					double angle = Math.toDegrees(e.pos.angleTo(this.getPosition()));
					
					if(angle > -22.5 && angle <= 22.5) {
						sprite.setAnimation(6, 6, 0);
					} else if(angle > 22.5 && angle <= 67.5) {
						sprite.setAnimation(5, 5, 0);
					} else if(angle > 67.5 && angle <= 112.5) {
						sprite.setAnimation(4, 4, 0);
					} else if(angle > 112.5 && angle <= 157.5) {
						sprite.setAnimation(3, 3, 0);
					} else if(angle > 157.5 && angle <= -157.5) {
						sprite.setAnimation(2, 2, 0);
					} else if(angle > -157.5 && angle <= -112.5) {
						sprite.setAnimation(1, 1, 0);
					} else if(angle > -112.5 && angle <= -67.5) {
						sprite.setAnimation(0, 0, 0);
					} else if(angle > -67.5 && angle <= -22.5) {
						sprite.setAnimation(7, 7, 0);
					}
				} else {
					if(!sprite.isAnimation(0, 7, 8))
						sprite.setAnimation(0, 7, 8);
				}
			} else {
				if(!sprite.isAnimation(7, 0, 8))
					sprite.setAnimation(7, 0, 8);
			}
		}
		
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		if(!inventory)
			renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 27), true);
		
		if(held) {
			Vector2D pvel = new Vector2D(UserInput.x + map.getCamera().pos.x, UserInput.y + map.getCamera().pos.y);
			
			this.pos = pvel.copy();
			
			if(map.isFree(pvel, 15)) {
				renderer.drawSprite(sprite, new Vector2D(pvel.x - 16, pvel.y - 27), true);
			} else {
				float[] scales = {1f, 0f, 0f, 0.8f};
			    float[] offsets = new float[4];
			    RescaleOp rop = new RescaleOp(scales, offsets, null);
			    renderer.drawSprite(sprite, new Vector2D(pvel.x - 16, pvel.y - 27), rop);
			}
		}
	}
	
	@Override
	public void damage(int a) {
		
	}
	
	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public void drop(int chance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int onItemUse() {
		Vector2D pvel = new Vector2D(UserInput.x + map.getCamera().pos.x, UserInput.y + map.getCamera().pos.y);
		
		if(map.isFree(pvel, 5)) {
			this.pos = pvel.copy();
			map.getPlayer().getInventory().useItem(ItemType.SENTRY_GUN);
			map.spawn(new SentryGun(null, this.pos.copy()));
			held = false;
		}
		
		return 0;
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
