package com.libgdx.eskimojoe;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.libgdx.eskimojoe.game.Assets;
import com.libgdx.eskimojoe.screens.MenuScreen;
import com.libgdx.eskimojoe.util.AudioManager;
import com.libgdx.eskimojoe.util.GamePreferences;

public class EskimoJoeMain extends Game
{
	@Override
	public void create () 
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Load assets
		Assets.instance.init(new AssetManager());
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
