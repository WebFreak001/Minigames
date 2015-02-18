package com.naronco.minigames.stomp;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;

public class StompClient implements IGame
{
    Game   host;
    Bunny  bunny;
    Sprite bg;
    Socket socket;
    
    @Override
    public int getScale()
    {
        return 4;
    }
    
    @Override
    public Dimension2d getSize()
    {
        return new Dimension2d(300, 160);
    }
    
    @Override
    public void init(Game game)
    {
        host = game;
        final SpriteSheet sitting = new SpriteSheet(new Sprite(new File("res/bunnies-sitting.png")), new Dimension2d(32, 32));
        final SpriteSheet running = new SpriteSheet(new Sprite(new File("res/bunnies-running.png")), new Dimension2d(32, 32));
        final Sprite deadBunny = new Sprite(new File("res/bunnies-dead.png"));
        
        bg = new Sprite(new File("res/bunnies-bg.png"));
        bunny = new Bunny(sitting, running, deadBunny);
        try
        {
            socket = new Socket("localhost", 1337);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void draw(Screen screen)
    {
        screen.renderSprite(0, 0, bg);
        
        bunny.render(screen);
        
        bunny.move(new Vector<Log>(), host.getKeyboard().isPressed(KeyEvent.VK_LEFT), host.getKeyboard().isPressed(KeyEvent.VK_RIGHT));
    }
    
    @Override
    public void update(double delta)
    {
        try
        {
            byte[] dead = new byte[1];
            socket.getInputStream().read(dead);
            byte[] data = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putFloat(0, bunny.x).putFloat(4, bunny.xa).array();
            socket.getOutputStream().write(data);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
