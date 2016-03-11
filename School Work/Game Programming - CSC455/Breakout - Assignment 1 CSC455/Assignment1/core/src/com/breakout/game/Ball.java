package com.breakout.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Ball class
 * @author Mike Mekker
 */
public class Ball implements DrawnObject
{
	public int x;
	public int y;
	public int xSpeed;
	public int ySpeed;
	public static final int startX = 300;
	public static final int startY = 90;
	public static final int radius = 12;
	public static final Color color = Color.WHITE;
	
	/**
	 * Creates default ball
	 */
	public Ball()
	{
		x = startX;
		y = startY;
		xSpeed = 7;
		ySpeed = 10;
	}
	/**
	 * Resets the ball to the start X and Y positions
	 */
	public void reset()
	{
		x = startX;
		y = startY;
		xSpeed = 7;
		ySpeed = 10;
	}
	
	/**
	 * Draws the ball
	 */
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(this.x, this.y, Ball.radius);
		shapeRenderer.setColor(Ball.color);
		shapeRenderer.circle(this.x, this.y, Ball.radius-1);
		shapeRenderer.end();
	}
}
