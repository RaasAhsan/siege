package com.jantox.siege;

import java.util.ArrayList;
import java.util.List;

import com.jantox.siege.gfx.Renderer;

public class ActiveList implements Active {
	
	private List<Active> actives;

	public ActiveList() {
		actives = new ArrayList<Active>();
	}
	
	public void addActive(int list, Active a) {
		if(list == -1) {
			actives.add(a);
		} else {
			((ActiveList)actives.get(list)).addActive(-1, a);
		}
	}
	
	@Override
	public void update() {
		for(Active a : actives) {
			a.update();
		}
	}

	@Override
	public void render(Renderer renderer) {
		for(Active a : actives) {
			a.render(renderer);
		}
	}

}
