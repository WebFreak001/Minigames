package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.map.Map;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Screen {

    private Dimension2d dimension;
    private BufferedImage bufferedImage;
    private int[] pixels;

    public Screen (Dimension2d dimension) {
        this.dimension = dimension;
        this.bufferedImage = new BufferedImage((int) this.dimension.getWidth
                (), (int) this.dimension.getHeight(), BufferedImage
                .TYPE_INT_RGB);
        this.pixels = ((DataBufferInt) this.bufferedImage.getRaster()
                .getDataBuffer()).getData();
    }

    public Dimension2d getDimension () {
        return this.dimension;
    }
    public BufferedImage getBufferedImage () {
        return this.bufferedImage;
    }
    public int[] getPixels () {
        return this.pixels;
    }

    public void renderSprite (int x, int y, int startX, int startY, int endX,
                             int endY, Sprite sprite) {
        BufferedImage spriteImage = sprite.getBufferedImage();

        if (startX > sprite.getDimension().getWidth()) startX =
                (int) sprite.getDimension().getWidth();
        if (startY > sprite.getDimension().getHeight()) startY =
                (int) sprite.getDimension().getHeight();

        if (endX > (sprite.getDimension().getWidth() - startX)) endX =
                ((int) sprite.getDimension().getWidth()
                        - startX);
        if (endY > (sprite.getDimension().getHeight() - startY)) endY =
                ((int) sprite.getDimension().getHeight()
                        - startY);


        for (int i = startX; i < startX + endX; i++) {
            for (int j = startY; j < startY + endY; j++) {
            	if(i >= spriteImage.getWidth() || i < 0 || j >= spriteImage.getHeight() || j < 0)
            		continue;
                int pixelColor = spriteImage.getRGB(i, j);
                if (pixelColor != sprite.getTransparentColor()) {
                    this.setPixel(x + (i - startX), y + (j - startY),
                            pixelColor);
                }
            }
        }
    }
    public void renderSprite (int x, int y, Sprite sprite) {
        this.renderSprite(x, y, 0, 0, (int) sprite.getDimension().getWidth(),
                (int) sprite.getDimension().getHeight(), sprite);
    }
    public void renderSprite (Vector2d location, Sprite sprite) {
        this.renderSprite((int) location.getX(), (int) location.getY(), sprite);
    }
    public void renderSprite (Vector2d location, Vector2d startLocation,
                             Dimension2d endLocation, Sprite sprite) {
        this.renderSprite((int) location.getX(), (int) location.getY(),
                (int) startLocation.getX(), (int) startLocation.getY(),
                (int) endLocation.getWidth(), (int) endLocation.getHeight(),
                sprite);
    }
    public void renderSprite (Vector2d location, int startX, int startY,
                             int endX, int endY, Sprite sprite) {
        this.renderSprite((int) location.getX(), (int) location.getY(), startX,
                startY, endX, endY, sprite);
    }
    public void renderSprite (int x, int y, Vector2d startLocation, int endX,
                             int endY, Sprite sprite) {
        this.renderSprite(x, y, (int) startLocation.getX(),
                (int) startLocation.getY(), endX, endY, sprite);
    }
    public void renderSprite (int x, int y, int startX, int startY,
                             Dimension2d endLocation, Sprite sprite) {
        this.renderSprite(x, y, startX, startY, (int) endLocation.getWidth(),
                (int) endLocation.getHeight(), sprite);
    }
    public void renderSprite (Vector2d location, Vector2d startLocation,
                             int endX, int endY, Sprite sprite) {
        this.renderSprite((int) location.getX(), (int) location.getY(), (int)
                startLocation.getX(), (int) startLocation.getY(), endX, endY,
                sprite);
    }
    public void renderSprite (Vector2d location, int startX, int startY,
                              Dimension2d endLocation, Sprite sprite) {
        this.renderSprite((int) location.getX(), (int) location.getY(),
                startX, startY, (int) endLocation.getWidth(), (int) endLocation
                        .getHeight(), sprite);
    }
    public void renderSprite (int x, int y, Vector2d startLocation,
                             Dimension2d endLocation, Sprite sprite) {
        this.renderSprite(x, y, (int) startLocation.getX(), (int)
                        startLocation.getY(), (int) endLocation.getWidth(),
                (int) endLocation.getHeight(), sprite);
    }
    public void setPixel (int x, int y, int color) {
        if (x >= 0 && y >= 0 && x < this.dimension.getWidth() && y < this
                .dimension.getHeight()) {
            this.pixels[x + y * (int) this.dimension.getWidth()] = color;
        }
    }

    public void renderSpriteTile(int x, int y, SpriteSheet spriteSheet,
                                 int tile) {
        Vector2d location = spriteSheet.getTileVector(tile);

        this.renderSprite(x, y, location, spriteSheet
                .getSpriteSize(), spriteSheet.getSprite());
    }
    public void renderSpriteTile(Vector2d vector, int tile, SpriteSheet
            spriteSheet) {
        this.renderSpriteTile((int) vector.getX(), (int) vector.getY(),
                spriteSheet, tile);
    }

    public void renderAnimatedSprite (int x, int y, SpriteAnimation
                                      spriteAnimation) {
        this.renderSpriteTile(x, y, spriteAnimation.getSpriteSheet(),
                spriteAnimation.getTile());
    }
    public void renderAnimatedSprite (Vector2d vector, SpriteAnimation
            spriteAnimation) {
        this.renderAnimatedSprite((int) vector.getX(), (int) vector.getY(),
                spriteAnimation);
    }

    public void renderRectangle (int x, int y, int width, int height, int
            color) {
        for (int i=x; i<width; i++) {
            for (int j=y; j<height; j++) {
                this.setPixel(i, j, color);
            }
        }
    }
    public void renderRectangle (Vector2d location, int width, int height,
                                 int color) {
        this.renderRectangle((int) location.getX(), (int) location.getY(),
                width, height, color);
    }
    public void renderRectangle (int x, int y, Dimension2d dimension, int
            color) {
        this.renderRectangle(x, y, (int) dimension.getWidth(), (int)
                dimension.getHeight(), color);
    }
    public void renderRectangle (Vector2d location, Dimension2d dimension,
                                 int color) {
        this.renderRectangle((int) location.getX(), (int) location.getY(),
                (int) dimension.getWidth(), (int) dimension.getHeight(), color);
    }

    public void renderMap (int x, int y, Map map, SpriteSheet spriteSheet) {
        for (int i=0; i<map.getDimension().getWidth(); i++) {
            for (int j=0; j<map.getDimension().getHeight(); j++) {
                int tile = map.getTile(i, j);
                if (tile != -1) {
                    this.renderSpriteTile(x + (i * (int) spriteSheet
                                    .getSpriteSize().getWidth()), y +
                                    (j * (int)
                                            spriteSheet.getSpriteSize()
                                                    .getHeight()),
                            spriteSheet, tile);
                }
            }
        }
    }

    public void fillScreen (int color) {
        Arrays.fill(this.pixels, color);
    }

}