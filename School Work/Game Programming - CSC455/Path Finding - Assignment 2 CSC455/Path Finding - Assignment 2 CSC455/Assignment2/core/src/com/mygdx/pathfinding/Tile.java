package com.mygdx.pathfinding;


public class Tile {
	private String weight;
	public int x;
	public int y;
	public Tile parent;
	public int cost;
	public boolean isTele;
	public Tile dest;
	
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
	public String getWeight() {return weight;}
	public void setWeight(String weight) {this.weight = weight;}
}
