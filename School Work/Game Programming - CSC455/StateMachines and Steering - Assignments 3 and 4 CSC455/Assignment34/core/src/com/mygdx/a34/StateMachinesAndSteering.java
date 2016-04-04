package com.mygdx.a34;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * This game implements various steering behaviors.
 * It uses vectors in order to simulate real world movement.
 * It also implements state machines for the enemy's behaviors.
 * Created for CSC455 - Game Programming, 4/4/2016
 * @author Mike Mekker
 */
public class StateMachinesAndSteering extends ApplicationAdapter
{
	SpriteBatch batch;
	ShapeRenderer sr;
	public static Person player;
	Person enemy;
	List<Person> people;
	
	boolean spacePressed = false;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		player = new Player();
		enemy = new Enemy();
		people = new ArrayList<Person>();
		people.add(enemy);
		people.add(player);
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		checkInputs();
		checkBounds(player);
		checkBounds(enemy);
		updatePeople(people);
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BROWN);
		sr.rect(500, 500, 200, 200);
		sr.end();
	}
	
	/**
	 * Update method for a list of Person objects.
	 * Each Person has tick() and draw() so that they are updated on screen.
	 * @param people2 - List of Person objects to be updated
	 */
	private void updatePeople(List<Person> people2)
	{
		for(Person p: people2)
		{
			p.tick();
			p.draw(sr);
		}
	}
	
	/**
	 * Checks for if the user has pressed any of the input keys:
	 * Left - Increases the players x acceleration
	 * Right - Decreases the players x acceleration
	 * Up - Increases the players y acceleration
	 * Down - Decreases the players y acceleration
	 */
	private void checkInputs()
	{
		float accelerationSpeed = 0.2f;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			player.acceleration.add(new Vector2(-accelerationSpeed,0));
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			player.acceleration.add(new Vector2(accelerationSpeed,0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP))
		{
			player.acceleration.add(new Vector2(0,accelerationSpeed));
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			player.acceleration.add(new Vector2(0,-accelerationSpeed));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || 
				Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
				Gdx.input.isKeyPressed(Input.Keys.UP) ||
				Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			player.moving = true;
		}
		else
			player.moving = false;
	}
	
	/**
	 * Takes a Person object and checks if it is out of bounds.
	 * If so it moves them back in bounds
	 * @param p - Person who's bounds are being checked
	 */
	public void checkBounds(Person p)
	{
		//Window Bounds
		if(p.position.x < 0)p.position.x=0;
		if(p.position.x > 590)p.position.x=590;
		if(p.position.y < 0)p.position.y=0;
		if(p.position.y > 590)p.position.y=590;
		//Building Bounds
		if(p.position.x > 500 && p.position.y > 500
				&& p.position.y < 510)
			p.position.y = 500;
		if(p.position.x > 500 && p.position.y > 500
				&& p.position.x < 510)
			p.position.x = 500;
			
	}
}
