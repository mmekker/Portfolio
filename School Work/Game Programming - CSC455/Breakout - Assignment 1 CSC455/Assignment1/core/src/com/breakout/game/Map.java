package com.breakout.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/**
 * Map class
 * Contains a 2D array of bricks and can be drawn
 * @author Mike Mekker
 */
public class Map implements DrawnObject
{
	public String name;
	public Brick[][] map;
	public int rows;
	/**
	 * Creates a default map
	 */
	public Map()
	{
		name = "Level 0";
		rows = 10;
		map = new Brick[rows][10];
		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < rows; y++)
			{
				map[y][x] = new Brick(0,(x*70),660-(((y+2)*30)));
			}
		}
	}
	/**
	 * Accepts a map name and opens that map from the assets folder
	 * @param nName: Map name
	 */
	public Map(String nName)
	{
		name  = nName;
		rows = 0;
		produceMap(Gdx.files.internal(name + ".map"));
	}
	
	/**
	 * Accepts a file and creates bricks to put into the map
	 * @param file: Map file
	 */
	private void produceMap(FileHandle file)
	{
		String s = file.readString();
		for(int x = 0; x < s.length(); x++)
		{
			if(s.charAt(x) == '\n')
			{
				s = s.substring(0, x) + s.substring(x+1);
				x--;
			}
			if(s.charAt(x) == '\r')
			{
				rows++;
				s = s.substring(0, x) + s.substring(x+1);
				x--;
			}
			
		}
		rows++;
		map = new Brick[rows][10];
		for(int y = 0; y < rows; y++)
		{
			for(int x = 0; x < 10; x++)
			{
				map[y][x%10] = new Brick(Character.getNumericValue(s.charAt((y * 10) + x)),((x%10)*70),660-(((y+2)*30)));
			}
		}
	}
	
	/**
	 * Draws all bricks in the map
	 */
	public void draw(ShapeRenderer shapeRenderer)
	{
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				map[x][y].draw(shapeRenderer);
			}
		}
	}
}
