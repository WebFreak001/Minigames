package com.naronco.minigames;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Dimension2d;

public interface IGame {
	int getScale();

	Dimension2d getSize();

	void init(Game game);

	void draw(Screen screen);

	void update(double delta);
}
