package com.jantox.siege;

import com.jantox.siege.gfx.Renderer;


public interface Item {

	public int onItemUse();
	
	public void onHeld();
	
	public void onNotHeld();
	
	public void update();
	
	public void render(Renderer renderer);
	
	public int getRest();
	
}
