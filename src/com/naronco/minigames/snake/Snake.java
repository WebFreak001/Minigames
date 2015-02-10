package com.naronco.minigames.snake;

import java.awt.event.KeyEvent;
import java.io.File;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;

public class Snake implements IGame
{
    
    private SpriteSheet  sprites;
    private Sprite       background;
    private SnakeManager snake;
    private boolean      started = false;
    private Game         host;
    
    @Override
    public int getScale()
    {
        return 4;
    }
    
    @Override
    public Dimension2d getSize()
    {
        return new Dimension2d(200, 160);
    }
    
    @Override
    public void init(Game game)
    {
        host = game;
        background = new Sprite(new File("res/snake-bg.png"));
        sprites = new SpriteSheet(new Sprite(new File("res/snake.png")), new Dimension2d(8, 8));
        snake = new SnakeManager(3, 25, 20);
    }
    
    @Override
    public void draw(Screen screen)
    {
        screen.renderSprite(0, 0, background);
        screen.renderSpriteTile(0, 0, sprites, 2);
        for(int x = 0; x < snake.field.length; x++)
        {
            for(int y = 0; y < snake.field[0].length; y++)
            {
                if(snake.field[x][y] > 0)
                    screen.renderSpriteTile(x * 8,  y * 8, sprites, snake.field[x][y] - 1);
            }
        }
        
        if (host.getKeyboard().isPressed(KeyEvent.VK_SPACE) && !started)
        {
            started = true;
        }
        if (host.getKeyboard().isPressed(KeyEvent.VK_LEFT) && started)
        {
            snake.left();
        }
        if (host.getKeyboard().isPressed(KeyEvent.VK_RIGHT) && started)
        {
            snake.right();
        }
        if (host.getKeyboard().isPressed(KeyEvent.VK_UP) && started)
        {
            snake.up();
        }
        if (host.getKeyboard().isPressed(KeyEvent.VK_DOWN) && started)
        {
            snake.down();
        }
    }
    
    @Override
    public void update(double delta)
    {
        if (started) snake.update();
        if (snake.dead)
        {
            started = false;
            snake.reset();
        }
    }
    
}
