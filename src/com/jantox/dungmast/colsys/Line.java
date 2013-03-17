package com.jantox.dungmast.colsys;

import java.util.Random;

import com.jantox.dungmast.math.Vector2D;

public class Line {
	
	public Vector2D a, b;
	
	static Random rand = new Random();
	
	public Line(Vector2D av, Vector2D bv) {
		this.a = av;
		this.b = bv;
	}
	
	public Vector2D project(Vector2D pj) {
		Vector2D hp = new Vector2D(pj.x - a.x, pj.y - a.y);
		
		Vector2D ab = new Vector2D(b.x - a.x, b.y - a.y);
		Vector2D abn = ab.copy();
		abn.normalize();
		
		double proj = hp.dotProduct(abn);
		if(proj < 0)
			return a.copy();
		else if(proj > ab.length())
			return b.copy();
		
		Vector2D prj = new Vector2D(a.x + proj * abn.x, a.y + proj * abn.y);
		
		return prj;
	}

}
