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
    Game        host;
    Bunny       bunny;
    Sprite      bg;
    Socket      socket;
    Vector<Log> logs;
    Bunny[]     bunnies;
    Sprite deadBunny;
    SpriteSheet sitting;
    SpriteSheet running;
    
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
        sitting = new SpriteSheet(new Sprite(new File("res/bunnies-sitting.png")), new Dimension2d(32, 32));
        running = new SpriteSheet(new Sprite(new File("res/bunnies-running.png")), new Dimension2d(32, 32));
        deadBunny = new Sprite(new File("res/bunnies-dead.png"));
        Sprite log = new Sprite(new File("res/bloody-log.png"));
        
        bg = new Sprite(new File("res/bunnies-bg.png"));
        bunny = new Bunny(sitting, running, deadBunny);
        try
        {
            socket = new Socket("localhost", 1337);
            byte[] logCount = new byte[8];
            socket.getInputStream().read(logCount);
            logs = new Vector<Log>();
            
            for (int i = 0; i < ByteBuffer.wrap(logCount).getInt(0); i++)
            {
                logs.add(new Log(0, log));
            }
            bunnies = new Bunny[ByteBuffer.wrap(logCount).getInt(4)];
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
        
        for (Bunny bunny : bunnies)
        {
            if (bunny != null) bunny.render(screen);
        }
        
        for (Log log : logs)
        {
            log.draw(screen);
        }
        
        for (Bunny bunny : bunnies)
        {
            if (bunny != null) bunny.renderParticles(screen);
        }
        
        bunny.move(logs, host.getKeyboard().isPressed(KeyEvent.VK_LEFT), host.getKeyboard().isPressed(KeyEvent.VK_RIGHT));
    }
    
    @Override
    public void update(double delta)
    {
        bunny.update();
        for (Log log : logs)
        {
            log.update();
        }
        
        for (Bunny bunny : bunnies)
        {
            if (bunny != null) bunny.update();
        }
        
        try
        {
            byte[] dead = new byte[1];
            socket.getInputStream().read(dead);
            if(dead[0] == 1)
                bunny.isDead = true;
            byte[] speed = new byte[4];
            socket.getInputStream().read(speed);
            for (int i = 0; i < logs.size(); i++)
            {
                byte[] logHeader = new byte[13];
                socket.getInputStream().read(logHeader);
                logs.get(i).speed = ByteBuffer.wrap(speed).getInt();
                logs.get(i).x = ByteBuffer.wrap(logHeader).getInt(0);
                logs.get(i).y = ByteBuffer.wrap(logHeader).getInt(4);
                logs.get(i).delay = ByteBuffer.wrap(logHeader).getInt(8);
                byte bits = ByteBuffer.wrap(logHeader).get(12);
                logs.get(i).stomping = (bits & 1) == 1;
                logs.get(i).goingDown = ((bits >> 1) & 1) == 1;
            }
            for (int i = 0; i < bunnies.length; i++)
            {
                byte[] bunnyHeader = new byte[10];
                socket.getInputStream().read(bunnyHeader);
                if(bunnyHeader[0] == 1)
                {
                    if(bunnies[i] == null)
                        bunnies[i] = new Bunny(sitting, running, deadBunny);
                    bunnies[i].x = ByteBuffer.wrap(bunnyHeader, 1, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                    bunnies[i].xa = ByteBuffer.wrap(bunnyHeader, 5, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                    bunnies[i].looksLeft = (bunnyHeader[9] & 1) == 1;
                    boolean wasDead = bunnies[i].isDead;
                    bunnies[i].isDead = ((bunnyHeader[9] >> 1) & 1) == 1;
                    if(!wasDead && bunnies[i].isDead)
                        bunnies[i].kill();
                }
                else
                {
                    bunnies[i] = null;
                }
            }
            byte[] data = ByteBuffer.allocate(9).order(ByteOrder.LITTLE_ENDIAN).putFloat(0, bunny.x).putFloat(4, bunny.xa).put(8, (byte) (bunny.looksLeft ? 1 : 0)).array();
            socket.getOutputStream().write(data);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
