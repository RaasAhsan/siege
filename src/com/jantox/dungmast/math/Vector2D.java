package com.jantox.dungmast.math;

import java.util.Random;

public class Vector2D {

	public double x, y;
	
	public static Random random = new Random();
	
	public Vector2D() {
		x = y = 0;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public double dotProduct(Vector2D b) {
		return x * b.x + y * b.y;
	}
	
	public double distanceSquared(Vector2D b) {
		return Math.pow(b.x - x, 2) + Math.pow(b.y - y, 2);
	}
	
	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public void normalize() {
		double length = this.length();
		x /= length;
		y /= length;
	}
	
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	public void add(Vector2D b) {
		x += b.x;
		y += b.y;
	}
	
	public void subtract(Vector2D b) {
		x -= b.x;
		y -= b.y;
	}
	
	public void multiply(double d) {
		x *= d;
		y *= d;
	}
	
	public Vector2D getPositionCloseTo(float radius) {
		Vector2D spos = this.copy();
		spos.x += random.nextGaussian() * radius;
		spos.y += random.nextGaussian() * radius;
		
		return spos;
	}
	
	public double angleTo(Vector2D pos) {
		return Math.atan2(pos.y - y, pos.x - x);
	}
	
}
