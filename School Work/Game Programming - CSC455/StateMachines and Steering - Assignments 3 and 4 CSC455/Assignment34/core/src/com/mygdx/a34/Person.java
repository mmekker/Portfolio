package com.mygdx.a34;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * This is the abstract class for any Person object (Player, Enemy)
 * It holds Vector2 objects for position, velocity, and acceleration
 * It has methods for tick, update movement, and truncate. It requires
 * inheriting classes to implement a draw method.
 * Created for CSC455 - Game Programming, 4/4/2016
 * @author Mike Mekker
 */
public abstract class Person
{
	public Vector2 position;
	public Vector2 velocity;
	public Vector2 acceleration;
	public int maxSpeed;
	public boolean moving = false;
	
	public abstract void draw(ShapeRenderer sr);
	
	/**
	 * Updates Person object every iteration of the game loop
	 */
	public void tick()
	{
		updateMovement();
	}
	
	/**
	 * Updates acceleration, velocity, and position vectors while
	 * also limiting acceleration and velocity. If Person is not moving then
	 * it slows them down to zero.
	 */
	public void updateMovement()
	{
		acceleration = truncate(acceleration, 2);
		if(!moving)//slow down if not moving
		{
			if(velocity.x > 1)acceleration.x=-1;
			else if(velocity.x < -1)acceleration.x=1;
			else acceleration.x=0;velocity.x = 0;
			if(velocity.y > 1)acceleration.y=-1;
			else if(velocity.y < -1)acceleration.y=1;
			else acceleration.y=0;velocity.y=0;
		}
		velocity.add(acceleration);
		velocity = truncate(velocity,maxSpeed);
		position.add(velocity);
	}
	
	/**
	 * Limits a vector to a max value
	 * @param v - Vector to be limited
	 * @param max - Max value of vector v
	 * @return - Limited vector
	 */
	private Vector2 truncate(Vector2 v, float max)
	{
		v.limit(max);
		return v;
	}
}
