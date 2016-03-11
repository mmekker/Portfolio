package com.mygdx.jump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Floor
{
	public int x;
	public int y;
	public static int speed = -5;
	public static int height = 17;
	public static int width = 200;
	
	public Floor(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void tick()
	{
		y += speed;
	}

	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(x, y, width, height);
		sr.end();
	}
}
