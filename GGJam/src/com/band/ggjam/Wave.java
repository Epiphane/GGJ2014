package com.band.ggjam;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wave extends Entity {
	/** How long in ticks it takes to move one square for the Wave */
	private static final int MOVE_TICKS = 20;
	private boolean moving = false;
	
	private TextureRegion[][] spriteSheet;
	
	private ArrayList<WaveTail> tails;
	
	private int tileX, tileY;
	
	public Wave(int x, int y, Level level) {
		super((int) GGJam.TILE_SIZE * x, (int) GGJam.TILE_SIZE * y, Art.wave[0][0], level);
		tileX = x;
		tileY = y;

		spriteSheet = Art.wave;
		tails = new ArrayList<WaveTail>();
		
		// Initialize the tail facing to the right ->
		tails.add(new WaveTail(tileX + 1, tileY, level));
		tails.add(new WaveTail(tileX + 2, tileY, level));
		tails.add(new WaveTail(tileX + 3, tileY, level));
		tails.add(new WaveTail(tileX + 4, tileY, level));
	}
	
	public void tick(Input input) {
		super.tick();

		Point offset = input.buttonStack.airDirection();

		x += offset.x;
		y += offset.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		for (WaveTail tail : tails) {
			tail.draw(batch);
		}
	}
}
