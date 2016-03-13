package com.mygdx.pathfinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PathFinding extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer sr;
	Texture img;
	Grid grid;
	boolean newPath = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		//grid = new Grid(100,100);
		grid = new Grid("Grid2");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		checkInputs();
		checkBounds();
		if(newPath)
		{
			grid.findPath();
			newPath = false;
		}
		grid.draw(sr, batch, font);
	}
	
	public void checkInputs()
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){grid.Xmargin += -5;}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){grid.Xmargin += 5;}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){grid.Ymargin += 5;}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){grid.Ymargin += -5;}
		if(Gdx.input.justTouched())
		{
			int mouseX = Gdx.input.getX();
			int mouseY = 600 - Gdx.input.getY();
			int x = ((mouseX - grid.Xmargin))/(Grid.tileSize +1);
			int y = ((mouseY - grid.Ymargin))/(Grid.tileSize +1);
			if(grid.changeEnd)
			{
				if(x >= 0 &&  x < grid.xLen && y >= 0 &&  y < grid.yLen
						&& !grid.grid[x][y].getWeight().equals("F"))
				{
					grid.setEndX(x);
					grid.setEndY(y);
					newPath = true;
					grid.changeEnd = false;
				}
			}
			else if(grid.changeStart)
			{
				if(x >= 0 &&  x < grid.xLen && y >= 0 &&  y < grid.yLen
						&& !grid.grid[x][y].getWeight().equals("F"))
				{
					grid.setStartX(x);
					grid.setStartY(y);
					newPath = true;
					grid.changeStart = false;
				}
			}
			else
			{
				if(mouseX > 0 && mouseX < 100
						&& mouseY > 0 && mouseY < 25)
				{
					grid.changeStart = !grid.changeStart;
					grid.changeEnd = false;
				}
				else if(mouseX > 500 && mouseX < 600
						&& mouseY > 0 && mouseY < 25)
				{
					grid.changeEnd = !grid.changeEnd;
					grid.changeStart = false;
				}
			}
		}
	}
	public void checkBounds()
	{
		if(grid.Xmargin*-1 > grid.xLen*Grid.tileSize)grid.Xmargin = grid.xLen*Grid.tileSize*-1;
		if(grid.Xmargin > 590)grid.Xmargin = 590;
		if(grid.Ymargin*-1 > grid.yLen*Grid.tileSize -30)grid.Ymargin = grid.yLen*Grid.tileSize*-1 +30;
		if(grid.Ymargin > 590)grid.Ymargin = 590;
	}
}
