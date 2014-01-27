package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EndState extends GameState {

	public EndState() {
		spriteBatch = new SpriteBatch(100);
	}
	
	@Override
	public void render() {
		spriteBatch.begin();
		draw(Art.winScreen, 0, 0);
		spriteBatch.end();
	}

	@Override
	public void tick(Input input) {
		
	}

	@Override
	public void dispose() {

	}

}
