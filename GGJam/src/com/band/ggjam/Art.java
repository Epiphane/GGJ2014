package com.band.ggjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {
	
	public final static int DIRECTIONS = 4;
	
	public static TextureRegion[][] particle;
	public static TextureRegion[][] wave;
	public static TextureRegion[][] tiles;
	public static byte[][] mainCharacterMap;
	
	public static boolean loaded = false;
	
	public static void load () {
		particle = split("img/DAHT.png", 16, 16);
		wave = split("img/WAIV.png", 16, 16);
		tiles = split("img/tiles.png", 16, 16);
		
		loaded = true;
	}

	public static TextureRegion load (String name, int width, int height) {
		Texture texture = new Texture(Gdx.files.internal(name));
		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);
		region.flip(false, false);
		return region;
	}

	private static TextureRegion[][] split (String name, int width, int height) {
		return split(name, width, height, false);
	}

	private static TextureRegion[][] split (String name, int width, int height, boolean flipX) {
		Texture texture = new Texture(Gdx.files.internal(name));
		int xSlices = texture.getWidth() / width;
		int ySlices = texture.getHeight() / height;
		TextureRegion[][] res = new TextureRegion[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				res[x][y] = new TextureRegion(texture, x * width, y * height, width, height);
				res[x][y].flip(flipX, false);
			}
		}
		return res;
	}
}

