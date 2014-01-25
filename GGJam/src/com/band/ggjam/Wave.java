package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	private TextureRegion[][] spriteSheet;

	public Wave(int x, int y, Level level) {
		super(x, y, Art.wave[0][0], level);
		spriteSheet = Art.wave;
	}
	
	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.airDirection();

		x += offset.x;
		y += offset.y;
	}
}
