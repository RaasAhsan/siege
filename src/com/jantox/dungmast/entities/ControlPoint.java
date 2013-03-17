package com.jantox.dungmast.entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jantox.dungmast.Map;
import com.jantox.dungmast.Renderer;
import com.jantox.dungmast.colsys.Circle;
import com.jantox.dungmast.colsys.CollisionSystem;
import com.jantox.dungmast.math.Vector2D;
import com.jantox.dungmast.scripts.Assets;

public class ControlPoint extends Entity {
	
	int id;
	int owner = 0;
	int ownership = 10000; // 0 for enemy, 10000 for us
	
	int pbreak = 0;

	public ControlPoint(int id, Vector2D pos) {
		super(pos);
		this.id = id;
		
		this.colmask = new Circle(pos, 50);
		
		this.requestCollisions(entity_type.PLAYER);
		
		this.sprite = Assets.loadSprite("controlpoint.png");
	}

	@Override
	public void handleCollision(Entity e) {
		//Map.currentcp = this;
	}
	
	public void update() {
		List<Entity> ale = map.getEntities();
		boolean monster = false, player = false;
		for(Entity ent : ale) {
			if(CollisionSystem.collides(this.getMask(), ent.getMask())) {
				if(ent.getEntityType() == entity_type.ZOMBIE || ent.getEntityType() == entity_type.SKELETON) {
					monster = true;
				} else if(ent.getEntityType() == entity_type.PLAYER) {
					player = true;
				}
			}
		}
		
		if(pbreak > 0)
			pbreak--;
		
		if(owner == 0) {
			if(ownership == 0) 
				owner = 1;
			if(ownership < 10000 && ownership > 0 && monster == false) {
				ownership = 10000;
			}
		} else if(owner == 1) {
			if(ownership == 10000)
				owner = 0;
			if(ownership < 10000 && ownership > 0 && player == false) {
				ownership = 0;
			}
		} else if(owner == -1) {
			
		}
		
		for(Entity ent : ale) {
			if(CollisionSystem.collides(this.getMask(), ent.getMask())) {
				if(ent.getEntityType() == entity_type.ZOMBIE || ent.getEntityType() == entity_type.SKELETON) {
					if(!player) {
						if(owner == 0) {
							if(ownership > 0) {
								if(pbreak == 0) {
									ownership-=8;
									pbreak = 1;
								}
							}
						} else if(owner == -1) {
							if(ownership > 0) {
								if(pbreak == 0) {
									ownership-=8;
									pbreak = 1;
								}
							}
						}
					}
				} else if(ent.getEntityType() == entity_type.PLAYER) {
					if(!monster) {
						if(owner == 1) {
							if(ownership < 10000) {
								if(pbreak == 0) {
									ownership+=8;
									pbreak = 1;
								}
							} 
						} else if(owner == -1) {
							if(ownership > 0) {
								if(pbreak == 0) {
									ownership-=8;
									pbreak = 1;
								}
							}
						}
					}
				}
			}
		}
		
		sprite.setAnimation(owner, owner, 0);
		sprite.update();
	}

	@Override
	public void render(Renderer renderer) {
		renderer.drawSprite(sprite, new Vector2D(pos.x - 128, pos.y - 128), true);
	}
	
	public void renderProgress(Renderer renderer) {
		renderer.setColor(Color.RED);
		renderer.drawText("" + ownership, new Vector2D(50, 50));
	}

	public int getOwnership() {
		return ownership;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int i) {
		this.owner = i;
		if(owner == 1) {
			ownership = 0;
		} else if(owner == -1) {
			ownership = 10000;
		}
	}

}
