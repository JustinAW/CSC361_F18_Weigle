package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;

public class IglooGoal extends AbstractGameObject 
{
	private TextureRegion regGoal;
	
	public IglooGoal ()
	{
		init();
	}
	
	private void init ()
	{
		dimension.set(5.0f, 3.0f);
		regGoal = Assets.instance.levelDecoration.iglooGoal;
		
		// Set bounding box for collision detection
		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2.0f, 0.0f);
	}
	
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		reg = regGoal;
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