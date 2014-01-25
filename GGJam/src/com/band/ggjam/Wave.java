package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	private TextureRegion[][] spriteSheet;

	public Wave(int y) {
		super(0, y, Art.wave[0][0]);
		spriteSheet = Art.wave;
	}
}
