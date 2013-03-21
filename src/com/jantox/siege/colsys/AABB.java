package com.jantox.siege.colsys;

import com.jantox.siege.math.Vector2D;

public class AABB extends CollisionMask {
	
	float r[]; // the half-measures
	
	public AABB(Vector2D pos, float a, float b) {
		super(pos);
		r = new float[2];
		r[0] = a;
		r[1] = b;
	}
	
	public float[] getMeasures() {
		return r;
	}

}
