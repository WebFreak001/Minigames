package com.naronco.minigames.snake;

import java.util.Random;
import java.util.Vector;

import com.deviotion.ld.eggine.math.Vector2d;

public class SnakeManager
{
    private int              ticksPerStep;
    private int              currentTick;
    private Random           random;
    public int[][]           field;
    private Vector2d         snakePos;
    private Vector<Vector2d> lastSnakePos;
    private int              dir     = 0;
    private int              lastDir = 0;
    private int              length  = 1;
    public boolean           dead;
    
    public SnakeManager(int ticksPerStep, int width, int height)
    {
        this.ticksPerStep = ticksPerStep;
        currentTick = 0;
        random = new Random();
        field = new int[width][height];
        snakePos = new Vector2d(width / 2, height / 2);
        lastSnakePos = new Vector<Vector2d>();
        lastSnakePos.add(snakePos.copy());
        field[width / 2][height / 2] = 1;
        spawnFood();
    }
    
    public void up()
    {
        if (lastDir == 1) return;
        dir = 3;
    }
    
    public void down()
    {
        if (lastDir == 3) return;
        dir = 1;
    }
    
    public void right()
    {
        if (lastDir == 2) return;
        dir = 0;
    }
    
    public void left()
    {
        if (lastDir == 0) return;
        dir = 2;
    }
    
    public void step()
    {
        snakePos.add(new Vector2d(dir == 0 ? 1 : (dir == 2 ? -1 : 0), dir == 3 ? -1 : (dir == 1 ? 1 : 0)));
        if (snakePos.getX() >= field.length) snakePos.setX(0);
        if (snakePos.getY() >= field[0].length) snakePos.setY(0);
        if (snakePos.getX() < 0) snakePos.setX(field.length - 1);
        if (snakePos.getY() < 0) snakePos.setY(field[0].length - 1);
        if (field[(int) snakePos.getX()][(int) snakePos.getY()] == 2)
        {
            field[(int) snakePos.getX()][(int) snakePos.getY()] = 1;
            length++;
            spawnFood();
        }
        else if (field[(int) snakePos.getX()][(int) snakePos.getY()] == 1)
        {
            dead = true;
        }
        field[(int) snakePos.getX()][(int) snakePos.getY()] = 1;
        lastSnakePos.add(snakePos.copy());
        while (lastSnakePos.size() > length)
        {
            field[(int) lastSnakePos.get(0).getX()][(int) lastSnakePos.get(0).getY()] = 0;
            lastSnakePos.remove(0);
        }
    }
    
    public void reset()
    {
        field = new int[field.length][field[0].length];
        dead = false;
        length = 1;
        dir = 0;
        snakePos = new Vector2d(field.length / 2, field[0].length / 2);
        lastSnakePos = new Vector<Vector2d>();
        lastSnakePos.add(snakePos.copy());
        field[field.length / 2][field[0].length / 2] = 1;
        spawnFood();
    }
    
    public void spawnFood()
    {
        while (true)
        {
            if (spawnFoodAt(random.nextInt(field.length), random.nextInt(field[0].length))) return;
        }
    }
    
    public boolean spawnFoodAt(int x, int y)
    {
        if (field[x][y] == 0)
        {
            field[x][y] = 2;
            return true;
        }
        return false;
    }
    
    public void update()
    {
        currentTick++;
        if (currentTick >= ticksPerStep)
        {
            step();
            lastDir = dir;
            currentTick = 0;
        }
    }
}
