package com.libgdx.eskimojoe.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.libgdx.eskimojoe.game.Assets;
import com.libgdx.eskimojoe.game.objects.EskimoJoe;
import com.libgdx.eskimojoe.game.objects.EskimoJoe.JUMP_STATE;
import com.libgdx.eskimojoe.game.objects.Fish;
import com.libgdx.eskimojoe.game.objects.Glacier;
import com.libgdx.eskimojoe.game.objects.SnowShoes;
import com.libgdx.eskimojoe.util.CameraHelper;
import com.libgdx.eskimojoe.util.Constants;

public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName();
	
	public CameraHelper cameraHelper;
	
	public Level level;
	
	// Track Player Lives and Score
	public int lives;
	public int score;
	
	// Collision Detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	// Game Over Time Delay
	private float timeLeftGameOverDelay;
	
	public WorldController ()
	{
		init();
	}
	
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	private void initLevel () 
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.eskimoJoe);
	}
	
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
		if (!cameraHelper.hasTarget(level.eskimoJoe))
		{
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if(Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if(Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if(Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
			
			// Camera Controls (zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
			if(Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
			if(Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
			if(Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		}
	}
	
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,  y);
	}
	
	private void onCollisionWithGlacier(Glacier glacier) 
	{
		EskimoJoe eskimoJoe = level.eskimoJoe;
		float heightDifference = Math.abs(eskimoJoe.position.y
		- ( glacier.position.y + glacier.bounds.height));
		if (heightDifference > 0.25f) 
		{
			boolean hitRightEdge = eskimoJoe.position.x > (glacier.position.x + glacier.bounds.width / 2.0f);
			if (hitRightEdge) 
			{
				eskimoJoe.position.x = glacier.position.x + glacier.bounds.width;
			} 
			else 
			{
				eskimoJoe.position.x = glacier.position.x - eskimoJoe.bounds.width;
			}
			return;
		}
		switch (eskimoJoe.jumpState) 
		{
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
				eskimoJoe.position.y = glacier.position.y + eskimoJoe.bounds.height + eskimoJoe.origin.y;
				eskimoJoe.jumpState = JUMP_STATE.GROUNDED;
				break;
			case JUMP_RISING:
				eskimoJoe.position.y = glacier.position.y + eskimoJoe.bounds.height + eskimoJoe.origin.y;
				break;
		}
	}
	
	private void onCollisionWithFish(Fish fish) 
	{
		fish.collected = true;
		score += fish.getScore();
		Gdx.app.log(TAG, "Fish collected");
	}
	
	private void onCollisionWithSnowShoes(SnowShoes snowShoes) 
	{
		snowShoes.collected = true;
		score += snowShoes.getScore();
		level.eskimoJoe.setPowerup(true);
		Gdx.app.log(TAG, "Snow Shoes collected");
	}
	
	private void testCollisions () 
	{
		r1.set(level.eskimoJoe.position.x, level.eskimoJoe.position.y,
		level.eskimoJoe.bounds.width, level.eskimoJoe.bounds.height);
		
		// Test collision Player <-> Glaciers
		for (Glacier glacier : level.glaciers) 
		{
			r2.set(glacier.position.x, glacier.position.y, glacier.bounds.width, glacier.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionWithGlacier(glacier);
			// IMPORTANT: must do all collisions for valid
			// edge testing on platforms.
		}
		
		// Test collision: Player <-> Fish
		for (Fish fish : level.fish) 
		{
			if (fish.collected) continue;
			r2.set(fish.position.x, fish.position.y, fish.bounds.width, fish.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionWithFish(fish);
			break;
		}
		
		// Test collision: Player <-> Snow Shoes
		for (SnowShoes snowShoes : level.snowShoes) 
		{
			if (snowShoes.collected) continue;
			r2.set(snowShoes.position.x, snowShoes.position.y, snowShoes.bounds.width, snowShoes.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionWithSnowShoes(snowShoes);
			break;
		}
	}
	
	private void handleInputGame (float deltaTime) 
	{
		if (cameraHelper.hasTarget(level.eskimoJoe)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) 
			{
				level.eskimoJoe.velocity.x = -level.eskimoJoe.terminalVelocity.x;
			} 
			else if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			{
				level.eskimoJoe.velocity.x = level.eskimoJoe.terminalVelocity.x;
			} 
			
			// Player Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
			{
				level.eskimoJoe.setJumping(true);
			} 
			else 
			{
				level.eskimoJoe.setJumping(false);
			}
		}
	}
	
	public boolean isGameOver()
	{
		return lives < 0;
	}
	
	public boolean isPlayerInWater()
	{
		return level.eskimoJoe.position.y < -5;
	}
	
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		if (isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) init();
		}
		else
		{
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater())
		{
			lives--;
			if (isGameOver())
			{
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			}
			else
			{
				initLevel();
			}
		}
	}
	
	@Override
	public boolean keyUp (int keycode)
	{
		// Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG,  "Game world reset");
		}
		// Toggle Camera Follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.eskimoJoe);
			Gdx.app.debug(TAG, "camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}
}
