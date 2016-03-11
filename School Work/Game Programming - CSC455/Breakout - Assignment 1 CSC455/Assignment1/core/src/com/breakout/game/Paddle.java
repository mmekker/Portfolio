package com.breakout.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Paddle Class
 * @author Mike Mekker
 */
public class Paddle implements DrawnObject
{
	public int x;
	public int y;
	public int speed;
	public int width = 150;
	public int height = 15;
	public static final int startX = 220;
	public static final int startY = 50;
	public static final Color color = Color.GRAY;
	
	/**
	 * Default Paddle
	 */
	public Paddle()
	{
		x = startX;
		y = startY;
		speed = 0;
	}
	/**
	 * Resets the Paddle to the starting X and Y
	 */
	public void reset()
	{
		x = startX;
		y = startY;
		speed = 0;
	}
	
	/**Getters and Setters**/
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Draws the Paddle
	 */
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(this.getX(), this.getY(), this.width, this.height);
		shapeRenderer.setColor(Paddle.color);
		shapeRenderer.rect(this.getX()+1, this.getY()+1, this.width-2, this.height-2);
		shapeRenderer.end();
	}
}
