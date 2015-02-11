package com.naronco.minigames.stomp;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;

public class Log {
	int x, y;
	boolean stomping = false;
	boolean goingDown = false;
	boolean canGoUp = true;
	int delay = 0;
	int speed = 2;
	Sprite log;
	
	public Log(int x, Sprite log)
	{
		this.x = x;
		this.log = log;
	}
	
	public void draw(Screen screen)
	{
		if(stomping)
			screen.renderSprite(x, y - 128, log);
	}
	
	public void freeze()
	{
	    canGoUp = false;
	}
	
	public boolean isDeadly()
	{
	    return stomping && goingDown && y < 128 - 8;
	}
	
	public boolean collides(float bunnyX)
	{
	    if(y < 128 - 12) return false;
	    if(bunnyX + 28 > x && bunnyX < x + 60) return true;
	    return false;
	}
	
	public void stomp(int speed)
	{
		if(!stomping && canGoUp)
		{
		    this.speed = speed;
			stomping = true;
			goingDown = true;
		}
	}
	
	public void update()
	{
		if(stomping)
		{
			if(goingDown)
			{
				y += speed;
				if(y >= 128)
				{
					y = 128;
					if(canGoUp)
					    goingDown = false;
				}
			}
			else
			{
				delay++;
				if(delay > 20)
				{
					y -= 2;
					if(y <= 0)
					{
						stomping = false;
						y = 0;
						delay = 0;
					}
				}
			}
		}
	}
}
