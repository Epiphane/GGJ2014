package com.band.ggjam;


public class Laser extends Entity {
	public static final float LASER_SPEED = 8;
	public static final int LASER_FRAMES = 10;

	private int frame, deadTicks;
	private int col;
	
	private float dx, dy;
	private boolean toBeDead = false;
	
	public Laser(float x, float y, Level level, float dx, float dy, int frameStart) {
		super(x, y, Art.laser[0][0], level);
		
		deadTicks = 2;
		
		hazard = true;
		
		this.dx = dx;
		this.dy = dy;
		
		col = 3;
		if(dy < 0)
			col -= 3;
		else if(dy > 0)
			col += 2;
		
		if(dx == 0)
			col ++;
		if(dx > 0) {
			col += 2;
			if(dy == 0) col --;
		}
		
		setRegion(Art.laser[col][0]);
		
		frame = frameStart;
	}
	
	public void tick() {
		if(toBeDead) {
			if(deadTicks-- <= 0)
				dead = true;
		}
		else {
			setRegion(Art.laser[col][frame * 2 / LASER_FRAMES]);
			if(++frame >= LASER_FRAMES) frame = 0;
			
			tryMove(dx, dy);
		}
	}
	
	public void tryMove(float dx, float dy) {
		if(dx == 0 && dy == 0) return;
		
		float w = getWidth();
		float h = getHeight();

		if (currentLevel.canMove(this, x + dx, y + dy, w, h)) {
			x += dx;
			y += dy;
		} 
		else {
			// Hit a wall
			hitWall(dx, dy);
		}
		
		setPosition(x, y);
	}
	
	public void hitWall(float dx, float dy) {
		toBeDead = true;
		deadTicks = 3;
		setRegion(Art.laser[col][3]);

		x += dx;
		y += dy;
		while(!currentLevel.canMove(this, x, y, getWidth(), getHeight())) {
			x -= dx * 0.01;
			y -= dy * 0.01;
		}
	}
	
	public boolean canPass(Entity other) {
		return (other instanceof Laser) || (other instanceof Emitter);
	}
	
	public boolean collide(float x, float y, float w, float h) {
		float midx = this.x + getWidth() / 2;
		float midy = this.y + getHeight() / 2;
		float padx = getWidth() / 2;
		float pady = -5;
		
		if(col == 1 || col ==  6) {
			pady = getHeight() / 2;
			padx = -5;
		}
		
		if((x < midx + padx && x + w > midx - padx) && 
				(y < midy + pady && y + h > midy - pady)){
			return true;
		}
		return false;
	}
}
