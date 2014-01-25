package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity extends Sprite {

	/** What level am I in? */
	protected Level currentLevel;

	/** Current Location (top left) */
	protected float x;
	protected float y;
	/** Current direction */
	protected float dx, dy;

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y, TextureRegion texture, Level level) {
		super(texture);
		//setSize(getWidth() / GGJam.TILE_SIZE, getHeight() / GGJam.TILE_SIZE);
		setPosition(x, y);
		this.x = x;
		this.y = y;
		
		currentLevel = level;
	}

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Update me! This function is where we will have all computations such as
	 * AI, walking off, and the like.
	 */
	public void tick() {
		if(currentLevel == null)
			return;
		
		setPosition(x, y);
	}

	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(float dx, float dy) {
		if(currentLevel != null) {
			if(currentLevel.canMove(x + dx, y + dy, getWidth(), getHeight())) {
				x += dx;
				y += dy;
				setPosition(x,y);
			}
		}
	}

	/**
	 * Called when you run into a wall. Basically just "backs you up" until
	 * you're not colliding with the wall anymore.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void hitWall(float dx, float dy) {
		
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.x = x;
		this.y = y;
	}
}
