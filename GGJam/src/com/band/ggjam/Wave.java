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
	
	private ArrayList<WaveTail> tails;
	private int lastDirection;
	
	int tileX, tileY;
	
	private int dx, dy;
	
	public Wave(int x, int y, Level level) {
		super(x, y, Art.wave[3][0], level);
		tileX = (int) (x / GGJam.TILE_SIZE);
		tileY = (int) (y / GGJam.TILE_SIZE);

		spriteSheet = Art.wave;
		tails = new ArrayList<WaveTail>();
	}
	
	public void setTails(ArrayList<WaveTail> tails) {
		this.tails = tails;
		lastDirection = Utility.directionFromOffset(tails.get(tails.size() - 1).direction);
	}
	
	public void tick(Input input) {
		super.tick();

		tickTails();
		
		if (moving || input == null) {
			moveTicks--;
			if (moveTicks == 0) {
				moving = false;
			}
			
			tickTails();
			
			this.setRegion(Art.wave[3 - moveTicks][0]);
		} else {
			Point offset = input.buttonStack.waveWalkDirection();
			this.setRegion(Art.wave[3][0]);
			
			if (!offset.equals(new Point(0, 0))) {
				dx = offset.x;
				dy = offset.y;
			
				if(currentLevel.canMove(this, x + (int) GGJam.TILE_SIZE * dx, y + (int) GGJam.TILE_SIZE * dy, getWidth() - 1, getHeight() - 1)) {
					// Create a new WaveTail at where we're going to go
					// Be smart about corners
					
					lastDirection = Utility.directionFromOffset(tails.get(tails.size() - 1).direction);
					
					// Tell the WaveTail at the end to kill itself
					tails.get(0).die();
					
					tileX += dx;
					tileY += dy;

					x += GGJam.TILE_SIZE * dx;
					y += GGJam.TILE_SIZE * dy;

					tails.get(tails.size()-1).setDirection(offset, lastDirection);
					
					tails.add(new WaveTail((int) x, (int) y, currentLevel, offset, -1));
					
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
		for (int i = tails.size() - 1; i >= 0; i--) {
			tails.get(i).draw(batch);
		}
		//super.draw(batch);
	}
	
	public boolean collide(float x, float y, float w, float h) {
		x += 2; y += 2;
		w -= 2; h -= 2;
		
		for(WaveTail tail : tails) {
			if(tail.collide(x, y, w, h))
				return true;
		}
		return super.collide(x, y, w, h);
	}
	
	public boolean canPass(Entity other) {
		return (other instanceof Laser);
	}

	public ArrayList<WaveTail> getTails() {
		return tails;
	}
}
