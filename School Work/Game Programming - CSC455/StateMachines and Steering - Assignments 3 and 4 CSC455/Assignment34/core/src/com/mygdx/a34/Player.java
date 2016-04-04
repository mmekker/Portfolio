package com.mygdx.a34;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * This is the Player class. It is a small extension
 * of the Person class. Instantiates vectors and maxSpeed and
 * implements draw()
 * Created for CSC455 - Game Programming, 4/4/2016
 * @author Mike Mekker
 */
public class Player extends Person
{
	/**
	 * Default constructor for Player
	 */
	public Player()
	{
		this.position = new Vector2(100,300);
		this.velocity = new Vector2();
		this.acceleration = new Vector2();
		maxSpeed = 5;
	}
	
	/**
	 * Draws the Player as a blue circle with a lime green
	 * line the correlates to the player's velocity.
	 */
	@Override
	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLUE);
		sr.circle(this.position.x, this.position.y, 10);
		sr.setColor(Color.LIME);
		sr.rectLine(position.x, position.y, position.x + (velocity.x*5) + 2, position.y + (velocity.y*5) + 2, 3);
		sr.end();
	}
	
}
