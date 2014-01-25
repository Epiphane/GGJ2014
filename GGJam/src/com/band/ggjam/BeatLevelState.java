package com.band.ggjam;

public class BeatLevelState extends GameState {
	Level currentLevel;
	
	public BeatLevelState(Level level) {
		currentLevel = level;
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		System.out.println("YOU WON");
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

}

