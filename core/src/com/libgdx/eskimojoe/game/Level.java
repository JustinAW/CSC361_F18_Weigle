package com.libgdx.eskimojoe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.libgdx.eskimojoe.game.objects.AbstractGameObject;
import com.libgdx.eskimojoe.game.objects.Clouds;
import com.libgdx.eskimojoe.game.objects.Glacier;
import com.libgdx.eskimojoe.game.objects.Mountains;
import com.libgdx.eskimojoe.game.objects.Water;

public class Level 
{
	public static final String TAG = Level.class.getName();
	 
	public enum BLOCK_TYPE 
	{
		EMPTY(0, 0, 0), // black
	    PLATFORM(0, 255, 0), // green
	    PLAYER_SPAWNPOINT(255, 255, 255), // white
	    ITEM_POWERUP(255, 0, 255), // purple
	    GOAL(255, 0, 0), // red
	    ITEM_COLLECTIBLE(255, 255, 0); // yellow

	    private int color;

	    BLOCK_TYPE(int r, int g, int b) 
	    {
	        color = r << 24 | g << 16 | b << 8 | 0xff;
	    }

	    public boolean sameColor(int color) 
	    {
	        return this.color == color;
	    }

	    public int getColor() 
	    {
	        return color;
	    }
	}
	
	// objects
	public Array<Glacier> glaciers;
	
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public Water water;
	
	public Level (String filename)
	{
		init(filename);
	}
	
	private void init (String filename) 
	{
		//objects
		glaciers = new Array<Glacier>();
		
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					// do nothing
				}
				// platform
				else if (BLOCK_TYPE.PLATFORM.sameColor(currentPixel))
				{
					if (lastPixel != currentPixel)
					{
						obj = new Glacier();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						glaciers.add((Glacier)obj);
					}
					else
					{
						glaciers.get(glaciers.size - 1).increaseLength(1);;
					}
				}
				// player spawn pint
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					
				}
				// powerup
				else if (BLOCK_TYPE.ITEM_POWERUP.sameColor(currentPixel))
				{
					
				}
				// collectible
				else if (BLOCK_TYPE.ITEM_COLLECTIBLE.sameColor(currentPixel))
				{
					
				}
				// unknown object/pixel color
				else
				{
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel>>> 8);   // blue color channel
					int a = 0xff & currentPixel;          // alpha color channel
					Gdx.app.error(TAG,  "Unknown object at x<" + pixelX + "> y<"
							+pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		
		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		water = new Water(pixmap.getWidth());
		water.position.set(0, -3.75f);
		
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG,  "level '" + filename + "' loaded");
	}
	public void render (SpriteBatch batch) 
	{
		// Draw Mountains
		mountains.render(batch);;
		
		// Draw Platforms
		for (Glacier glacier : glaciers)
		{
			glacier.render(batch);
		}
		
		// Draw Water
		water.render(batch);
		
		// Draw Clouds
		clouds.render(batch);
	}
}