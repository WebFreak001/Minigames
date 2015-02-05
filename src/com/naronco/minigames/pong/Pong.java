package com.naronco.minigames.pong;

import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;
import com.naronco.minigames.PVPScoreboard;

public class Pong implements IGame {
	private PongPlayer player1, player2;
	private PongBall ball;
	private boolean started = false;
	private Game host;
	private Random random;
	private PVPScoreboard scoreboard;

	@Override
	public int getScale() {
		return 4;
	}

	@Override
	public Dimension2d getSize() {
		return new Dimension2d(200, 120);
	}

	@Override
	public void init(Game game) {
		host = game;
		player1 = new PongPlayer(10, 60 - 20, 8, 40, 0x673AB7);
		player2 = new PongPlayer(190 - 8, 60 - 20, 8, 40, 0x3F51B5);
		ball = new PongBall(100 - 5, 60 - 5, 10, 10);
		
		player1.setBounds(200, 120);
		player2.setBounds(200, 120);
		ball.setBounds(200, 120);

		scoreboard = new PVPScoreboard();
		random = new Random();
	}

	@Override
	public void draw(Screen screen) {
		player1.draw(screen);
		player2.draw(screen);
		ball.draw(screen);
		scoreboard.draw(screen);

		if (host.getKeyboard().isPressed(KeyEvent.VK_SPACE) && !started) {
			started = true;
			ball.applyForce(random.nextBoolean() ? -1 : 1,
					0); //random.nextFloat() * 2 - 1);
		}
		if (host.getKeyboard().isPressed(KeyEvent.VK_W) && started) {
			player1.move(-1);
		}
		if (host.getKeyboard().isPressed(KeyEvent.VK_S) && started) {
			player1.move(1);
		}
		if (host.getKeyboard().isPressed(KeyEvent.VK_UP) && started) {
			player2.move(-1);
		}
		if (host.getKeyboard().isPressed(KeyEvent.VK_DOWN) && started) {
			player2.move(1);
		}
	}

	@Override
	public void update(double delta) {
		player1.update(delta);
		player2.update(delta);
		int result = ball.update(player1, player2, delta);
		if(result > 0)
		{
			started = false;
			reset();
			scoreboard.add(1 - (result - 1), 1);

			if(scoreboard.get(0) >= 7)
			{
				JOptionPane.showMessageDialog(null, "Player 1 won!", "Congratulations", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
			if(scoreboard.get(1) >= 7)
			{
				JOptionPane.showMessageDialog(null, "Player 2 won!", "Congratulations", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
		}
	}

	public void reset() {
		player1.setPosition(10, 60 - 20);
		player2.setPosition(175, 60 - 20);
		ball.setPosition(100 - 5, 60 - 5);
	}
}
