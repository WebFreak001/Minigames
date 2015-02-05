package com.naronco.minigames;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.naronco.minigames.pong.Pong;

public class Main {
	public static void main(String[] args) throws Exception {
		Vector<IGame> games = new Vector<IGame>();
		games.add(new Pong());
		int i = 0;

		Object[] gameNames = { "Pong" };
		if (gameNames.length != 1) {
			String s = (String) JOptionPane.showInputDialog(null,
					"Choose from one of those games: ", "Choose a Game",
					JOptionPane.PLAIN_MESSAGE, null, gameNames, "Pong");

			if (s.equals("Pong"))
				i = 0;
			else
				throw new Exception();
		}

		new Game(games.get(i)).start();
	}
}
