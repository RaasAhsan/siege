package com.jantox.dungmast;

import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class Store {
	
	private String name;
	private Sprite store;
	
	public Store(String name) {
		this.name = name;
		
		this.store = Assets.loadSprite("interface_store.png");
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		renderer.drawSprite(store, new Vector2D(50, 50), false);
	}
	
	public String getShopName() {
		return name;
	}
	
}
