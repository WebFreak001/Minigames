package com.naronco.minigames.pong;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;

public class PongBall {

	private float x, y, width, height;
	private float xVelocity, yVelocity;
	
	final double speedIncrease = 1.1;

	private int lastHitColor = 0xE0E0E0;
	private int boundsX, boundsY;
	private int bounces = 0;

	public Vector2d getMedian() {
		return new Vector2d(x + width * 0.5, y + height * 0.5);
	}

	public Vector2d getMedianRight() {
		return new Vector2d(x + width, y + height * 0.5);
	}

	public Vector2d getMedianLeft() {
		return new Vector2d(x, y + height * 0.5);
	}

	public Vector2d getTopLeft() {
		return new Vector2d(x, y);
	}

	public Vector2d getTopRight() {
		return new Vector2d(x + width, y);
	}

	public Vector2d getBottomLeft() {
		return new Vector2d(x, y + height);
	}

	public Vector2d getBottomRight() {
		return new Vector2d(x + width, y + height);
	}

	public PongBall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		xVelocity = 0;
		yVelocity = 0;
	}

	public void setBounds(int width, int height) {
		boundsX = width;
		boundsY = height;
	}

	public void draw(Screen screen) {
		screen.renderRectangle((int) x, (int) y, (int) (x + width),
				(int) (y + height), lastHitColor);
	}

	public boolean intersects(Vector2d a1, Vector2d a2, Vector2d b1,
			Vector2d b2, Vector2d intersection) {

		Vector2d b = a2.copy().add(a1.copy().multiply(-1));
		Vector2d d = b2.copy().add(b1.copy().multiply(-1));
		double bDotDPerp = b.getX() * d.getY() - b.getY() * d.getX();

		// if b dot d == 0, it means the lines are parallel so have infinite
		// intersection points
		if (bDotDPerp == 0)
			return false;

		Vector2d c = b1.add(a1.copy().multiply(-1));
		double t = (c.getX() * d.getY() - c.getY() * d.getX()) / bDotDPerp;
		if (t < 0 || t > 1)
			return false;

		double u = (c.getX() * b.getY() - c.getY() * b.getX()) / bDotDPerp;
		if (u < 0 || u > 1)
			return false;

		Vector2d out = a1.add(b.copy().multiply(t));
		intersection.setX(out.getX());
		intersection.setY(out.getY());

		return true;
	}

	public int update(PongPlayer player1, PongPlayer player2, double delta) {
		x += xVelocity * delta * 5 * Math.pow(speedIncrease, bounces);
		y += yVelocity * delta * 5 * Math.pow(speedIncrease, bounces);

		if (x + width <= 0) {
			bounces = 0;
			return 1;
		}

		if (x >= boundsX) {
			bounces = 0;
			return 2;
		}

		if (y <= 0) {
			yVelocity *= -1;
			y = 0;
		}
		if (y + height >= boundsY) {
			yVelocity *= -1;
			y = boundsY - height - 1;
		}

		Vector2d reflect = new Vector2d(0, 0);
		if (intersects(
				new Vector2d(player1.getX() + player1.getWidth(),
						player1.getY()),
				new Vector2d(player1.getX() + player1.getWidth(), player1
						.getY() + player1.getHeight()),
				getMedianLeft(),
				getMedianLeft().copy().add(
						new Vector2d(xVelocity * delta * 7 * Math.pow(speedIncrease, bounces),
								yVelocity * delta * 7 * Math.pow(speedIncrease, bounces))), reflect)) {
			reflectBall(reflect, player1);
		} else if (intersects(
				new Vector2d(player1.getX() + player1.getWidth(),
						player1.getY()),
				new Vector2d(player1.getX() + player1.getWidth(), player1
						.getY() + player1.getHeight()),
				getTopLeft(),
				getTopLeft().copy().add(
						new Vector2d(xVelocity * delta * 6 * Math.pow(speedIncrease, bounces),
								yVelocity * delta * 6 * Math.pow(speedIncrease, bounces))), reflect)
				|| intersects(
						new Vector2d(player1.getX() + player1.getWidth(),
								player1.getY()),
						new Vector2d(player1.getX() + player1.getWidth(),
								player1.getY() + player1.getHeight()),
						getBottomLeft(),
						getBottomLeft().copy().add(
								new Vector2d(xVelocity * delta * 6 * Math.pow(speedIncrease, bounces),
										yVelocity * delta * 6 * Math.pow(speedIncrease, bounces))),
						reflect)) {
			reflectBall(reflect, player1);
		}

		if (intersects(
				new Vector2d(player2.getX(), player2.getY()),
				new Vector2d(player2.getX(), player2.getY()
						+ player2.getHeight()),
				getMedianRight(),
				getMedianRight().copy().add(
						new Vector2d(xVelocity * delta * 7 * Math.pow(speedIncrease, bounces),
								yVelocity * delta * 7 * Math.pow(speedIncrease, bounces))), reflect)) {
			reflectBall(reflect, player2);
		} else if (intersects(
				new Vector2d(player2.getX(), player2.getY()),
				new Vector2d(player2.getX(), player2.getY()
						+ player2.getHeight()),
				getTopRight(),
				getTopRight().copy().add(
						new Vector2d(xVelocity * delta * 6 * Math.pow(speedIncrease, bounces),
								yVelocity * delta * 6 * Math.pow(speedIncrease, bounces))), reflect)
				|| intersects(
						new Vector2d(player2.getX(), player2.getY()),
						new Vector2d(player2.getX(), player2.getY()
								+ player2.getHeight()),
						getBottomRight(),
						getBottomRight().copy().add(
								new Vector2d(xVelocity * delta * 6 * Math.pow(speedIncrease, bounces),
										yVelocity * delta * 6 * Math.pow(speedIncrease, bounces))),
						reflect)) {
			reflectBall(reflect, player2);
		}

		return 0;
	}

	public void reflectBall(Vector2d reflect, PongPlayer player) {
		System.out.println(reflect.getX() + " " + reflect.getY());
		Vector2d direction = (player.getMedian().add(reflect.multiply(-1)))
				.normalized();
		xVelocity = (float) -direction.getX();
		yVelocity = (float) -direction.getY();
		x += xVelocity;
		y += yVelocity;
		lastHitColor = player.getColor();
		bounces++;
	}

	public void applyForce(float x, float y) {
		xVelocity += x;
		yVelocity += y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		xVelocity = yVelocity = 0;
	}

}
