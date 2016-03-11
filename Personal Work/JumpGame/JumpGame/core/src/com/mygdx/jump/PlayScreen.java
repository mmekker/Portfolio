package com.mygdx.jump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class PlayScreen extends Screen
{
	public PlayScreen()
	{
		name = "Play";
	}
	
	@Override
	public void draw(ShapeRenderer sr)
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.LIGHT_GRAY);
		sr.rect(0, 0, 880, 800);
		sr.end();
	}

}
