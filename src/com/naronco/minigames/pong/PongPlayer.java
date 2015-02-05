package com.naronco.minigames.pong;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;

public class PongPlayer {

	private float x, y, width, height;
	private float yVelocity;
	private int boundsY;
	private int color;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getColor() {
		return color;
	}
	
	public Vector2d getMedian() {
		return new Vector2d(x + width * 0.5, y + height * 0.5);
	}

	public PongPlayer(int x, int y, int width, int height, int color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		yVelocity = 0;
	}

	public void setBounds(int width, int height) {
		boundsY = height;
	}

	public void draw(Screen screen) {
		screen.renderRectangle((int) x, (int) y, (int) (x + width),
				(int) (y + height), color);
	}

	public void setPosition(int x, int y) {
		yVelocity = 0;
		this.x = x;
		this.y = y;
	}

	public void update(double delta) {
		this.y += yVelocity * delta;
		yVelocity *= 0.8f;
		if(y <= 0)
		{
			yVelocity = 0;
			y = 0;
		}
		else if(y + height >= boundsY)
		{
			yVelocity = 0;
			y = boundsY - height;
		}
	}

	public void move(int dir) {
		yVelocity += dir * 1.1f;
	}

	public boolean intersects(float x, float y, float width, float height) {
		Vector2d min1 = new Vector2d(this.x, this.y);
		Vector2d max1 = new Vector2d(this.x + this.width, this.y + this.height);
		Vector2d min2 = new Vector2d(x, y);
		Vector2d max2 = new Vector2d(x + width, y + height);

		if (max1.getX() < min2.getX())
			return false;
		if (min1.getX() > max2.getX())
			return false;
		if (max1.getY() < min2.getY())
			return false;
		if (min1.getY() > max2.getY())
			return false;
		return true;
	}
}
