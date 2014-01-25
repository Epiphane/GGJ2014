package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	private TextureRegion[][] spriteSheet;

	public Wave(int x, int y) {
		super(x, y, Art.wave[0][0]);
		spriteSheet = Art.wave;
	}
	
	public void tick(Input input) {
		super.tick();
		
		// TODO: anything, really
	}
}
