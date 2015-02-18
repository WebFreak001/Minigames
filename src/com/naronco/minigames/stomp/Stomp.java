package com.naronco.minigames.stomp;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Func;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;
import com.naronco.minigames.TcpManager;

public class Stomp implements IGame
{
    Vector<Log> logs;
    Game        host;
    Random      random;
    int         speed;
    float       logSpeed;
    int         time;
    int         level;
    Sprite      bg;
    Bunny[]     bunnies;
    int         bunniesLen = 0;
    Bunny       ownBunny;
    boolean     started    = false;
    
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
        logs = new Vector<Log>();
        Sprite logSprite = new Sprite(new File("res/bloody-log.png"));
        logs.add(new Log(0, logSprite));
        logs.add(new Log(64, logSprite));
        logs.add(new Log(128, logSprite));
        logs.add(new Log(192, logSprite));
        logs.add(new Log(256, logSprite));
        random = new Random();
        speed = 30;
        logSpeed = 1;
        time = 0;
        level = 1;
        final SpriteSheet sitting = new SpriteSheet(new Sprite(new File("res/bunnies-sitting.png")), new Dimension2d(32, 32));
        final SpriteSheet running = new SpriteSheet(new Sprite(new File("res/bunnies-running.png")), new Dimension2d(32, 32));
        final Sprite deadBunny = new Sprite(new File("res/bunnies-dead.png"));
        
        bg = new Sprite(new File("res/bunnies-bg.png"));
        ownBunny = new Bunny(sitting, running, deadBunny);
        
        bunnies = new Bunny[32];
        bunnies[0] = ownBunny;
        bunniesLen++;
        
        TcpManager tcp = new TcpManager(1337);
        tcp.start();
        tcp.onConnection(new Func<Socket>()
        {
            @Override
            public void run(Socket arg)
            {
                final Socket socket = arg;
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        System.out.println("Connection from " + socket.getRemoteSocketAddress().toString());
                        int i = bunniesLen;
                        bunnies[i] = new Bunny(sitting, running, deadBunny);
                        bunniesLen++;
                        try
                        {
                            socket.getOutputStream().write(ByteBuffer.allocate(8).putInt(0, logs.size()).putInt(4, bunnies.length).array());
                        }
                        catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        while (true)
                        {
                            try
                            {
                                socket.getOutputStream().write(new byte[] { (byte) (bunnies[i].dead() ? 1 : 0) });
                                socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(0, speed).array());
                                for (Log log : logs)
                                {
                                    socket.getOutputStream().write(ByteBuffer.allocate(13).putInt(0, log.x).putInt(4, log.y).putInt(8, log.delay).put(12, (byte) ((log.stomping ? 1 : 0) | (log.goingDown ? 2 : 0))).array());
                                }
                                for (int x = 0; x < bunnies.length; x++)
                                {
                                    if (bunnies[x] == null)
                                    {
                                        socket.getOutputStream().write(new byte[10]);
                                    }
                                    else
                                    {
                                        socket.getOutputStream().write(ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN).put(0, (byte) 1).putFloat(1, bunnies[x].x).putFloat(5, bunnies[x].xa).put(9, (byte) ((bunnies[x].looksLeft ? 1 : 0) | (bunnies[x].isDead ? 2 : 0))).array());
                                    }
                                }
                                byte[] position = new byte[9];
                                socket.getInputStream().read(position);
                                bunnies[i].x = ByteBuffer.wrap(position, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                                bunnies[i].xa = ByteBuffer.wrap(position, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                                bunnies[i].looksLeft = position[8] == 1;
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
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
        
        if(host.getKeyboard().isPressed(KeyEvent.VK_SPACE))
            started = true;
        
        ownBunny.move(logs, host.getKeyboard().isPressed(KeyEvent.VK_LEFT), host.getKeyboard().isPressed(KeyEvent.VK_RIGHT));
    }
    
    @Override
    public void update(double delta)
    {
        for (Log log : logs)
        {
            log.update();
            
            for (Bunny bunny : bunnies)
            {
                if (bunny != null) bunny.checkLog(log);
            }
        }
        
        for (Bunny bunny : bunnies)
        {
            if (bunny != null) bunny.update();
        }
        
        if (started)
        {
            if (random.nextInt(speed) == 0)
            {
                logs.get(random.nextInt(logs.size())).stomp(Math.round(logSpeed));
            }
            
            if (time > 200 + Math.pow(level, 2) * 20)
            {
                if (speed > 12)
                {
                    System.out.println("Level Up!");
                    logSpeed += 0.5f;
                    speed--;
                    level++;
                    time = 0;
                }
            }
            
            time++;
        }
    }
    
}
