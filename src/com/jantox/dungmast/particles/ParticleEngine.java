package com.jantox.dungmast.particles;

import java.util.ArrayList;

import com.jantox.dungmast.Renderer;

public class ParticleEngine {
	
	private ArrayList<Particle> particles;
	
	public ParticleEngine() {
		particles = new ArrayList<Particle>();
	}

	public void add(Particle e) {
		particles.add(e);
	}

	public void update() {
		for(int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if(p.isExpired()) {
				particles.remove(p);
			} else {
				p.update();
			}
		}
	}
	
	public void render(Renderer renderer) {
		for(Particle p : particles) {
			p.render(renderer);
		}
	}
	
}
