package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;

public class Glacier extends AbstractGameObject 
{
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	
	private int length;
	
	public Glacier ()
	{
		init();
	}
	
	private void init ()
	{
		dimension.set(1,1.5f);
		
		regEdge = Assets.instance.platform.edge;
		regMiddle = Assets.instance.platform.middle;
		
		// Start length of this glacier
		setLength(1);
	}
	
	public void setLength (int length)
	{
		this.length = length;
		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y);
	}
	
	public void increaseLength (int amount)
	{
		setLength(length + amount);
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		// Draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), 
				position.x + relX, position.y + relY, 
				origin.x, origin.y, 
				dimension.x / 4, dimension.y, 
				scale.x, scale.y, 
				rotation, 
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				false, false);
		
		// Draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length*2; i++)
		{
			batch.draw(reg.getTexture(), 
					position.x + relX, position.y + relY, 
					origin.x, origin.y, 
					dimension.x / 2, dimension.y, 
					scale.x, scale.y, 
					rotation, 
					reg.getRegionX(), reg.getRegionY(), 
					reg.getRegionWidth(), reg.getRegionHeight(), 
					false, false);
			relX += dimension.x / 2;
		}
		
		// Draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), 
				position.x + relX, position.y + relY, 
				origin.x + dimension.x / 8, origin.y, 
				dimension.x / 4, dimension.y, 
				scale.x, scale.y, 
				rotation, 
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), 
				true, false);
	}
	
//	@Override
//	public void update (float deltaTime) 
//	{
//		super.update(deltaTime);
//		floatCycleTimeLeft -= deltaTime;
//		if (floatCycleTimeLeft <= 0) 
//		{
//			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
//			floatingDownwards = !floatingDownwards;
//			body.setLinearVelocity(0, 
//					FLOAT_AMPLITUDE(floatingDownwards ? -1 : 1));
//		} 
//		else 
//		{
//			body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
//		}
//	}
}
