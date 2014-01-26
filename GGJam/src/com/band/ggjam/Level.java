package com.band.ggjam;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


public class Level {
	private OrthographicCamera camera;
	private TiledMap map;
	private ArrayList<MapObject> polygonCollisions;
	private OrthogonalTiledMapRenderer renderer;
	
	/**
	 * MUSIC VARIABLES
	 */
	private Music initialMusic;
	private Music switchMusic;
	
	/**
	 * Width and height of the level in tiles
	 */
	private int width, height;
	
	private Particle particle;
	private Wave wave;
	private MapObject goal;
	
	private String levelTitle;
	
	/** You're either controlling a particle, or a wave.  This tells you which. */
	public boolean controllingParticle = true;
	
	private boolean beatLevel, particleExplodeLoss, particleSpawning;
	private int ticks;
	
	/**
	 * Contains the filename of the next level, or null if no level afterwards
	 */
	private String nextLevel;
	
	private GameState gameState;
	
	private SpriteBatch batch;
	
	private ArrayList<Entity> entities;
	public ArrayList<Switch> switches;
	/** Used for entities that are drawn below all other entities (like Laser) */
	private ArrayList<Entity> entitiesSubLayer;
	
	@SuppressWarnings("unchecked")
	public Level(String mapName, GameState gameState, Music initMusic, Music swapMusic) {
		entities = new ArrayList<Entity>();
		entitiesSubLayer = new ArrayList<Entity>();
		switches = new ArrayList<Switch>();
		
		// Load some sounds nigga
		initialMusic = initMusic;
		switchMusic = swapMusic;
		
		// Play Dem sounds
		initialMusic.play();
		swapMusic.play();
		
		map = new TmxMapLoader().load("levels/"+mapName+".tmx");
		MapProperties prop = map.getProperties();
		nextLevel = (String) prop.get("nextLevel");
		levelTitle = (String) prop.get("title");
		
		width = (Integer) map.getProperties().get("width");
		height = (Integer) map.getProperties().get("height");
		
		this.gameState = gameState;

		camera = new OrthographicCamera(width * GGJam.TILE_SIZE, height * GGJam.TILE_SIZE);
		camera.setToOrtho(false, width / GGJam.DISPLAY_TILE_SCALE, height / GGJam.DISPLAY_TILE_SCALE);

		// Create renderer
		renderer = new OrthogonalTiledMapRenderer(map, 1 / (GGJam.TILE_SIZE * GGJam.MULTIPLIER_FOR_GOOD_CALCULATIONS));
		renderer.setView(camera);
		
		camera.update();
		
		// Get wall objects from layer #1
		polygonCollisions = new ArrayList<MapObject>();
		for(MapObject object : map.getLayers().get(1).getObjects()) {
			if(object instanceof PolygonMapObject) {
				polygonCollisions.add(object);
			}
		}
		
		// Get all other objects from layer #2
		ArrayList<WaveTail> newTails = new ArrayList<WaveTail>();
		Point lastTail = new Point(-1, -1);
		
		for(MapObject object : map.getLayers().get(2).getObjects()) {
			
			MapProperties properties = object.getProperties();
			if(object.getName().equals("Goal")) {
				goal = object;
			}
			else if(object.getName().equals("Particle")) {
				particle = new Particle((Integer) properties.get("x"), (Integer) properties.get("y"), this);
			}
			else if(object.getName().equals("Wave")) {
				wave = new Wave((Integer) properties.get("x"), (Integer) properties.get("y"), this);
				lastTail = new Point(wave.tileX, wave.tileY);
			}
			else if(object.getName().equals("Emitter")) {
				add(new Emitter((Integer) properties.get("x"), (Integer) properties.get("y"), this), false);
			}
			else if(object.getName().equals("Door")) {
				add(new Door((Integer) properties.get("x"), (Integer) properties.get("y"), this, (String) properties.get("trigger")), false);
			}
			else if(object.getName().equals("Switch")) {
				addSwitch(new Switch((Integer) properties.get("x"), (Integer) properties.get("y"), this, Integer.parseInt((String) properties.get("id"))));
			}
			else if(object.getName().charAt(0) == 't') {
				int tailIndex = Integer.parseInt(object.getName().substring(1));
				newTails.add(new WaveTail( (Integer) properties.get("x"), (Integer) properties.get("y"), 
						this, tailIndex));
			}
		}

		newTails.add(new WaveTail( (int) wave.x, (int) wave.y, this, -1));
		
		// Sort the wave tails
		Collections.sort(newTails);
		int lastDirection = -1;
		
		for (int i = newTails.size() - 1; i >= 0; i--) {
			WaveTail w = newTails.get(i);
			lastDirection = w.setDirection(new Point(lastTail.x - w.tileX, lastTail.y - w.tileY), lastDirection);
			
			lastTail = new Point(w.tileX, w.tileY);
		}
		wave.setTails(newTails);
		
		batch = new SpriteBatch(1);
		
		controllingParticle = true;
		beatLevel = false;
		particleExplodeLoss = false;
		
		wave.tick(null);
//		particleSpawning = true;
	}
	
	/**
	 * Can the main character move to the point X, Y?
	 * @param x location of point we want to move TO
	 * @param y
	 * @return CAN WE MOVE THERE OR NOT
	 */
	public boolean canMove(Entity e, float x, float y, float w, float h) {
		if(particleExplodeLoss) return true;
		
		int pad = 2;
		
		float x0 = (x    ) + pad;// * GGJam.TILE_SIZE;
		float y0 = (y    ) + pad;// * GGJam.TILE_SIZE;
		float x1 = (x + w) - pad;// * GGJam.TILE_SIZE;
		float y1 = (y + h) - pad;// * GGJam.TILE_SIZE;
		
//		System.out.println("Checking ["+x0+","+y0+","+x1+","+y1+"]");
		
		for(MapObject objectToCheck : polygonCollisions) {
			// Check if our 4 corners intersect with any platform polygons
			Polygon polyToCheck = ((PolygonMapObject) objectToCheck).getPolygon();
			if(polyToCheck.contains(x0, y0) || polyToCheck.contains(x0, y1) || polyToCheck.contains(x1, y0) || polyToCheck.contains(x1, y1)) {
				return false;
			}
		}

		if(e.getClass() == Particle.class) {
			Rectangle polyToCheck = ((RectangleMapObject) goal).getRectangle();
			if(polyToCheck.contains(x0, y0) || polyToCheck.contains(x0, y1) || polyToCheck.contains(x1, y0) || polyToCheck.contains(x1, y1))
				beatLevel = true;
			
		}
		else
			if(!e.canPass(particle) && e.collide(particle.x, particle.y, particle.getWidth(), particle.getHeight()) && !particleExplodeLoss) {
				if(e.hazard) explodeParticle();
				return false;
			}

		if(e instanceof Wave) {
			Wave wave = (Wave) e;
			for(int i = 0; i < wave.getTails().size(); i ++) {
				Entity other = wave.getTails().get(i);
				if(other.collide(x, y, w, h))
					return false;
			}
		}
		else if(!e.canPass(wave) && wave.collide(x, y, w, h))
			return false;
		
		for(Entity other : entities) {
			if(!e.canPass(other) && other.collide(x, y, w, h)) {
				if(e.getClass() == Particle.class) explodeParticle();
				return false;
			}
		}
		
		for(Entity other : entitiesSubLayer) {
			if(!e.canPass(other) && other.collide(x, y, w, h)) {
				if(e.getClass() == Particle.class) explodeParticle();
				return false;
			}
		}
		
		for(Switch other : switches)
			if(!other.canPass(e) && other.collide(x, y, w, h))
				return false;
		
		return true;
	}
	
	public void render() {
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		
		for(Entity e : entitiesSubLayer)
			e.draw(batch);

		for(Entity e : switches)
			e.draw(batch);
		
		for(Entity e : entities)
			e.draw(batch);

		if(!particleExplodeLoss) {
			if(wave != null) wave.draw(batch);
		
			batch.setColor(particle.getColor());
			if(particle != null) particle.draw(batch);
			batch.setColor(Color.WHITE);
		}
		
		if(levelTitle != null)
			drawTitle(levelTitle);
			
		batch.end();
	}

	public void tick(Input input) {
		// If we beat the level, do an animation
		if(beatLevel) {
			float dx = particle.x - (Integer) goal.getProperties().get("x");
			particle.x -= dx * 0.1f;
			float dy = particle.y - (Integer) goal.getProperties().get("y");
			particle.y -= dy * 0.1f;
			
			particle.setPosition(particle.x, particle.y);
			
			if(Math.abs(dx) < 0.01f && Math.abs(dy) < 0.01f)
				gameState.beatLevel(nextLevel);
		}
		// If we died, do an animation
		else if(particleExplodeLoss) {
			ticks--;
			
			for(int i = 0; i < entities.size(); i ++) {
				Entity e = entities.get(i);
				e.tick();
				if(e.dead)
					entities.remove(i--);
			}
			
			for(int i = 0; i < entitiesSubLayer.size(); i ++) {
				Entity e = entitiesSubLayer.get(i);
				e.tick();
				if(e.dead)
					entitiesSubLayer.remove(i--);
			}
			
			if(ticks <= 0)
				gameState.setScreen(new LostLevelScreen(gameState));
		}
		else if(particleSpawning) {
			particleSpawning = particle.spawn();
		}
		// Otherwise treat everything normally
		else {
			// Switch between snake/particle music
			if (input.buttonStack.shouldSwitch()) {
				if(controllingParticle) {
					initialMusic.setVolume(1);
					switchMusic.setVolume(0);
				}
				else {
					switchMusic.setVolume(1);
					initialMusic.setVolume(0);
				}
				controllingParticle = !controllingParticle;
			}
			
			//if (controllingParticle) {
				particle.tick(input);
			//} else {
				wave.tick(input);
			//}
			
			for(int i = 0; i < entities.size(); i ++) {
				Entity e = entities.get(i);
				e.tick();
				if(e.dead)
					entities.remove(i--);
			}
			
			for(int i = 0; i < entitiesSubLayer.size(); i ++) {
				Entity e = entitiesSubLayer.get(i);
				e.tick();
				if(e.dead)
					entitiesSubLayer.remove(i--);
			}
		}
	}

	public void dispose() {
	}

	public void add(Entity entity, boolean subLayer) {
		if(subLayer)
			entitiesSubLayer.add(0, entity);
		else
			entities.add(entity);
	}

	public void addSwitch(Switch s) {
		switches.add(s);
	}
	
	private void explodeParticle() {
		ticks = 100;
		for(int i = 0; i < 50; i ++) {
			add(new MiniParticle(particle.x, particle.y, this, (float) Math.random() * 20 - 10, (float) Math.random() * 20 - 10), false);
		}
		particleExplodeLoss = true;
	}
	
	public void drawStringTopRight(String string, float f, float g) {
		
		string = string.toUpperCase();
		f -= string.length() * 20;
		for(int i = 0; i < string.length(); i ++) {
			char ch = string.charAt(i);
			for(int ys = 0; ys < GameState.chars.length; ys ++) {
				int xs = GameState.chars[ys].indexOf(ch);
				if(xs >= 0) {
					batch.draw(Art.guys[xs][ys], f + i * 20, g);
				}
			}
		}
	}
	
	public void drawTitle(String string) {
		drawStringTopRight(string, width * GGJam.TILE_SIZE, height * GGJam.TILE_SIZE - 40);
	}
}
