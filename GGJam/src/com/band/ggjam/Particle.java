package com.band.ggjam;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	public static float PARTICLE_SPEED = 5;
	public static float PARTICLE_ACCEL = 1f;
	
	private TextureRegion[][] spriteSheet;
	
	private float h;

	public Particle(int x, int y, Level level) {
		super(x, y, Art.particle[0][0], level);
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.walkDirection();

		tryMove(offset.x *= PARTICLE_SPEED, offset.y *= PARTICLE_SPEED);
		
		setRotation(getRotation() + 1);

		h += 0.01f;
		if(h > 1.0f) h = 0f;
		setColor(new Color(Utility.RGBfromHSV(h, 0.25f, 1)));
	}
}