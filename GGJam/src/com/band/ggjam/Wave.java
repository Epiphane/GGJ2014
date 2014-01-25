package com.band.ggjam;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	public static final int WAVE_SPEED = 8;
	
	/** How long in ticks it takes to move one square for the Wave */
	public static final int MOVE_TICKS = (int) GGJam.TILE_SIZE / WAVE_SPEED;
	private boolean moving = false;
	private int moveTicks;
	
	private TextureRegion[][] spriteSheet;
	
	public ArrayList<WaveTail> tails;
	
	int tileX, tileY;
	
	private int dx, dy;
	
	public Wave(int x, int y, Level level) {
		super(x, y, Art.wave[1][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);

		spriteSheet = Art.wave;
		tails = new ArrayList<WaveTail>();
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
			
			tickTails();
			
			if (dx < 0) {
				this.setRegion(Art.wave[(moveTicks + 1) % 4][0]);
			} else if (dx > 0)
				this.setRegion(Art.wave[3 - (moveTicks) % 4][0]);
			
//			float w = getWidth();
//			float h = getHeight();
//			
//			// First, try to move horizontally
//			if (currentLevel.canMove(this, x + WAVE_SPEED * dx, y, w, h)) {
//				x += WAVE_SPEED * dx;
//				tickTails();
//			} 
//			else {
//				// Hit a wall
//				hitWall(WAVE_SPEED * dx, WAVE_SPEED * dy);
//			}
//			
//
//			// Next, move vertically
//			if (currentLevel.canMove(this, x, y + WAVE_SPEED * dy, w, h)) {
//				y += WAVE_SPEED * dy;
//				tickTails();
//			} else {
//				// Hit the wall
//				hitWall(WAVE_SPEED * dx, WAVE_SPEED * dy);
//			}
		} else {
			Point offset = input.buttonStack.walkDirection();
			this.setRegion(Art.wave[0][0]);
			if (!offset.equals(new Point(0, 0))) {
				dx = offset.x;
				dy = offset.y;
				
				if(currentLevel.canMove(this, x + WAVE_SPEED * dx, y + WAVE_SPEED * dy, getWidth() - 1, getHeight() - 1)) {

					// Create a new WaveTail at our old location
					tails.add(new WaveTail((int) x, (int) y, currentLevel, offset));
					
					// Tell the WaveTail at the end to kill itself
					tails.get(0).die();
					
					tileX += dx;
					tileY += dy;
					
					moving = true;
					moveTicks = MOVE_TICKS;
				}
			}
		}
	}
	
	private void tickTails() {
		for (int i = 0; i < tails.size(); i++) {
			tails.get(i).tick();
			if (tails.get(i).dead)
				tails.remove(i--);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		for (WaveTail tail : tails) {
			tail.draw(batch);
		}
	}
	
	public boolean collide(float x, float y, float w, float h) {
		for(WaveTail tail : tails) {
			if(tail.collide(x, y, w, h))
				return true;
		}
		return super.collide(x, y, w, h);
	}
}
