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
		if(input.buttonStack.find(Input.RESTART) != null) {
			inGameState.restartLevel();
			setScreen(inGameState);
		}
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

}
