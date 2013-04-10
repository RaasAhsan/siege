package com.jantox.siege.entities;

import com.jantox.siege.Item;
import com.jantox.siege.PotionEffect;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Potion extends Entity implements Item {

	public static final int POTION_HEALTH = 0;
	public static final int POTION_SWIFT = 1;
	public static final int POTION_STRENGTH = 2;
	public static final int POTION_WEALTH = 3;
	
	private int type;
	private Player p;
 	
	public Potion(Player player, int type) {
		super(new Vector2D());
		
		this.p = player;
		this.type = type;
	}
	
	@Override
	public void render(Renderer renderer) {
		
	}

	@Override
	public void handleCollision(Entity e) {
		
	}

	@Override
	public int onItemUse() {
		//Sounds.play(new Sound.Drink());
		//p.peffects.add(new PotionEffect(pid, ))
		this.map.getPlayer().peffects.add(new PotionEffect(this.type));
		return 0;
	}

	@Override
	public void onHeld() {
		
	}

	@Override
	public void onNotHeld() {
		
	}

	@Override
	public int getRest() {
		return 60;
	}
	
	public int getType() {
		return type;
	}

}
