package com.libgdx.eskimojoe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.libgdx.eskimojoe.EskimoJoeMain;

public class DesktopLauncher {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	public static void main (String[] arg) {
		//rebuilds texture atlas every time the game is run on desktop
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images",
					"../core/assets/images", "eskimojoe.pack");
			TexturePacker.process(settings, "assets-raw/images-ui",
					"../core/assets/images", "eskimojoe-ui.pack");
		}
				
		LwjglApplicationConfiguration config = 
				new LwjglApplicationConfiguration();
		config.title = "Eskimo Joe";
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new EskimoJoeMain(), config);
	}
}
