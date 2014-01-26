package com.band.ggjam;

import java.io.File;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InGameState extends GameState {
	private Level currentLevel;
	
	protected Music initMusic;
	protected Music swapMusic;
	
	public InGameState() {		
		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		spriteBatch = new SpriteBatch(100);
		
		// Get and set music variables for Tutorial Level
		FileHandle swapHandler = null;
		FileHandle initHandler = null;
		try{
			swapHandler = Gdx.files.internal("audio/tut.wave.wav");
			initHandler = Gdx.files.internal("audio/tut.dot.wav");
		}catch(Exception e) {
			System.out.println("Encountered error: " + e);
		}
		initMusic = Gdx.audio.newMusic(initHandler);
		swapMusic = Gdx.audio.newMusic(swapHandler);
		swapMusic.setLooping(true);
		initMusic.setLooping(true);
		swapMusic.setVolume(0);
		initMusic.setVolume(1);
		currentLevel = new Level("tutorial", this, initMusic, swapMusic);
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		currentLevel.tick(input);
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

	public void beatLevel(String nextLevel) {
		if(nextLevel != null) {
			currentLevel.dispose();
			currentLevel = new Level(nextLevel, this, initMusic, swapMusic);
		}
	}
}

