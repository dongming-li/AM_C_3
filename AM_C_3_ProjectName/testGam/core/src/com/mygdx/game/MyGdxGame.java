package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.GameStateManager;
import com.mygdx.game.Screens.MenuState;
import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM=100;

	//powers of 2 so we can or the bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short FIREBALL_BIT = 1024;


	public SpriteBatch batch;
	private Music music;
	private GameStateManager gsm;
	Texture img;

	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//img = new Texture("badlogic.jpg");

		//mario
		setScreen( new PlayScreen(this));
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg",Music.class);//load music to be in the background

		//load game sound
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.finishLoading();//finish loading the assets

		//screens for different states
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1,0,1,1);
		gsm.push(new MenuState(gsm));//push menu to stack


		//music = MyGdxGame.manager.get("audio/music/mario_music.ogg", Music.class);
		//manager.load("audio/music/mario_music.ogg",Music.class);



		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();


		// just bad logic image

//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		gsm.update(Gdx.graphics.getDeltaTime());
//		gsm.render(batch);




	}
//
	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}


}
