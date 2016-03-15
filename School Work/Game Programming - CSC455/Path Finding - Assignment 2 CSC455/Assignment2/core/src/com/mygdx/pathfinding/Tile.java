package com.mygdx.pathfinding;

/**
 * Tile class for path finding for CSC455 This is the tile object class it can
 * either be impassable, weighted, or a teleporter.
 * 
 * @author Mike Mekker
 */
public class Tile
{
	private String weight;
	public int x;
	public int y;
	public Tile parent;
	public int cost;
	public boolean isTele;
	public Tile dest;
	
	/**
	 * Default constructor for Tile
	 */
	public Tile()
	{
		x = 0;
		y = 0;
		cost = 1000000000;
		parent = null;
		weight = "F";
		isTele = false;
		dest = null;
	}
	
	/**
	 * Parameterized constructor
	 * 
	 * @param w
	 *            - Weight
	 * @param nx
	 *            - X Coordinate
	 * @param ny
	 *            - Y Coordinate
	 */
	public Tile(String w, int nx, int ny)
	{
		cost = 1000000000;
		parent = null;
		x = nx;
		y = ny;
		weight = w;
		isTele = false;
		dest = null;
	}
	
	/* Getters and Setters */
	public String getWeight()
	{
		return weight;
	}
	
	public void setWeight(String weight)
	{
		this.weight = weight;
	}
}
