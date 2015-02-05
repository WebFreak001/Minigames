package com.deviotion.ld.eggine.math;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Vector2d {

    private double x;
    private double y;

    public Vector2d (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX () {
        return this.x;
    }
    public double getY () {
        return this.y;
    }

    public void setX (double x) {
        this.x = x;
    }
    public void setY (double y) {
        this.y = y;
    }

    public double distance (Vector2d vector) {
        double distX = vector.getX() - this.x;
        double distY = vector.getY() - this.y;

        return Math.sqrt(distX * distX + distY * distY);
    }

    public Vector2d add (Vector2d vector) { // CHANGE
        this.x += vector.getX();
        this.y += vector.getY();
        return this; // CHANGE
    }
    
    public Vector2d multiply (double v) { // ADDITION
    	this.x *= v;
    	this.y *= v;
    	return this;
    }
    
    public double getLength () { // ADDITION
    	return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    
    public Vector2d normalized () { // ADDITION
    	return new Vector2d(this.x, this.y).multiply(1.0 / this.getLength());
    }
    
    public Vector2d copy () { // ADDITION
    	return new Vector2d(this.x, this.y);
    }
    
    public double dot (Vector2d vector) { // ADDITION
    	return this.x * vector.x + this.y * vector.y;
    }

    public static Vector2d reflect (Vector2d i, Vector2d n) { // ADDITION
    	Vector2d in = i.normalized();
    	Vector2d normal = n.normalized();
    	return in.copy().add(normal.copy().multiply(-2).multiply(normal.dot(in)));
    }
}