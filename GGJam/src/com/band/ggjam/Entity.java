package com.band.ggjam;

import java.util.logging.Level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class Entity extends Sprite {
	public static final float MAX_FALL_SPEED = -0.5f;
	public static final float GRAVITY = -0.020f;

	/** What level am I in? */
	protected Level currentLevel;

	/** Am I on the ground? */
	protected boolean onGround = false, againstLWall = false, againstRWall = false;

	/** Current Location (top left) */
	protected float x;
	protected float y;
	/** Current direction */
	protected float dx, dy;

	/**
	 * "Bounciness" - taken from Metagun, have yet to test whether it helps with
	 * not being in blocks
	 */
	protected double bounce = 0.05;

	/**
	 * Initializes the entity to a specific location.
	 */
	public Entity(int x, int y, TextureRegion texture) {
		super(texture);
		setSize(getWidth() / GGJam.TILE_SIZE, getHeight() / GGJam.TILE_SIZE);
		this.x = x;
		this.y = y;
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
		if (currentLevel == null) // Do nothing if we're not in a world
			return;
	}

	/**
	 * Try to move specified distance.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void tryMove(float dx, float dy) {
		if(Math.abs(dx) < 0.01) dx = 0;

		float w = getWidth();
		float h = getHeight();

		// First, try to move horizontally
		if (currentLevel.canMove(x + dx, y, w, h)) {
			x += dx;
		} else {
			// Slope?
			if (currentLevel.canMove(x + dx, y + Math.abs(dx), w, h)) {
				x += dx;
				y += Math.abs(dx);
			}
			// Nope. Definitely a wall
			else { //TODO: remove
				// Hit a wall
				hitWall(dx, dy);
				if(dx != 0 && dy < 0) {
					this.dy = 0;
					onGround = false;
				}
			}
		}

		// Next, move vertically
		if (currentLevel.canMove(x, y + dy, w, h)) {
			y += dy;
			
			// What if we're above something really close? "Step" down
			// Any slope that's less than a 45 degree drop
			if(dy < 0 && Math.abs(dy) < Math.abs(dx)) {
				if(!currentLevel.canMove(x-dx, y - Math.abs(dx), w, h)) {
					hitWall(0, -Math.abs(dx));
				}
			}
		} else {
			// Hit the wall
			hitWall(dx, dy);
		}
		setPosition(x, y);
	}

	/**
	 * Called when you run into a wall. Basically just "backs you up" until
	 * you're not colliding with the wall anymore.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void hitWall(float dx, float dy) {
		x += dx;
		y += dy;

		while(!currentLevel.canMove(x, y, getWidth(), getHeight())) {
			x -= dx * 0.01;
			y -= dy * 0.01;
		}
		
		// Check to see if we hit something above us
		if(this.dy > 0 && !currentLevel.canMove(x, y + 0.5f, getWidth(), getHeight())) {
			this.dy = 0;
		}
		
		if(dy < 0) {
			this.dy = 0;
			onGround = true;
		}
		// Now we figure out which part of you is hitting a surface
		//onGround = checkFoot()
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
