package com.libgdx.eskimojoe.util;

public class Constants
{
    // Visible  game  world  is  5  meters  wide
	public  static  final  float  VIEWPORT_WIDTH  =  5.0f;
	// Visible  game  world  is  5  meters  tall
	public  static  final  float  VIEWPORT_HEIGHT  =  5.0f;
	
	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/eskimojoe.pack.atlas";
		
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	
	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	
	// Length of time (in seconds) that powerup lasts
	public static final float ITEM_POWERUP_DURATION = 15;
	
	// Delay after game over (to post message and restart game)
	public static final float TIME_DELAY_GAME_OVER = 3;
	
	// Number of Icicles to Spawn
	public static final int ICICLES_SPAWN_MAX = 100;
	
	// Spawn Radius for Icicles
	public static final float ICICLES_SPAWN_RADIUS = 3.5f;
	
	// Delay After Game Finished
	public static final float TIME_DELAY_GAME_FINISHED = 6;
}
