package com.band.ggjam;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InGameState extends GameState {
	private Level currentLevel;
	
	protected int numSongs = 5;
	protected int currSong = 0;
	protected int levelCounter = 0;
	protected ArrayList<Music> songs = new ArrayList<Music>();
	
	private String levelName;
	
	public InGameState() {	
		levelName = "tutorial";
		
		camera = new OrthographicCamera(GGJam.GAME_WIDTH, GGJam.GAME_HEIGHT);
		spriteBatch = new SpriteBatch(100);
		
		// Get and set music variables for Tutorial Level
		FileHandle songHandler = null;
		songHandler = Gdx.files.internal("audio/tut.dot.wav");
		songs.add(Gdx.audio.newMusic(songHandler));
		songHandler = Gdx.files.internal("audio/tut2.wave.wav");
		songs.add(Gdx.audio.newMusic(songHandler));
		songHandler = Gdx.files.internal("audio/level1.wave.wav");
		songs.add(Gdx.audio.newMusic(songHandler));
		songHandler = Gdx.files.internal("audio/winter.wave.wav");
		songs.add(Gdx.audio.newMusic(songHandler));
		songHandler = Gdx.files.internal("audio/main.wav");
		songs.add(Gdx.audio.newMusic(songHandler));
				
		//Load all songs
		for(int i = 0; i < numSongs; i++) {
			songs.get(i).setLooping(true);
			songs.get(i).setVolume(1);
		}

		restartLevel();
	}
	
	@Override
	public void render() {
		currentLevel.render();
	}

	@Override
	public void tick(Input input) {
		if(input.buttonStack.find(Input.RESTART) != null) {
			restartLevel();
		}
		else {
			currentLevel.tick(input);
		}
	}

	@Override
	public void dispose() {
		currentLevel.dispose();
	}

	public void beatLevel(String nextLevel) {
		if(nextLevel != null) {
			levelName = nextLevel;
			currentLevel.dispose();
			
			if(levelCounter == 1) {
				levelCounter = 0;
				songs.get(currSong).stop();
				currSong = (currSong+1)%numSongs;
			}
			else
				levelCounter = 1;
			
			currentLevel = new Level(nextLevel, this, songs.get(currSong), levelCounter);
		}
		else {
			
		}
	}
	
	public void restartLevel() {
		currentLevel = new Level(levelName, this, songs.get(currSong), levelCounter);
	}
}

