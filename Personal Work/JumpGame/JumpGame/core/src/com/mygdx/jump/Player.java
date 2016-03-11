package com.mygdx.jump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player
{
	public int x;
	public int y;
	public int xSpeed;
	public int ySpeed;
	public int yAccel;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 100;
	public int height = 100;
	public int width = 50;
	public boolean canJump;
	
	public Player()
	{
		x = 400;
		y = 200;
		xSpeed = 0;
		ySpeed = 0;
		canJump = true;
	}
	
	public void tick()
	{
		x += xSpeed;
		y += ySpeed;
		ySpeed += yAccel;
		yAccel -= 1;
		if(yAccel < -5) yAccel = -5;
	}
	
	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.CYAN);
		sr.rect(x, y, WIDTH, HEIGHT);
		sr.end();
	}
}
