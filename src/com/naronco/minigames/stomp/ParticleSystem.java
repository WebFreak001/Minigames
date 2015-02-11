package com.naronco.minigames.stomp;

import java.util.Random;
import java.util.Vector;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;

class ParticleInfo
{
    public ParticleInfo(int x, int y, double fx, double fy)
    {
        position = new Vector2d(x, y);
        velocity = new Vector2d(fx, fy);
    }
    
    public Vector2d position;
    public Vector2d velocity;
}

public class ParticleSystem
{
    int                  time;
    int                  x, y;
    Vector<ParticleInfo> particles = new Vector<ParticleInfo>();
    Random               random    = new Random();
    
    public ParticleSystem()
    {
    }
    
    public void spawn(int x, int y, int time)
    {
        this.time = time;
        this.x = x;
        this.y = y;
    }
    
    public void draw(Screen screen)
    {
        for(ParticleInfo particle : particles)
            screen.setPixel((int)particle.position.getX(), (int)particle.position.getY(), 0xFF0000);
    }
    
    public void update()
    {
        if (time > 0)
        {
            particles.add(new ParticleInfo(x, y, random.nextDouble() * 2 - 1, -random.nextDouble() * 2));
            particles.add(new ParticleInfo(x, y, random.nextDouble() * 2 - 1, -random.nextDouble() * 2));
            particles.add(new ParticleInfo(x, y, random.nextDouble() * 2 - 1, -random.nextDouble() * 2));
            particles.add(new ParticleInfo(x, y, random.nextDouble() * 2 - 1, -random.nextDouble() * 2));
            particles.add(new ParticleInfo(x, y, random.nextDouble() * 2 - 1, -random.nextDouble() * 2));
            while(particles.size() > 1000)
                particles.remove(0);
            time--;
        }

        for(ParticleInfo particle : particles)
        {
            particle.position.add(particle.velocity);
            particle.velocity.add(new Vector2d(0, 0.08));
            particle.position.setY(Math.min(particle.position.getY(), 128));
            if(particle.position.getY() > 127)
                particle.velocity.setX(0);
        }
    }
}
