package com.mygdx.a34;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * This is the Enemy class. It extends Person so therefore has vectors
 * for position, velocity, and acceleration and moves using those vectors.
 * The enemy class also has a state variable which can be either:
 * SENTRY - Looking for intruders
 * CHASE - Pursuing an intruder
 * RETURN - Returning to his post
 * Created for CSC455 - Game Programming, 4/4/2016
 * @author Mike Mekker
 */
public class Enemy extends Person
{
	private State state;
	public double fov = 0.85;
	public double fovLength = 200;
	public double direction;
	private Color FOVCOLOR = Color.YELLOW;
	private final Vector2 originalPosition = new Vector2(480,480);
	private double turnDirection = 0.02;
	private int chaseTimer = 0;
	
	Vector2 point1, point2, point3;
	
	/**
	 * Default constructor for enemy
	 */
	public Enemy()
	{
		this.position = originalPosition.cpy();
		this.velocity = new Vector2();
		this.acceleration = new Vector2();
		maxSpeed = 3;
		state = State.SENTRY;

		point1 = new Vector2(0,0);
		point2 = new Vector2(0,0);
		point3 = new Vector2(0,0);
	}
	
	/**
	 * Overrides the tick method from Person so states
	 * can be implemented.
	 */
	@Override
	public void tick()
	{
		if(state == State.SENTRY)
		{
			moving = false;
			this.velocity.x = 0; this.velocity.y = 0;
			this.acceleration.x = 0; this.acceleration.y = 0;
			if(direction > 0 || direction < -4.71)
				turnDirection *= -1;
			direction += turnDirection;
			if(playerSpotted())
			{
				StateTransition(State.CHASE);
			}
		}
		else if(state == State.CHASE)
		{
			chaseTimer += 1;
			moving = true;
			if(!playerSpotted() && chaseTimer > 20)
			{
				StateTransition(State.RETURN);
			}
			else
			{
				Player player = (Player) StateMachinesAndSteering.player;
				Vector2 desired_velocity = position.cpy().sub(player.position.cpy()).nor();
				desired_velocity.x *= -maxSpeed; desired_velocity.y *= -maxSpeed;
				acceleration = desired_velocity.sub(velocity);
			}
		}
		else if(state == State.RETURN)
		{
			moving = true;
			if(playerSpotted())
			{
				StateTransition(State.CHASE);
			}
			if(position.x < (originalPosition.cpy().x)+10 &&
					position.x > (originalPosition.cpy().x)-10 &&
					position.y < (originalPosition.cpy().y)+10 &&
					position.y > (originalPosition.cpy().y)-10)
			{
				StateTransition(State.SENTRY);
			}
			Vector2 desired_velocity = originalPosition.cpy().sub(position.cpy()).nor();
			desired_velocity.x *= maxSpeed; desired_velocity.y *= maxSpeed;
			acceleration = desired_velocity.cpy().sub(velocity);
		}
		updateMovement();
	}
	
	/**
	 * Changes state value while making changes
	 * needed for the different states.
	 * @param s - State to be changed to
	 */
	private void StateTransition(State s)
	{
		state = s;
		if(state == State.SENTRY)
		{
			position = this.originalPosition.cpy();
			velocity = new Vector2();
			acceleration = new Vector2();
			direction = 0;
			moving = false;
		}
		else if(state == State.CHASE)
		{
			chaseTimer = 0;
			Player player = (Player) StateMachinesAndSteering.player;
			Vector2 desired_velocity = position.cpy().sub(player.position.cpy()).nor();
			desired_velocity.x *= -maxSpeed; desired_velocity.y *= -maxSpeed;
			acceleration = desired_velocity.sub(velocity);
			moving = true;
		}
	}
	
	/**
	 * Draws the enemy as a red circle with a lime green
	 * line indication direction and velocity. It also draws
	 * a yellow a black gradient triangle to represent the enemy's
	 * field of view.
	 */
	@Override
	public void draw(ShapeRenderer sr)
	{
		//set direction to velocity if moving
		if(moving)
		{
			direction = velocity.angleRad(new Vector2(0,1));
		}
		point1.x = position.x;
		point1.y = position.y;
		point2.x = ((position.x)+(float)(Math.sin(direction-fov) * fovLength));
		point2.y = ((position.y)+(float)(Math.cos(direction-fov) * fovLength));
		point3.x = ((position.x)+(float)(Math.sin(direction+fov) * fovLength));
		point3.y = ((position.y)+(float)(Math.cos(direction+fov) * fovLength));
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.RED);
		sr.circle(this.position.x, this.position.y, 10);
		
		sr.triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y, FOVCOLOR, Color.BLACK, Color.BLACK);
		sr.setColor(Color.LIME);
		if(moving)
		{
			sr.rectLine(position.x, position.y, position.x + (velocity.x*7) + 2, position.y + (velocity.y*7) + 2, 3);
		}
		else
		{
			sr.rectLine(position.x, position.y, (position.x) + (float)(Math.sin(direction) * 20), (position.y)+(float)(Math.cos(direction) * 20), 3);
		}
		sr.end();
	}
	
	/**
	 * Checks if the player is inside of the enemies field of view.
	 * Determines if the enemy will start to chase the player.
	 * @return
	 */
	public boolean playerSpotted()
	{
		Player player = (Player) StateMachinesAndSteering.player;
		return PointInTriangle(player.position, point1, point2, point3);
	}
	
	/**
	 * Overrides the updateMovement method in order to
	 * change the acceleration value the enemy's acceleration
	 * is truncated to.
	 */
	@Override
	public void updateMovement()
	{
		acceleration = truncate(acceleration, 0.15f);
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
	
	/**
	 * The "sign" and "PointInTriangle" methods were found online at:
	 * "http://www.gamedev.net/topic/295943-is-this-a-better-point-in-triangle-test-2d/"
	 */
	float sign (Vector2 p1, Vector2 p2, Vector2 p3)
	{
	    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	boolean PointInTriangle (Vector2 pt, Vector2 v1, Vector2 v2, Vector2 v3)
	{
	    boolean b1, b2, b3;

	    b1 = sign(pt, v1, v2) < 0.0f;
	    b2 = sign(pt, v2, v3) < 0.0f;
	    b3 = sign(pt, v3, v1) < 0.0f;

	    return ((b1 == b2) && (b2 == b3));
	}
}
