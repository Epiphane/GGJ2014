package com.band.ggjam;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	public static final int WAVE_SPEED = 4;
	
	/** How long in ticks it takes to move one square for the Wave */
	public static final int MOVE_TICKS = (int) GGJam.TILE_SIZE / WAVE_SPEED;
	private boolean moving = false;
	private int moveTicks;
	
	private TextureRegion[][] spriteSheet;
	
	public ArrayList<WaveTail> tails;
	
	int tileX, tileY;
	
	private int dx, dy;
	
	public Wave(int x, int y, Level level) {
		super(x, y, Art.wave[0][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);

		spriteSheet = Art.wave;
		tails = new ArrayList<WaveTail>();
		
		// Initialize the tail facing to the right ->
		Point offset = Utility.offsetFromDirection(Input.LEFT);
	}
	
	public void tick(Input input) {
		super.tick();

		if (moving) {
			moveTicks--;
			if (moveTicks == 0) {
				moving = false;
			}
			
			x += WAVE_SPEED * dx;
			y += WAVE_SPEED * dy;
			
			for (int i = 0; i < tails.size(); i++) {
				tails.get(i).tick();
				if (tails.get(i).dead)
					tails.remove(i--);
			}
		} else {
			Point offset = input.buttonStack.walkDirection();
			if (!offset.equals(new Point(0, 0))) {
				dx = offset.x;
				dy = offset.y;

				// Create a new WaveTail at our old location
				tails.add(new WaveTail((int) x, (int) y, currentLevel, offset));
				
				// Tell the WaveTail at the end to kill itself
				tails.get(0).die();
				
				tileX += dx;
				tileY += dy;
				
				moving = true;
				moveTicks = MOVE_TICKS;
				System.out.println("START Moving");
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		for (WaveTail tail : tails) {
			tail.draw(batch);
		}
	}
}
