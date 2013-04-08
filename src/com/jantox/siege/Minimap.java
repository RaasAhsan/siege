package com.jantox.siege;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Decoration;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Fence;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Gem;
import com.jantox.siege.entities.Player;
import com.jantox.siege.entities.Projectile;
import com.jantox.siege.entities.Tree;
import com.jantox.siege.entities.drones.AGC;
import com.jantox.siege.entities.drones.Barricade;
import com.jantox.siege.entities.drones.SentryGun;
import com.jantox.siege.entities.monsters.Skeleton;
import com.jantox.siege.entities.monsters.MonsterFactory;
import com.jantox.siege.entities.monsters.Zombie;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.gfx.Renderer;
import com.jantox.siege.gfx.Sprite;
import com.jantox.siege.math.Vector2D;
import com.jantox.siege.scripts.Assets;

public class Minimap {

	private Vector2D pos;
	private Map map;
	
	private BitmapFont numbers;
	private Sprite icons;
	
	public Minimap(Map map) {
		this.map = map;
		
		this.pos = new Vector2D(535, 15);
		icons = Assets.loadSprite("cmap_icons.png");
		
		numbers = new BitmapFont();
	}
	
	public void update() {
		
	}
	
	public void render(Renderer renderer) {
		renderer.setColor(new Color(76, 150, 44));
		renderer.fillCircle(pos, new Vector2D(150, 150));
		
		Vector2D ppos = new Vector2D(pos.getX() + 75, pos.getY() + 75);
		
		Player p = map.getPlayer();
		List<Entity> entities = map.getEntities();
		
		for (Entity e : entities) {
			Vector2D to = e.pos.copy();
			to.subtract(p.getPosition());

			to.normalize();
			to.multiply(Math.sqrt(e.distanceSquared(p)) / 5);

			to.add(ppos);

			if (e instanceof AGC) {
				icons.setAnimation(1, 1, 0);
			} else if (e instanceof SentryGun) {
				icons.setAnimation(2, 2, 0);
			} else if (e instanceof MonsterFactory) {
				icons.setAnimation(4, 4, 0);
			} else if (e instanceof Fence) {
				icons.setAnimation(6, 6, 0);
			} else if (e instanceof Barricade) {
				icons.setAnimation(0, 0, 0);
			} else if (e instanceof Tree) {
				icons.setAnimation(5, 5, 0);
			} else if (e instanceof ControlPoint) {
				int a = ((ControlPoint)e).getOwner() + 8;
				icons.setAnimation(a, a, 0);
			} else if (e instanceof Decoration) {
				if (((Decoration) e).getType() == Decoration.FOOTPATH)
					icons.setAnimation(7, 7, 0);
				else
					continue;
			} else if (e instanceof Skeleton || e instanceof Zombie) {
				if (to.distanceSquared(ppos) < 65 * 65) {
					renderer.setColor(Color.RED);
					renderer.fill(new Rectangle(to.getX() - 1, to.getY() - 1,
							3, 3), false);
				}
				continue;
			} else {
				continue;
			}
			icons.update();

			if (to.distanceSquared(ppos) < 65 * 65) {
				renderer.drawSprite(icons, new Vector2D(to.x - 16, to.y - 16),
						false);
			} else {
				if (e instanceof MonsterFactory) {
					to = e.pos.copy();
					to.subtract(p.getPosition());

					to.normalize();
					to.multiply(75);

					to.add(ppos);

					renderer.drawSprite(icons, new Vector2D(to.x - 16,
							to.y - 16), false);
				}
			}
		}
		
		renderer.setColor(Color.WHITE);
		renderer.fillRect(ppos.getX() - 2, ppos.getY() - 2, 4, 4);
	}
	
}
