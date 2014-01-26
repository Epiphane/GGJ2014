package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleState extends GameState {

	public TitleState() {
		spriteBatch = new SpriteBatch(100);
	}
	
	@Override
	public void render() {
		spriteBatch.begin();
		draw(Art.titleScreen, 50, 50);
		spriteBatch.end();
	}

	@Override
	public void tick(Input input) {
		// When user clicks button
		if (input.clicked) 
			setScreen(new InGameState());
	}

	@Override
	public void dispose() {

	}

}
