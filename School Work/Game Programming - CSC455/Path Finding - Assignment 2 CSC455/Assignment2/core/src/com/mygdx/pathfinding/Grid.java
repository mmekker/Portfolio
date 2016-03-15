package com.mygdx.pathfinding;

import java.util.ArrayList;
import java.util.Scanner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Grid Class for path finding program for CSC455 This is a grid of tiles in
 * which the shortest path between 2 points is calculated
 * 
 * @author Mike Mekker
 */
public class Grid
{
	public Tile grid[][];
	Tile startTile;
	Tile endTile;
	int xLen;
	int yLen;
	int startX;
	int startY;
	int endX;
	int endY;
	public int Xmargin = 0;
	public int Ymargin = 30;
	public static int tileSize = 25;
	private ArrayList<Tile> open;
	private ArrayList<Tile> closed;
	public boolean changeStart = false;
	public boolean changeEnd = false;
	
	/**
	 * Grid constructor Builds grid from file ("filename.grid")
	 * 
	 * @param fileName
	 *            - Name of grid file
	 */
	public Grid(String fileName)
	{
		produceGrid(Gdx.files.internal(fileName + ".grid"));
	}
	
	/**
	 * Makes a random grid of size xL, yL
	 * 
	 * @param xL
	 *            - X Length
	 * @param yL
	 *            - Y Length
	 */
	public Grid(int xL, int yL)
	{
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		xLen = xL;
		yLen = yL;
		grid = new Tile[xLen][yLen];
		open = new ArrayList<Tile>();
		closed = new ArrayList<Tile>();
		for (int x = 0; x < xLen; x++)
		{
			for (int y = 0; y < yLen; y++)
			{
				int temp = (int) (Math.random() * 12 - 2);
				String sTemp = "";
				if (temp == -1)
					sTemp = "F";
				else
					sTemp = "" + temp;
				grid[x][y] = new Tile(sTemp, x, y);
			}
		}
		grid[0][0].setWeight("0");
		grid[0][1].setWeight("0");
		grid[1][0].setWeight("F");
		grid[1][1].setWeight("0");
		startTile = grid[startX][startY];
		endTile = grid[endX][endY];
	}
	
	/**
	 * Reads in a .grid file and produces a grid object
	 * 
	 * @param file
	 *            - .grid file to be read
	 */
	private void produceGrid(FileHandle file)
	{
		String s = file.readString();
		int Xcount = 0;
		int Ycount = 0;
		for (int x = 0; x < s.length(); x++)
		{
			if (s.charAt(x) == '\n')
			{
				xLen = Xcount;
				Xcount = 0;
				Ycount++;
			}
			else if (!(s.charAt(x) == '\r') && !(s.charAt(x) == '\n') && !(s.charAt(x) == ' '))
			{
				Xcount++;
			}
			if (s.charAt(x) == 'T')
				Xcount--;
			
		}
		Ycount++;
		yLen = Ycount;
		grid = new Tile[xLen][yLen];
		String str = file.readString();
		Scanner scan = new Scanner(str);
		for (int y = yLen - 1; y >= 0; y--)
		{
			for (int x = 0; x < xLen; x++)
			{
				String sTemp = scan.next();
				grid[x][y] = new Tile(sTemp, x, y);
				if (sTemp.startsWith("T") || sTemp.startsWith("t"))
					grid[x][y].isTele = true;
			}
		}
		scan.close();
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		open = new ArrayList<Tile>();
		closed = new ArrayList<Tile>();
		startTile = grid[startX][startY];
		endTile = grid[endX][endY];
		findTele();
	}
	
	/**
	 * Finds and links all teleporters
	 */
	private void findTele()
	{
		for (int x = 0; x < xLen; x++)
		{
			for (int y = 0; y < yLen; y++)
			{
				if (grid[x][y].isTele)
				{
					Tile start = grid[x][y];
					for (int x2 = 0; x2 < xLen; x2++)
					{
						for (int y2 = 0; y2 < yLen; y2++)
						{
							if (grid[x2][y2].getWeight().equals(start.getWeight()) && !grid[x2][y2].equals(start))
							{
								start.dest = grid[x2][y2];
								grid[x2][y2].dest = start;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Draws grid, path, bottom bar, and weight numbers
	 * 
	 * @param sr
	 *            - ShapeRenderer
	 * @param batch
	 *            - SpriteBatch
	 * @param font
	 *            - BitmapFont
	 */
	public void draw(ShapeRenderer sr, SpriteBatch batch, BitmapFont font)
	{
		// ShapeRenderer
		sr.begin(ShapeType.Filled);
		// Grid blocks
		for (int x = (-Xmargin) / (tileSize + 1); x < (-Xmargin + 615) / (tileSize + 1); x++)
		{
			for (int y = (-Ymargin) / (tileSize + 1); y < (-Ymargin + 615) / (tileSize + 1); y++)
			{
				if (x >= 0 && x < xLen && y >= 0 && y < yLen)
				{
					int xPos = x + (tileSize * x) + Xmargin;
					int yPos = y + (tileSize * y) + Ymargin;
					if (xPos >= -20 && xPos <= 600 && yPos >= -20 && yPos <= 600)
					{
						if (x == startX && y == startY)
							sr.setColor(Color.GREEN);
						else if (x == endX && y == endY)
							sr.setColor(Color.BLUE);
						else if (grid[x][y].getWeight().equals("F"))
							sr.setColor(Color.RED);
						else if (closed.contains(grid[x][y]))
							sr.setColor(Color.GRAY);
						else
							sr.setColor(Color.TAN);
						sr.rect(xPos, yPos, tileSize, tileSize);
					}
				}
			}
		}
		// Path
		Tile current = endTile;
		while (current.parent != null)
		{
			int xPos1 = (current.x * (tileSize + 1) + Xmargin) + 9;
			int yPos1 = (current.y * (tileSize + 1) + Ymargin) + 8;
			int xPos2 = (current.parent.x * (tileSize + 1) + Xmargin) + 9;
			int yPos2 = (current.parent.y * (tileSize + 1) + Ymargin) + 8;
			if ((xPos1 >= -20 && xPos1 <= 600 && yPos1 >= -20 && yPos1 <= 600)
					|| (xPos2 >= -20 && xPos2 <= 600 && yPos2 >= -20 && yPos2 <= 600))
			{
				sr.setColor(Color.ORANGE);
				sr.rectLine(xPos1, yPos1, xPos2, yPos2, 3);
				sr.setColor(Color.YELLOW);
				int xPos = (current.x * (tileSize + 1) + Xmargin) + 5;
				int yPos = (current.y * (tileSize + 1) + Ymargin) + 5;
				if (xPos >= -20 && xPos <= 600 && yPos >= -20 && yPos <= 600)
				{
					sr.rect(xPos, yPos, 7, 7);
				}
				xPos = (current.parent.x * (tileSize + 1) + Xmargin) + 5;
				yPos = (current.parent.y * (tileSize + 1) + Ymargin) + 5;
				if (xPos >= -20 && xPos <= 600 && yPos >= -20 && yPos <= 600)
				{
					sr.rect(xPos, yPos, 7, 7);
				}
			}
			if (current.parent.equals(current))
				break;
			else
				current = current.parent;
		}
		// Bottom Bar
		sr.setColor(Color.VIOLET);
		sr.rect(0, 0, 600, 25);
		if (changeStart)
			sr.setColor(Color.BLACK);
		else
			sr.setColor(Color.GREEN);
		sr.rect(0, 0, 100, 25);
		if (changeEnd)
			sr.setColor(Color.BLACK);
		else
			sr.setColor(Color.CYAN);
		sr.rect(500, 0, 100, 25);
		sr.end();
		
		// Batch
		batch.begin();
		// Weight numbers
		for (int x = (-Xmargin) / (tileSize + 1); x < (-Xmargin + 615) / (tileSize + 1); x++)
		{
			for (int y = (-Ymargin) / (tileSize + 1); y < (-Ymargin + 615) / (tileSize + 1); y++)
			{
				if (x >= 0 && x < xLen && y >= 0 && y < yLen)
				{
					int xOffset = 3;
					if (grid[x][y].isTele)
						xOffset = 0;
					int xPos = x + (tileSize * x) + xOffset + Xmargin;
					int yPos = y + (tileSize * y) - 2 + tileSize + Ymargin;
					if (xPos >= -20 && xPos <= 600 && yPos >= 25 && yPos <= 600)
					{
						font.setColor(Color.BLACK);
						font.draw(batch, grid[x][y].getWeight(), xPos, yPos);
					}
				}
			}
		}
		// Bottom Bar
		font.setColor(Color.BLACK);
		font.draw(batch, "<-- Click Here to choose start tile. Or here for the end tile. -->", 107, 20);
		if (changeStart)
			font.setColor(Color.WHITE);
		else
			font.setColor(Color.BLACK);
		font.draw(batch, "START", 25, 17);
		if (changeEnd)
			font.setColor(Color.WHITE);
		else
			font.setColor(Color.BLACK);
		font.draw(batch, "END", 535, 17);
		batch.end();
	}
	
	/**
	 * Uses Dijkstra's path finding algorithm to find the shortest path between
	 * the start tile and the end tile.
	 */
	public void findPath()
	{
		if (endTile.equals(startTile))
		{
			endTile.parent = startTile;
			return;
		}
		Tile current;
		startTile = grid[startX][startY];
		startTile.cost = 0;
		open = new ArrayList<Tile>();
		closed = new ArrayList<Tile>();
		for (int x = 0; x < xLen; x++)
		{
			for (int y = 0; y < yLen; y++)
			{
				open.add(grid[x][y]);
			}
		}
		while (!open.isEmpty())
		{
			current = getLowestCost(open);
			open.remove(current);
			closed.add(current);
			if (current.isTele)
			{
				current.dest.parent = current;
				current = current.dest;
				current.cost = current.parent.cost;
				open.remove(current);
				closed.add(current);
			}
			if (current.equals(endTile) || (current.isTele && current.dest.equals(endTile)))
			{
				return;
			}
			if (current.x > 0)
			{
				Tile target = grid[current.x - 1][current.y];
				if (!target.getWeight().equals("F") && !closed.contains(target))
				{
					updateTile(target, current);
					if (target.parent == null)
						System.out.print("");
				}
			}
			if (current.x < xLen - 1)
			{
				Tile target = grid[current.x + 1][current.y];
				if (!target.getWeight().equals("F") && !closed.contains(target))
				{
					updateTile(target, current);
					if (target.parent == null)
						System.out.print("");
				}
			}
			if (current.y > 0)
			{
				Tile target = grid[current.x][current.y - 1];
				if (!target.getWeight().equals("F") && !closed.contains(target))
				{
					updateTile(target, current);
					if (target.parent == null)
						System.out.print("");
				}
			}
			if (current.y < yLen - 1)
			{
				Tile target = grid[current.x][current.y + 1];
				if (!target.getWeight().equals("F") && !closed.contains(target))
				{
					updateTile(target, current);
					if (target.parent == null)
						System.out.print("");
				}
			}
		}
		
	}
	
	/**
	 * Gets the item in open with the lowest cost
	 * 
	 * @param open
	 *            - ArrayList of open tiles
	 * @return - Returns the tile with the lowest cost
	 */
	private Tile getLowestCost(ArrayList<Tile> open)
	{
		if (open.isEmpty())
			return null;
		Tile lowest = open.get(0);
		for (Tile t : open)
		{
			if (t.cost < lowest.cost)
				lowest = t;
		}
		return lowest;
	}
	
	/**
	 * Changes cost and parent if necessary
	 * 
	 * @param target
	 *            - Tile being updated
	 * @param current
	 *            - Tile the path finding algorithm is currently on
	 */
	private void updateTile(Tile target, Tile current)
	{
		int temp;
		int weight;
		if (!target.isTele)
		{
			weight = Integer.parseInt(target.getWeight());
		}
		else
		{
			weight = 0;
		}
		temp = current.cost + weight;
		if (target.cost > temp)
		{
			target.cost = temp;
			target.parent = current;
		}
	}
	
	/**
	 * Resets path finding fields of each tile in the grid
	 */
	private void resetGrid()
	{
		for (int x = 0; x < xLen; x++)
		{
			for (int y = 0; y < yLen; y++)
			{
				grid[x][y].cost = 1000000000;
				grid[x][y].parent = null;
			}
		}
	}
	
	/* Getters and Setters */
	public int getStartX()
	{
		return startX;
	}
	
	public void setStartX(int startX)
	{
		this.startX = startX;
		this.startTile = grid[startX][startY];
		resetGrid();
	}
	
	public int getStartY()
	{
		return startY;
	}
	
	public void setStartY(int startY)
	{
		this.startY = startY;
		this.startTile = grid[startX][startY];
		resetGrid();
	}
	
	public int getEndX()
	{
		return endX;
	}
	
	public void setEndX(int endX)
	{
		this.endX = endX;
		this.endTile = grid[endX][endY];
		resetGrid();
	}
	
	public int getEndY()
	{
		return endY;
	}
	
	public void setEndY(int endY)
	{
		this.endY = endY;
		this.endTile = grid[endX][endY];
		resetGrid();
	}
	
}
