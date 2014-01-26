package com.band.ggjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LostLevelScreen extends GameState {

	private GameState inGameState;
	
	public LostLevelScreen(GameState gameState) {
		inGameState = gameState;
		
		spriteBatch = new SpriteBatch(1);
	}

	@Override
	public void render() {
		inGameState.render();
		
		
	}

	@Override
	public void tick(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
