package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	private TextureRegion[][] spriteSheet;

	public Particle(int x, int y, Level level) {
		super(x, y, Art.particle[0][0], level);
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.airDirection();

//		System.out.println(offset.x+", "+offset.y);
		tryMove(offset.x, offset.y);
//		x += offset.x;
//		y += offset.y;
	}
}