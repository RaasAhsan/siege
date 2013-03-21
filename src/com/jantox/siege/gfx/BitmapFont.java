package com.jantox.siege.gfx;

import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class BitmapFont {

	private Sprite sprite;
	
	public BitmapFont() {
		sprite = Assets.loadSprite("font_number.png");
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer, Vector2D pos, String str) {
		int rx = 0;
		for(int i = 0; i < str.length(); i++) {
			int f = Integer.valueOf(str.substring(i, i+1));
			sprite.setAnimation(f, f, 0);
			sprite.update();
			renderer.drawSprite(sprite, new Vector2D(pos.x + rx, pos.y), false);
		}
	}
	
}
