package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	private TextureRegion[][] spriteSheet;

	public Particle(int x, int y) {
		super(x, y, Art.particle[0][0]);
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.airDirection();

		x += offset.x;
		y += offset.y;
	}
}