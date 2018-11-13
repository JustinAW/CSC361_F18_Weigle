/**
 * @author Justin Weigle
 */

package com.libgdx.eskimojoe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Disposable;
import com.libgdx.eskimojoe.util.Constants;


/**
 * Class for loading a organizing assets
 */
public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	
	// singleton: prevent instantiation from other classes
	private Assets() {}
	
	public AssetPlayer player;
	public AssetPlatform platform;
	public AssetCollectible collectible;
	public AssetPowerup powerup;
	public AssetLevelDecoration levelDecoration;
	
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,  "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG,  "asset: " + a);
		}
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		// create game resource objects
		player = new AssetPlayer(atlas);
		platform = new AssetPlatform(atlas);
		collectible = new AssetCollectible(atlas);
		powerup = new AssetPowerup(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
	}
	
	public class AssetPlayer
	{
		public final AtlasRegion player;
		
		public AssetPlayer (TextureAtlas atlas)
		{
			player = atlas.findRegion("eskimojoe01");
		}
	}
	
	public class AssetPlatform
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetPlatform (TextureAtlas atlas)
		{
			edge = atlas.findRegion("glacier_edge");
			middle = atlas.findRegion("glacier_middle");
		}
	}
	
	public class AssetCollectible
	{
		public final AtlasRegion collectible;
		
		public AssetCollectible (TextureAtlas atlas)
		{
			collectible = atlas.findRegion("fish");
		}
	}
	
	public class AssetPowerup
	{
		public final AtlasRegion powerup;
		
		public AssetPowerup (TextureAtlas atlas)
		{
			powerup = atlas.findRegion("snow_shoes");
		}
	}
	
	public class AssetLevelDecoration
	{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion water;
		
		public AssetLevelDecoration (TextureAtlas atlas)
		{
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			water = atlas.findRegion("water");
		}
	}
	
	@Override
	public void dispose ()
	{
		assetManager.dispose();
	}
	
	public void error (String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}
	
	@Override
	public void error (AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
}
