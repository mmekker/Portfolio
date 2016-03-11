package com.mygdx.jump;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class JumpGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer sr;
	Texture img;
	Player player = new Player();
	int floorSpeed = -3;
	ArrayList<Floor> floors;
	Screen screen;
	int score;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		screen = new StartScreen();
		floors = new ArrayList<Floor>();
		score = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(192/255f, 192/255f, 192/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		checkInputs();
		screen.draw(sr);
		if(screen.name.equals("Play"))
		{
			checkBounds();
			checkCollision();
			player.draw(sr);
			player.tick();
			for(Floor f: floors)
			{
				f.draw(sr);
				f.tick();
			}
			score++;
			if(score % 1000 == 0)
				Floor.speed--;
		}
		batch.begin();
		font.setColor(Color.YELLOW);
		font.draw(batch, "Score: " + score, 40, 650);
		batch.end();
	}
	
	public void checkInputs()
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			if(player.canJump)
			{
				player.y += 100;
				player.canJump = false;
				player.yAccel = 3;
				player.ySpeed = 30;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			player.xSpeed = -15;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			player.xSpeed = 15;
		}
		else
		{
			player.xSpeed = 0;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
		{
			if(screen.name.equals("Start"))
			{
				score = 0;
				Floor.speed = -3;
				screen = new PlayScreen();
				floors = new ArrayList<Floor>();
				player.x = 30;
				player.y = 700;
				floors.add(new Floor(0,700));
				floors.add(new Floor(210,700));
				
				floors.add(new Floor(randomX(),1100));
				floors.add(new Floor(randomX(),1100));
			}
			else if(screen.name.equals("Game Over"))
			{
				screen = new StartScreen();
			}
		}
	}
	public void checkBounds()
	{
		if(player.y < -20)
		{
			screen = new GameOverScreen();
		}
		if(player.x < 0)player.x = 0;
		if(player.x+player.width > 700)player.x = 700 - player.width;
		for(int x = 0; x < floors.size(); x++)
		{
			if(floors.get(x).y < -20)
			{
				floors.remove(floors.get(x));
				floors.add(new Floor(randomX(),700));
				x--;
			}
		}
	}
	public void checkCollision()
	{
		for(Floor f: floors)
		{
			if(player.y > f.y-10 && player.y < f.y+Floor.height+10
					&& player.x+player.width > f.x && player.x < f.x + Floor.width
					&& player.ySpeed < 0)
			{
				player.ySpeed = Floor.speed;
				player.yAccel = 0;
				player.y = f.y+Floor.height;
				player.canJump = true;
				break;
			}
		}
	}
	public int randomX()
	{
		int i = (int)(Math.random()*3);
		switch(i)
		{
			case 0:
				return 0;
			case 1:
				return 210;
			case 2:
				return 420;
		}
		return 0;
	}
}
