package com.jantox.siege.colsys;

import com.jantox.siege.math.Vector2D;

public abstract class CollisionMask {
	
	public static final int COLMASK_SP = 0;
	public static final int COLMASK_SQ = 1;
	
	private int cid;
	protected Vector2D pos;
	
	public CollisionMask(Vector2D pos) {
		this.pos = pos.copy();
		if(this instanceof Circle) {
			cid = COLMASK_SP;
		} else if(this instanceof AABB) {
			cid = COLMASK_SQ;
		}
	}
	
	public void update(Vector2D pos) {
		this.pos = pos.copy();
	}
	
	public Vector2D getPosition() {
		return pos;
	}
	
	public int getMaskType() {
		return cid;
	}

}
