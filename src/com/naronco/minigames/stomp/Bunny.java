package com.naronco.minigames.stomp;

import java.util.Vector;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteAnimation;
import com.deviotion.ld.eggine.graphics.SpriteSheet;

public class Bunny
{
    SpriteAnimation sitLeft;
    SpriteAnimation runLeft;
    SpriteAnimation sitRight;
    SpriteAnimation runRight;
    Sprite          deadBunny;
    public float    xa;
    public float    x;
    boolean         looksLeft;
    boolean         inLog;
    ParticleSystem  particles;
    boolean         isDead;
    
    public Bunny(SpriteSheet sit, SpriteSheet run, Sprite dead)
    {
        sitLeft = new SpriteAnimation(sit, 0, 1, 1);
        runLeft = new SpriteAnimation(run, 0, 1, 12);
        sitRight = new SpriteAnimation(sit, 2, 3, 1);
        runRight = new SpriteAnimation(run, 2, 3, 12);
        deadBunny = dead;
        xa = 0;
        x = 140;
        looksLeft = false;
        particles = new ParticleSystem();
        isDead = false;
    }
    
    void render(Screen screen)
    {
        if (isDead)
        {
            screen.renderSprite(Math.round(x), 126, deadBunny);
        }
        else
        {
            if (Math.abs(xa) > 0.3f) screen.renderAnimatedSprite(Math.round(x), 96, looksLeft ? runLeft : runRight);
            else screen.renderAnimatedSprite(Math.round(x), 96, looksLeft ? sitLeft : sitRight);
        }
    }
    
    void renderParticles(Screen screen)
    {
        particles.update();
        particles.draw(screen);
    }
    
    void move(Vector<Log> logs, boolean left, boolean right)
    {
        if (left ^ right && !isDead)
        {
            if (left)
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
            if (right)
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
        }
        
        xa *= 0.8f;
        x += xa;
    }
    
    public boolean dead()
    {
        return isDead;
    }
    
    public void kill()
    {
        particles.spawn((int) x, 128, 30);
        isDead = true;
    }
    
    public void checkLog(Log log)
    {
        if (log.collides(x))
        {
            xa = 0;
            if (log.isDeadly())
            {
                kill();
            }
        }
    }
    
    public void update()
    {
        sitLeft.nextFrame();
        runLeft.nextFrame();
        sitRight.nextFrame();
        runRight.nextFrame();
    }
}
