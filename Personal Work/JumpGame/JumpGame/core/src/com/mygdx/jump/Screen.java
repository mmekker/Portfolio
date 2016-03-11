package com.mygdx.jump;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen
{
	public String name;
	public abstract void draw(ShapeRenderer sr);
}
