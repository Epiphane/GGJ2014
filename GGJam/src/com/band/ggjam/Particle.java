package com.band.ggjam;

public class Particle extends Entity {

	public Particle(int x, int y) {
		super(x, y);
	}

	public void tick(Input input) {
		super.tick();
		
		// TODO: If you're the active particle...
		Point offset = Utility.offsetFromDirection(
				input.buttonStack.consumeDirection());
		
		x += offset.x;
		y += offset.y;
	}
	
	

}
