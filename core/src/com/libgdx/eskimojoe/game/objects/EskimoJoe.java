package com.libgdx.eskimojoe.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.eskimojoe.game.Assets;
import com.libgdx.eskimojoe.game.objects.AbstractGameObject;
import com.libgdx.eskimojoe.util.CharacterSkin;
import com.libgdx.eskimojoe.util.Constants;
import com.libgdx.eskimojoe.util.GamePreferences;

public class EskimoJoe extends AbstractGameObject
{
	public static final String TAG = EskimoJoe.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX -0.018f;
	
	public enum VIEW_DIRECTION{LEFT, RIGHT}
    public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
    
    private TextureRegion regJoe;
    
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasPowerup;
    public float timeLeftPowerup;
    
    public EskimoJoe() 
    {
    	init();
    }
    
    public void init() 
    {
    	dimension.set(1,1);
    	regJoe = Assets.instance.player.player;
    	//center image on game object
    	origin.set(dimension.x/2, dimension.y/2);
    	//Building box for collision detection
    	bounds.set(0, 0, dimension.x, dimension.y);
    	//set physics value
    	terminalVelocity.set(3.0f, 4.0f);
    	friction.set(12.0f, 0.0f);
    	acceleration.set(0.0f, -25.0f);
    	//View direction
    	viewDirection = VIEW_DIRECTION.RIGHT;
    	//jumpState
    	jumpState = JUMP_STATE.FALLING;
    	timeJumping = 0;
    	//Power-ups
    	hasPowerup = false;
    	timeLeftPowerup = 0;
    }
    
    public void setJumping(boolean jumpKeyPressed) 
    {
    	switch (jumpState) 
    	{
    		case GROUNDED: //character is standing
    		  if(jumpKeyPressed)
    		  {
    			  //start counting jump time from the beginning
    			  timeJumping = 0;
    			  jumpState = JUMP_STATE.JUMP_RISING;
    		  }
    	 	  break;
    	 	  
    		case JUMP_RISING: //rising in the air
    		  if(!jumpKeyPressed)
    		  jumpState = JUMP_STATE.JUMP_FALLING;
    		  break;
    		  
    		case FALLING: //Falling down
    		case JUMP_FALLING: //Falling down after jump
    		  if(jumpKeyPressed && hasPowerup)
    		  {
    			  timeJumping = JUMP_TIME_OFFSET_FLYING;
    			  jumpState = JUMP_STATE.JUMP_RISING;
    		  }
    		  break;
    	}
    }
    
    public void setPowerup(boolean pickedUp) 
    {
    	hasPowerup = pickedUp;
    	if(pickedUp)
    	{
    		timeLeftPowerup = Constants.ITEM_POWERUP_DURATION;
    	}
    }
    
    //find out whether the powerup is still active
    public boolean hasPowerup()
    {
    	return hasPowerup && timeLeftPowerup > 0;
    }
	
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		if(velocity.x != 0)
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : 
				VIEW_DIRECTION.RIGHT;
		}
		
		if(timeLeftPowerup > 0)
		{
			timeLeftPowerup -= deltaTime;
			if(timeLeftPowerup < 0)
			{
				//disable powerup
				timeLeftPowerup = 0;
				setPowerup(false);
			}
		}
	}
	
	@Override
	protected void updateMotionY(float deltaTime)
	{
		switch (jumpState)
		{
		  case GROUNDED:
			  jumpState = JUMP_STATE.FALLING;
			  break;
			  
		  case JUMP_RISING:
		  	  //keep track of jump time
		  	  timeJumping += deltaTime;
		  	  //jump time left?
		  	  if(timeJumping <= JUMP_TIME_MAX)
		  	  {
		  		  //still jumping
		  		  velocity.y = terminalVelocity.y;
		  	  }
		  	  break;
		  
		  case FALLING:
		    break;
		  case JUMP_FALLING:
		    //Add delta times to track jump time
		    timeJumping += deltaTime;
		    //jump to minimal height if jump key was pressed too short
		    if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
		    {
		    	//still jumping
		    	velocity.y = terminalVelocity.y;
		    }
		}
		if(jumpState != JUMP_STATE.GROUNDED)
		{
			super.updateMotionY(deltaTime);
		}
	}
	
	
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		// Apply Skin Color
		batch.setColor(
		CharacterSkin.values()[GamePreferences.instance.charSkin]
		.getColor());
		
		//set special color when game object has a powerup
		if(hasPowerup)
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		//draw image
		reg = regJoe;
		batch.draw(reg.getTexture(), 
				position.x, position.y,
				origin.x, origin.y, 
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation, 
				reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(),
				viewDirection == VIEW_DIRECTION.LEFT, false);
		
		//reset color
		batch.setColor(1,1,1,1);
	}
}
