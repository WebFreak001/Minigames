package com.naronco.minigames;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.naronco.minigames.pong.Pong;
import com.naronco.minigames.snake.Snake;
import com.naronco.minigames.stomp.Stomp;
import com.naronco.minigames.stomp.StompClient;

public class Main {
	public static void main(String[] args) throws Exception {
	    Vector<IGame> games = new Vector<IGame>();
        games.add(new Pong());
        games.add(new Snake());
        games.add(new Stomp());
        games.add(new StompClient());
        
        Object[] gameNames = { "Pong", "Snake", "Stomp", "Stomp (Join Server)" };
        int i = -1;
        
        if (gameNames.length != 1) {
            String s = (String) JOptionPane.showInputDialog(null,
                    "Choose from one of those games: ", "Choose a Game",
                    JOptionPane.PLAIN_MESSAGE, null, gameNames, "Pong");

            for(int ii = 0; ii < gameNames.length; ii++) {
                if(s.equalsIgnoreCase(gameNames[ii].toString()))
                    i = ii;
            }
            if(i == -1)
                throw new Exception(s);
        }

		new Game(games.get(i)).start();
	}
}
