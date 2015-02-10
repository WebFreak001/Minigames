package com.naronco.minigames;

import java.io.File;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;

public class PVPScoreboard {
	private int[] players;
	private Sprite numbers;
	
	public PVPScoreboard()
	{
		players = new int[2];
		numbers = new Sprite(new File("res/numbers.png"));
	}
	
	public void add(int player, int value)
	{
		players[player] += value;
	}
	
	public void set(int player, int value)
	{
		players[player] = value;
	}
	
	public void draw(Screen screen)
	{
		screen.renderSprite((int)screen.getDimension().getWidth() / 2 - 8, 4, (players[0] % 10) * 6, 0, 6, 8, numbers);
		screen.renderSprite((int)screen.getDimension().getWidth() / 2 + 2, 4, (players[1] % 10) * 6, 0, 6, 8, numbers);
	}
	
	public int get(int player)
	{
		return players[player];
	}
}
