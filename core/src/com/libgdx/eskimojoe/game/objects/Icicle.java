package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;

public class Icicle extends AbstractGameObject 
{
	private TextureRegion regIcicle;
	
	public Icicle ()
	{
		init();
	}
	
	private void init ()
	{
		dimension.set(0.25f, 0.5f);
		
		regIcicle = Assets.instance.levelDecoration.icicle;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		origin.set(dimension.x / 2, dimension.y / 2);
	}
	
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		reg = regIcicle;
		batch.draw(reg.getTexture(), 
				position.x - origin.x, position.y - origin.y, 
				origin.x, origin.y, 
				dimension.x, dimension.y, 
				scale.x, scale.y, 
				rotation, 
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
	}
}
