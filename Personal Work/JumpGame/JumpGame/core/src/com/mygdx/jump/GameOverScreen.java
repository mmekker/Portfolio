package com.mygdx.jump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameOverScreen extends Screen
{
	public GameOverScreen()
	{
		name = "Game Over";
	}
	
	@Override
	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.RED);
		sr.rect(0, 0, 880, 800);
		sr.end();
	}

}