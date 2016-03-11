package com.breakout.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
/**
 * Brick Class
 * @author Mike Mekker
 */
public class Brick implements DrawnObject
{
	private int hits;
	private Color c;
	private int x;
	private int y;
	public final static int width = 70;
	public final static int height = 30;
	/**
	 * Default Brick object
	 */
	public Brick()
	{
		hits = 1;
		changeColor();
		x = 0;
		y = 0;
	}
	/**
	 * Custom Brick Object
	 * @param h: Number of hits
	 * @param xn: X Coordinate
	 * @param yn: Y Coordinate
	 */
	public Brick(int h, int xn, int yn)
	{
		hits = h;
		changeColor();
		x = xn;
		y = yn;
	}
	/**Getters and Setters**/
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public Color getC() {
		return c;
	}
	public void setC(Color c) {
		this.c = c;
	}
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
	
	public void incHits()
	{
		hits++;
		if(hits > 4)
			hits = 0;
		changeColor();
	}
	
	/**
	 * Changes Color based on the number of hits it has left
	 * 1: Red
	 * 2: Blue
	 * 3: Green
	 * 4: Purple
	 */
	private void changeColor()
	{
		switch(hits)
		{
			case 1:
			{
				c = Color.RED;
				break;
			}
			case 2:
			{
				c = Color.BLUE;
				break;
			}
			case 3:
			{
				c = Color.GREEN;
				break;
			}
			case 4:
			{
				c = Color.PURPLE;
				break;
			}
			default:
			{
				c = Color.RED;
				break;
			}
		}
	}
	
	/**
	 * Decrements the hits counter
	 * chenges color accordingly
	 */
	public void gotHit()
	{
		hits--;
		changeColor();
	}
	
	/**
	 * Draws the brick
	 */
	public void draw(ShapeRenderer shapeRenderer)
	{
		if(this.getHits() > 0)
		{
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.rect(this.getX(), this.getY(), Brick.width, Brick.height);
			shapeRenderer.setColor(this.getC());
			shapeRenderer.rect(this.getX()+1, this.getY()+1, Brick.width-2, Brick.height-2);
			shapeRenderer.end();
		}
	}
	
	/**
	 * Returns a copy of this
	 * Used for copying map
	 */
	public Brick clone()
	{
		Brick b = new Brick();
		b.hits = this.hits;
		b.c = this.c;
		b.x = this.x;
		b.y = this.y;
		return b;
	}
}
