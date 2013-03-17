package com.jantox.dungmast.entities;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.colsys.AABB;
import com.jantox.dungmast.colsys.CollisionMask;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Decoration extends Entity {
	
	public static final int COLBOX = 0;
	public static final int ANVIL = 1;
	public static final int CAULDRON = 2;
	public static final int FOOTPATH = 3;
	public static final int MARKET = 4;

	int type;
	
	int depth;
	
	public Decoration(CollisionMask cm) {
		super(cm.getPosition());
		this.type = COLBOX;
		
		this.colmask = cm;
	}
	
	public Decoration(Vector2D pos, int type, int depth) {
		super(pos);
		this.type = type;
		
		this.depth = depth;
		
		if(type == ANVIL) {
			this.colmask = new AABB(this.pos, 32, 20);
			
			this.sprite = Assets.loadSprite("anvil.png");
		} else if(type == CAULDRON) {
			this.colmask = new AABB(this.pos, 32, 20);
			
			this.sprite = Assets.loadSprite("cauldron.png");
			int r = rand.nextInt(2);
			sprite.setAnimation(r,r,0);
			sprite.update();
		} else if(type == FOOTPATH) {
			this.sprite = Assets.loadSprite("footpath.png");
		} else if(type == MARKET) {
			this.sprite = Assets.loadSprite("market.png");
			
			CollisionMask a = new AABB(new Vector2D(pos.x - 166 + 16, pos.y - 23 + 256/2), 32, 230);
			CollisionMask b = new AABB(new Vector2D(pos.x + 166 - 16, pos.y - 23 + 256/2), 32, 230);
			CollisionMask c = new AABB(new Vector2D(pos.x, pos.y - 44/2), 288, 44);
			
			map.spawn(new Decoration(a));
			map.spawn(new Decoration(b));
			map.spawn(new Decoration(c));
		}
	}
	
	public double getDepth() {
		if(depth == 0) {
			return -this.pos.getY();
		}
		return depth;
	}

	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public void render(Renderer renderer) {
		if(type == ANVIL) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 33, pos.y - 45), true);
		} else if(type == CAULDRON) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 33, pos.y - 45), true);
		} else if(type == FOOTPATH) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 16, pos.y - 16), true);
		} else if(type == MARKET) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 240, pos.y - 288/2 + 32 + 32 + 16), true);
		}
	}

	public int getType() {
		return type;
	}

}
