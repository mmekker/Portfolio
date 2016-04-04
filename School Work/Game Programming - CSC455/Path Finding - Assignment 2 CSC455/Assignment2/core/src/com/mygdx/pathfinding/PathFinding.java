package com.mygdx.pathfinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Path Finding program for CSC455 Choose a start and end point and the program
 * finds and draws the shortest path between them using dijkstra's path finding
 * algorithm.
 * 
 * @author Mike Mekker
 */
public class PathFinding extends ApplicationAdapter
{
	/* Declare variables */
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer sr;
	Texture img;
	Grid grid;
	boolean newPath = false;
	
	@Override
	public void create()
	{
		/* Instantiate variables */
		batch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		/* Random grid */
		//grid = new Grid(20, 20);
		/* Grid file input */
		grid = new Grid("Grid2");
	}
	
	/* Game loop */
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		/* Check for user input */
		checkInputs();
		/* Finds new path if the start or end points are moved */
		if (newPath)
		{
			grid.findPath();
			newPath = false;
		}
		/* Draw grid and path */
		grid.draw(sr, batch, font);
	}
	
	/**
	 * Check for user inputs from the keyboard and mouse Left, Right, Up, Down -
	 * Move grid Mouse click - select start or end
	 */
	public void checkInputs()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			grid.Xmargin += -5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			grid.Xmargin += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
		{
			grid.Ymargin += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			grid.Ymargin += -5;
		}
		if (Gdx.input.justTouched())
		{
			int mouseX = Gdx.input.getX();
			int mouseY = 600 - Gdx.input.getY();
			int x = ((mouseX - grid.Xmargin)) / (Grid.tileSize + 1);
			int y = ((mouseY - grid.Ymargin)) / (Grid.tileSize + 1);
			if (grid.changeEnd)
			{
				if (x >= 0 && x < grid.xLen && y >= 0 && y < grid.yLen && !grid.grid[x][y].getWeight().equals("F"))
				{
					grid.setEndX(x);
					grid.setEndY(y);
					newPath = true;
					grid.changeEnd = false;
				}
			}
			else if (grid.changeStart)
			{
				if (x >= 0 && x < grid.xLen && y >= 0 && y < grid.yLen && !grid.grid[x][y].getWeight().equals("F"))
				{
					grid.setStartX(x);
					grid.setStartY(y);
					newPath = true;
					grid.changeStart = false;
				}
			}
			else
			{
				if (mouseX > 0 && mouseX < 100 && mouseY > 0 && mouseY < 25)
				{
					grid.changeStart = !grid.changeStart;
					grid.changeEnd = false;
				}
				else if (mouseX > 500 && mouseX < 600 && mouseY > 0 && mouseY < 25)
				{
					grid.changeEnd = !grid.changeEnd;
					grid.changeStart = false;
				}
			}
		}
	}
}
