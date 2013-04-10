package com.jantox.siege;

import com.jantox.siege.Store.ItemType;
import com.jantox.siege.gfx.Renderer;

public class UpgradeItem implements Item {
	
	private ItemType type;

	public UpgradeItem(ItemType type) {
		this.type = type;
	}
	
	public void upgrade() {
		
	}
	
	public ItemType getType() {
		return type;
	}
	
	@Override
	public int onItemUse() {
		return 0;
	}

	@Override
	public void onHeld() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotHeld() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Renderer renderer) {
		
	}

	@Override
	public int getRest() {
		return 0;
	}

	
}
