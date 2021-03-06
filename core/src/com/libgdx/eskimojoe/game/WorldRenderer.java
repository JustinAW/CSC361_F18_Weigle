package com.libgdx.eskimojoe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.libgdx.eskimojoe.game.Assets;
import com.libgdx.eskimojoe.util.Constants;
import com.libgdx.eskimojoe.util.GamePreferences;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	// Box2d Debug
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private Box2DDebugRenderer b2debugRenderer;
	
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}
	
	private void init() 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, 
				Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, 
				Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		
		// Box2d debug
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render() 
	{
		renderWorld(batch);
		renderGui(batch);
	}
	
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		
		// Box2d debug
		if (DEBUG_DRAW_BOX2D_WORLD)
		{
			b2debugRenderer.render(worldController.b2world,  camera.combined);
		}
	}
	
	private void renderGui(SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		
		// draw collected gold coins icon + text to top left edge
		renderGuiScore(batch);
		
		// draw collected powerup icon (anchored to top left edge)
		renderGuiPowerup(batch);
		
		// draw extra lives icon + text on top right
		renderGuiExtraLives(batch);
		
		// draw FPS text on bottom right
		if (GamePreferences.instance.showFpsCounter)
		{
			renderGuiFpsCounter(batch);
		}
		
		// draw game over text to screen
		renderGuiGameOverMessage(batch);
		batch.end();
	}
	
	//draws score in top left
	private void renderGuiScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.collectible.collectible, 
				x, y, 
				50, 50, 
				100, 100, 
				0.35f, -0.35f,
				0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score,
				x + 75, y + 37);
	}
	
	//draws lives indicator in top right
	private void renderGuiExtraLives (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		for(int i = 0; i < Constants.LIVES_START; i++)
		{
			if(worldController.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			
			batch.draw(Assets.instance.player.player,
					x + i * 50, y,
					50, 50, 
					120, 100, 
					0.35f, -0.35f, 
					0);
			batch.setColor(1,1,1,1);
		}
	}
	
	//draws fps counter in bottom right
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45)
		{
			// 45 or more FPS show up in green
			fpsFont.setColor(0,1,0,1);
		}
		else if (fps >= 30)
		{
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1,1,0,1);
		}
		else
		{
			// less than 30 FPS show up in red
			fpsFont.setColor(1,0,0,1);
		}
		
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1,1,1,1); // white
	}
	
	private void renderGuiGameOverMessage (SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isGameOver()) 
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 1, Align.center, false);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}
	
	private void renderGuiPowerup (SpriteBatch batch) 
	{
		float x = -15;
		float y = 30;
		float timeLeftPowerup = worldController.level.eskimoJoe.timeLeftPowerup;
		if (timeLeftPowerup > 0) 
		{
			// Start icon fade in/out if the left power-up time
			// is less than 4 seconds. The fade interval is set
			// to 5 changes per second.
			if (timeLeftPowerup < 4) 
			{
				if (((int)(timeLeftPowerup * 5) % 2) != 0) 
				{
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
		}
		batch.draw(Assets.instance.powerup.powerup, 
				x, y, 
				50, 50, 
				100, 100, 
				0.35f, -0.35f, 
				0);
		batch.setColor(1, 1, 1, 1);
		Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftPowerup, x + 60, y + 57);
	}
	
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
		
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT/(float)height) 
				* (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth/2, 
				cameraGUI.viewportHeight/2, 0);
		cameraGUI.update();
	}
	
	@Override public void dispose() 
	{
		batch.dispose();
	}
}
