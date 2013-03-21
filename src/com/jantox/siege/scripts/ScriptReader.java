package com.jantox.siege.scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.jantox.siege.Map;
import com.jantox.siege.colsys.Line;
import com.jantox.siege.entities.Barricade;
import com.jantox.siege.entities.ControlPoint;
import com.jantox.siege.entities.Decoration;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Fence;
import com.jantox.siege.entities.Gate;
import com.jantox.siege.entities.Spawner;
import com.jantox.siege.gfx.Sprite.Animation;
import com.jantox.siege.math.Vector2D;

public class ScriptReader {

	public Map readMapScript(String mapfn) throws NumberFormatException, IOException {
		Map map = new Map();
		Entity.map = map;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/maps/" + mapfn), "UTF-8"));
		
		String command = null;
		int line = 0;
		
		boolean tm = false;
		
		ArrayList<String> tilemap = new ArrayList<String>();
		
		while((command = reader.readLine()) != null) {
			// parses everything
			String[] params = command.split(" ");
			
			if(tm == false) {
				if(!command.startsWith("#")) {
					if(params[0].equals("seed")) {
						map.seed(Integer.valueOf(params[1]));
					} else if(params[0].equals("tmstart")) {
						tm = true;
					} else {
						//if(!command.startsWith(" ") && command.length() > 0)
							//System.out.println("Unknown command found in map script file " + mapfn + " at line " + line + "!");
					}
				}
			} else {
				if(params[0].equals("tmend")) {
					tm = false;
					continue;
				}
				tilemap.add(command);
			}
			line++;
		}
		
		map.setSize(new Vector2D(tilemap.get(0).length() * 32, tilemap.size() * 32));
		
		int tx = 0, ty = 0;
		for(int i = 0; i < tilemap.size(); i++) {
			String comm = tilemap.get(i);
			for(int j = 0; j < comm.length(); j++) {
				char c = comm.charAt(j);
				if(c == '+') {
					int anim = 0;
					
					char up = 0, down = 0, left = 0, right = 0;
					if(i > 0) {
						up = tilemap.get(i-1).charAt(j);
					}
					if(i < tilemap.size() - 1) {
						down = tilemap.get(i+1).charAt(j);
					}
					if(j > 0) {
						left = tilemap.get(i).charAt(j - 1);
					}
					if(j < comm.length() - 1) {
						right = tilemap.get(i).charAt(j + 1);
					}
					
					if(left == '+' && right == '+') {
						anim = 0;
					} else if(up == '+' && down == '+') {
						anim = 1;
					} else if(right == '+' && down == '+') {
						anim = 2;
					} else if(left == '+' && down == '+') {
						anim = 3;
					} else if(left == '+' && up == '+') {
						anim = 5;
					} else if(right == '+' && up == '+') {
						anim = 4;
					} else if(right == 'g') {
						anim = 6;
					} else if(left == 'g') {
						anim = 7;
					}
					
					map.spawn(new Fence(new Vector2D(tx * 32, ty * 32), anim));
				} else if(c == 'f') {
					map.spawnResource("forest", new Vector2D(tx * 32, ty * 32), 45);
				} else if(c == 'g') {
					map.spawn(new Gate(new Vector2D(tx * 32, ty * 32)));
				} else if(c == 's') {
					map.spawn(new Spawner(new Vector2D(tx * 32, ty * 32)));
				} else if(c == 'd') {
					map.spawnForge(new Vector2D(tx * 32, ty * 32));
				} else if(c == 'a') {
					map.spawnArmory(new Vector2D(tx * 32, ty * 32));
				} else if(c == 'c') {
					map.spawn(new ControlPoint(0, new Vector2D(tx * 32, ty * 32)));
				} else if(c == 'b') {
					map.spawn(new Barricade(null, new Vector2D(tx * 32, ty * 32)));
				} else if(c == 'w') {
					map.spawn(new Decoration(new Vector2D(tx * 32, ty * 32), Decoration.FOOTPATH, 1203123));
				} else if(c == 'p') {
					map.pspawn = new Vector2D(tx * 32, ty * 32);
				} else if(c == 'e') {
					map.spawn(new Decoration(new Vector2D(tx * 32, ty * 32), Decoration.MARKET, 0));
				}
				tx++;
			}
			ty++;
			tx = 0;
		}
		
		reader.close();
		
		return map;
	}
	
	public SpriteScript readSpriteScript(String sfile) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/sprites/" + sfile), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String command = null;
		int line = 0;
		
		String gfxfile = null;
		int width = 0, height = 0;
		ArrayList<Animation> ala = new ArrayList<Animation>();

		try {
			while ((command = reader.readLine()) != null) {
				String[] params = command.split(" ");
	
				if (!command.startsWith("#")) {
					if (params[0].equals("gfx")) {
						gfxfile = params[1];
					} else if (params[0].equals("size")) {
						width = Integer.valueOf(params[1].split(",")[0]);
						height = Integer.valueOf(params[1].split(",")[1]);
					} else {
						//if (!command.startsWith(" ") && command.length() > 0)
							//System.out.println("Unknown command found in sprite script file " + sfile + " at line " + line + "!");
					}
				}
				line++;
			}
	
			reader.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return new SpriteScript(gfxfile, width, height, ala);
	}
	
	/*public MonsterScript readMonsterScript(String mfn) {
		String type, gfxfile;
		double speed, damage;
		int health;
		
		String command;
		try {
			while ((command = reader.readLine()) != null) {
				String[] params = command.split(" ");
	
				if (!command.startsWith("#")) {
					if (params[0].equals("gfx")) {
						gfxfile = params[1];
					} else if (params[0].equals("size")) {
						width = Integer.valueOf(params[1].split(",")[0]);
						height = Integer.valueOf(params[1].split(",")[1]);
					} else {
						if (!command.startsWith(" ") && command.length() > 0)
							System.out.println("Unknown command found in sprite script file " + sfile + " at line " + line + "!");
					}
				}
				line++;
			}
	
			reader.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return new MonsterScript(type, gfxfile, health, speed, damage, null);
	}*/
	
}
