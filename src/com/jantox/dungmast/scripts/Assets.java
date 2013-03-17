package com.jantox.dungmast.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.jantox.dungmast.Sprite;

public class Assets {
	
	private static Assets assets;

	private ArrayList<MonsterScript> monsters;
	private ArrayList<SpriteScript> sprites;
	
	public Assets() {
		this.loadResources();
	}
	
	private void loadResources() {
		monsters = new ArrayList<MonsterScript>();
		sprites = new ArrayList<SpriteScript>();
		
		/*File[] ffs = Assets.getFilesInDirectory(new File("src/sprites/"));
		for(int i = 0; i < ffs.length; i++) {
			System.out.println("/sprites/" + ffs[i].getName());
		}*/
		
		ScriptReader reader = new ScriptReader();
		
		System.out.println("Loading sprite files...");
		String[] spritefs = Assets.listLines(this.getClass().getResourceAsStream("/sprites.conf"));
		for(int i = 0; i < spritefs.length; i++) {
			sprites.add(reader.readSpriteScript(spritefs[i]));
		}
		
		System.out.println("Loading monster files...");
		String[] monsterfs = Assets.listLines(this.getClass().getResourceAsStream("/monsters.conf"));
		for(int i = 0; i < monsterfs.length; i++) {
			//monsters.add(reader.readMonsterScript(monsterfs[i]));
		}
	}
	
	public static void init() {
		assets = new Assets();
	}
	
	public static File[] getFilesInDirectory(File f) {
		return f.listFiles();
	}
	
	public static String[] listLines(InputStream inputStream) {
		ArrayList<String> als = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				als.add(line);
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return als.toArray(new String[als.size()]);
	}
	
	public SpriteScript getSprite(String ident) {
		for(SpriteScript ss : sprites) {
			if(ss.getIdentifier().equals(ident)) {
				return ss;
			}
		}
		return null;
	}
	
	public static Sprite loadSprite(String ident) {
		return assets.getSprite(ident).compile();
	}
	
}
