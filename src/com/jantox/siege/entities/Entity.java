package com.jantox.siege.entities;

import java.util.ArrayList;
import java.util.Random;

import com.jantox.siege.Active;
import com.jantox.siege.Map;
import com.jantox.siege.colsys.CollisionMask;
import com.jantox.siege.colsys.CollisionOwner;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.Spawner;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;

public abstract class Entity implements Active, CollisionOwner {

	public enum entity_type { ALL, PLAYER, TREE, SHOP, ZOMBIE, FENCE, GATE, SENTRY_GUN, CONTROL_POINT, BARRICADE, SKELETON, AGC, SPAWNER };
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	public static int ticks;
	public static Map map;
	public static Random rand = new Random(System.currentTimeMillis() * System.nanoTime());
	
	public Vector2D pos;
	protected int status;
	protected int direction;
	protected entity_type type;
	
	protected Sprite sprite;
	protected CollisionMask colmask;
	
	protected boolean expired = false;
	
	private ArrayList<entity_type> reqcol;
	
	public Entity(Vector2D pos) {
		if(pos != null)
			this.pos = new Vector2D(pos.x, pos.y);
		else
			this.pos = new Vector2D();
		status = direction = 0;
		
		if(this instanceof Player) {
			type = entity_type.PLAYER;
		} else if(this instanceof Tree) {
			type = entity_type.TREE;
		} else if(this instanceof Zombie) {
			type = entity_type.ZOMBIE;
		} else if(this instanceof Fence) {
			type = entity_type.FENCE;
		} else if(this instanceof Gate) {
			type = entity_type.GATE;
		} else if(this instanceof SentryGun) {
			type = entity_type.SENTRY_GUN;
		} else if(this instanceof ControlPoint) {
			type = entity_type.CONTROL_POINT;
		} else if(this instanceof Barricade) {
			type = entity_type.BARRICADE;
		} else if(this instanceof Skeleton) {
			type = entity_type.SKELETON;
		} else if(this instanceof AGC) {
			type = entity_type.AGC;
		} else if(this instanceof Spawner) {
			type = entity_type.SPAWNER;
		}
	}
	
	public void update() {
		if(colmask != null)
			colmask.update(pos);
		
	}
	
	public Vector2D getPosition() {
		return pos;
	}
	
	public void setPosition(Vector2D v) {
		this.pos = v;
	}
	
	public entity_type getEntityType() {
		return type;
	}
	
	public double getDepth() {
		return -pos.y;
	}
	
	public boolean isVisibleToCamera() {
		return false;
	}
	
	public boolean isExpired() {
		return expired;
	}
	
	public double distanceSquared(Entity e) {
		return pos.distanceSquared(e.getPosition());
	}
	
	public boolean requests() {
		return reqcol != null && reqcol.size() > 0;
	}
	
	public void requestCollisions(entity_type... types) {
		reqcol = new ArrayList<entity_type>();
		for(int i = 0; i < types.length; i++) {
			reqcol.add(types[i]);
		}
	}

	public ArrayList<entity_type> getRequestedCollisions() {
		return reqcol;
	}
	
	public Vector2D getCloseTo(float radius) {
		Vector2D sp = pos.copy();
		sp.x += rand.nextGaussian() * radius;
		sp.y += rand.nextGaussian() * radius;
		return sp;
	}
	
	public CollisionMask getMask() {
		return colmask;
	}

	public int getDirection() {
		return direction;
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
