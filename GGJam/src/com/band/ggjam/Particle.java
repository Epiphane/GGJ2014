package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	public static int PARTICLE_SPEED = 5;
	
	private TextureRegion[][] spriteSheet;

	public Particle(int x, int y, Level level) {
		super(x, y, Art.particle[0][0], level);
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.airDirection();

		tryMove(offset.x * PARTICLE_SPEED, offset.y * PARTICLE_SPEED);
	}
}