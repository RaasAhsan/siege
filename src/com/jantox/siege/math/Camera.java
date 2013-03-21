package com.jantox.siege.math;

import com.jantox.siege.DungeonGame;

public class Camera {
	
	public Vector2D pos;
	public Vector2D sdim;
	
	private Vector2D lastpos;
	
	public Vector2D interpos;
	
	public Camera(Vector2D pos, Vector2D sdim) {
		this.pos = pos.copy();
		this.interpos = pos.copy();
		this.sdim = sdim;
	}
	
	public void update(Vector2D pos) {
		this.pos.x = (pos.x - sdim.x / 2);
	    this.pos.y = (pos.y - sdim.y / 2);
	    
	    lastpos = pos.copy();
	    
	    this.interpos.x = (pos.x - lastpos.x) * DungeonGame.interp + lastpos.x - sdim.x / 2;
	    this.interpos.y = (pos.y - lastpos.y) * DungeonGame.interp + lastpos.y - sdim.y / 2;
	}

}
