package com.naronco.minigames.simon;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;

public class Simon implements IGame
{

    @Override
    public int getScale()
    {
        return 1;
    }

    @Override
    public Dimension2d getSize()
    {
        return new Dimension2d(800, 480);
    }

    @Override
    public void init(Game game)
    {
        
    }

    @Override
    public void draw(Screen screen)
    {
        
    }

    @Override
    public void update(double delta)
    {
    }
}
