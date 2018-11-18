package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;

public class Fish extends AbstractGameObject
{
	private TextureRegion regFish;
	
	public boolean collected;
	
	public Fish()
	{
		init();
	}
	
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		
		regFish = Assets.instance.collectible.collectible;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regFish;
		batch.draw(reg.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}
	
	public int getScore()
	{
		return 100;
	}
}