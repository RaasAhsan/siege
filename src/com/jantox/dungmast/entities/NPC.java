package com.jantox.dungmast.entities;

import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.Store;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class NPC extends Entity {

	public static final int ELI = 0;
	public static final int BRENDAN = 1;
	
	private int npc;
	private Store store;
	
	public NPC(Vector2D pos, int npc) {
		super(pos);
		
		this.npc = npc;
		
		if(npc == ELI) {
			this.colmask = new Circle(this.pos, 15);
			this.sprite = Assets.loadSprite("blacksmith.png");
		} else if(npc == BRENDAN) {
			this.colmask = new Circle(this.pos, 15);
			this.sprite = Assets.loadSprite("magician.png");
		}
	}

	@Override
	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Renderer renderer) {
		if(npc == ELI) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 31, pos.y - 58), true);
		} else if(npc == BRENDAN) {
			renderer.drawSprite(sprite, new Vector2D(pos.x - 33, pos.y - 60), true);
		}
	}

}
