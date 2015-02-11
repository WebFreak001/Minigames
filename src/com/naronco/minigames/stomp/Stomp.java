package com.naronco.minigames.stomp;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteAnimation;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.minigames.Game;
import com.naronco.minigames.IGame;

public class Stomp implements IGame
{
    
    Vector<Log>     logs;
    Game            host;
    Random          random;
    int             speed;
    float           logSpeed;
    int             time;
    int             level;
    SpriteAnimation sitLeft;
    SpriteAnimation runLeft;
    SpriteAnimation sitRight;
    SpriteAnimation runRight;
    Sprite          deadBunny;
    Sprite          bg;
    float           xa;
    float           x;
    boolean         looksLeft;
    boolean         inLog;
    ParticleSystem  particles;
    boolean         isDead;
    
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
        SpriteSheet sitting = new SpriteSheet(new Sprite(new File("res/bunnies-sitting.png")), new Dimension2d(32, 32));
        SpriteSheet running = new SpriteSheet(new Sprite(new File("res/bunnies-running.png")), new Dimension2d(32, 32));
        sitLeft = new SpriteAnimation(sitting, 0, 1, 1);
        runLeft = new SpriteAnimation(running, 0, 1, 12);
        sitRight = new SpriteAnimation(sitting, 2, 3, 1);
        runRight = new SpriteAnimation(running, 2, 3, 12);
        bg = new Sprite(new File("res/bunnies-bg.png"));
        deadBunny = new Sprite(new File("res/bunnies-dead.png"));
        xa = 0;
        x = 140;
        looksLeft = false;
        particles = new ParticleSystem();
        isDead = false;
    }
    
    @Override
    public void draw(Screen screen)
    {
        screen.renderSprite(0, 0, bg);
        if (isDead)
        {
            screen.renderSprite(Math.round(x), 126, deadBunny);
        }
        else
        {
            if (Math.abs(xa) > 0.3f) screen.renderAnimatedSprite(Math.round(x), 96, looksLeft ? runLeft : runRight);
            else screen.renderAnimatedSprite(Math.round(x), 96, looksLeft ? sitLeft : sitRight);
        }
        
        for (Log log : logs)
        {
            log.draw(screen);
        }
        
        particles.update();
        particles.draw(screen);
        
        if (!isDead)
        {
            if (host.getKeyboard().isPressed(KeyEvent.VK_LEFT))
            {
                boolean willCollide = false;
                for (Log log : logs)
                {
                    if (log.collides(x - 4))
                    {
                        willCollide = true;
                    }
                }
                if (!willCollide)
                {
                    xa -= 1.1f;
                    looksLeft = true;
                }
            }
            if (host.getKeyboard().isPressed(KeyEvent.VK_RIGHT))
            {
                boolean willCollide = false;
                for (Log log : logs)
                {
                    if (log.collides(x + 4))
                    {
                        willCollide = true;
                    }
                }
                if (!willCollide)
                {
                    xa += 1.1f;
                    looksLeft = false;
                }
            }
            xa *= 0.8f;
            x += xa;
        }
    }
    
    public void kill()
    {
        particles.spawn((int) x, 128, 30);
        isDead = true;
    }
    
    @Override
    public void update(double delta)
    {
        for (Log log : logs)
        {
            log.update();
            
            if (log.collides(x))
            {
                xa = 0;
                if (log.isDeadly())
                {
                    kill();
                }
            }
        }
        
        if (random.nextInt(speed) == 0)
        {
            logs.get(random.nextInt(logs.size())).stomp(Math.round(logSpeed));
        }
        
        sitLeft.nextFrame();
        runLeft.nextFrame();
        sitRight.nextFrame();
        runRight.nextFrame();
        
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
