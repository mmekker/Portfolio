package com.mygdx.jump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class StartScreen extends Screen
{
	public StartScreen()
	{
		name = "Start";
	}
	
	@Override
	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(0, 0, 880, 800);
		sr.end();
	}

}
