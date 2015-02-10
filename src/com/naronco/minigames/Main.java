package com.naronco.minigames;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.naronco.minigames.pong.Pong;
import com.naronco.minigames.snake.Snake;

public class Main {
	public static void main(String[] args) throws Exception {
		Vector<IGame> games = new Vector<IGame>();
		games.add(new Pong());
		games.add(new Snake());
		int i = 0;

		Object[] gameNames = { "Pong", "Snake" };
		if (gameNames.length != 1) {
			String s = (String) JOptionPane.showInputDialog(null,
					"Choose from one of those games: ", "Choose a Game",
					JOptionPane.PLAIN_MESSAGE, null, gameNames, "Pong");

			if (s.equalsIgnoreCase(gameNames[0].toString()))
				i = 0;
			else if (s.equalsIgnoreCase(gameNames[1].toString()))
				i = 1;
			else
				throw new Exception(s);
		}

		new Game(games.get(i)).start();
	}
}
