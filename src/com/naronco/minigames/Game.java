package com.naronco.minigames;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Window;

public class Game extends Eggine {

	IGame game;

	public Game(IGame game) {
		super(60, 30, new Window("Minigames", game.getSize(), game.getScale()));
		game.init(this);
		this.game = game;
	}

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0x000000);

		game.draw(screen);
	}

	@Override
	public void update(double delta) {
		game.update(delta);
	}

}
