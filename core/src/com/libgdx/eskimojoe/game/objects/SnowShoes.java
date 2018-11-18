package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;

public class SnowShoes extends AbstractGameObject
{
	private TextureRegion regSnowShoes;
	
	public boolean collected;
	
	public SnowShoes()
	{
		init();
	}
	
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		
		regSnowShoes = Assets.instance.powerup.powerup;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regSnowShoes;
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
		return 250;
	}
}