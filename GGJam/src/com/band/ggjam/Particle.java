package com.band.ggjam;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle extends Entity {
	public static float PARTICLE_SPEED = 5;
	public static float PARTICLE_ACCEL = 1f;
	
	private TextureRegion[][] spriteSheet;
	
	private float h;
	private float ox, oy;
	
	private float width, height, scale;

	public Particle(int x, int y, Level level) {
		super(x, y, Art.particle[0][0], level);
		
		ox = x;
		oy = y;
		
		width = getWidth();
		height = getHeight();
//		scale = 5;
//		setSize(width*scale, height*scale);
		
		spriteSheet = Art.particle;
	}

	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.walkDirection();

		tryMove(offset.x *= PARTICLE_SPEED, offset.y *= PARTICLE_SPEED);
		
		setRotation(getRotation() + 1);

//		h += 0.01f;
//		if(h > 1.0f) h = 0f;
//		setColor(new Color(Utility.RGBfromHSV(h, 0.25f, 1.0f)));
	}

	public boolean spawn() {
		scale --;
		setOrigin(width*scale/2, height*scale/2);
		setSize(width*scale, height*scale);
		setPosition(ox - (width-1)*scale/2, oy - (height-1)*scale/2);
		setRotation(getRotation() + 1);
		return scale > 1;
	}
}