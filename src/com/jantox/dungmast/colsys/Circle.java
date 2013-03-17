package com.jantox.dungmast.colsys;

import com.jantox.dungmast.math.Vector2D;

public class Circle extends CollisionMask {
	
	private float radius;

	public Circle(Vector2D pos, float radius) {
		super(pos);
		this.radius = radius;
	}
	
	public float getRadius() {
		return radius;
	}

}
