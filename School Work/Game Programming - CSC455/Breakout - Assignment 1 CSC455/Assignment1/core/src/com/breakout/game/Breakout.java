package com.breakout.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
/**
 * Breakout
 * Created for CSC 455 at SUNY Oswego
 * @author Mike Mekker
 */
public class Breakout extends ApplicationAdapter {
	SpriteBatch batch;
    private BitmapFont font;
	ShapeRenderer shapeRenderer;
	Texture background;
	Texture title;
	Map m;
	Map mapEditorMap;
	Map mapEditorMapBackup;
	Paddle paddle;
	Ball ball;
	String[] level;
	String cheat;
	boolean pause;
	boolean launch;
	boolean mapEditor;
	boolean usedCheats;
	boolean resetScore;
	int currentLvl;
	int highscore;
	int score;
	int lives;
	
	/**
	 * Initializes variables and populates map
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		background = new Texture("BlueBG.JPG");
		title = new Texture("BREAKOUT.PNG");
		cheat = "";
		currentLvl = 0;
		level = new String[5];
		
		//Enter Level names in order you want player to play them
		level[0] = "Level1";
		level[1] = "Level2";
		level[2] = "Level3";
		level[3] = "Level4";
		level[4] = "Level5";
		
		//Create Game Objects
		m = new Map(level[currentLvl]);
		mapEditorMap = new Map();
		mapEditorMap.name = "ME";
		mapEditorMapBackup = new Map();
		mapEditorMapBackup.name = "MEB";
		paddle = new Paddle();
		ball = new Ball();
		
		pause = true;
		launch = false;
		mapEditor = false;
		usedCheats = false;
		resetScore = false;
		highscore = getHighscore();
		score = 0;
		lives = 5;
	}
	
	/**
	 * Loops game
	 */
	@Override
	public void render () {
		//Set background color
		Gdx.gl.glClearColor(0,1,1,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Draw the Background
		batch.begin();
		batch.draw(background, -150, -150);
		batch.end();
		
		//Input
		checkInputs();
		
		if(mapEditor)
		{
			drawMapEditor();
			//Draw Objects
			drawObjects();
			if(launch)
			{
				//Change ball's position
				ball.x += ball.xSpeed;
				ball.y += ball.ySpeed;
			}
			else
			{
				ball.x += paddle.speed;
			}
			//Change paddle's position
			paddle.x += paddle.speed;
			
			//Collision
			collide(ball,paddle);
			brickCollision(ball,mapEditorMap);
			
			//Ball goes off screen
			checkBallBounds();
			
			//Reset Paddle speed and position if needed
			paddle.speed = 0;
			if(paddle.getX() < -100){paddle.setX(-100);}
			if(paddle.getX() > 660){paddle.setX(660);}
		}
		else
		{
			if(pause)
			{
				pauseMenu();
			}
			else
			{
				//Draw Objects
				drawObjects();
				//Bottom Bar
				bottomBar();
				
				//Check for game over
				if(lives <= 0)
				{
					gameOver();
				}
				//Check for completed level
				if(allBricksGone(m))
				{
					nextLevel();
				}
				
				if(launch)
				{
					//Change ball's position
					ball.x += ball.xSpeed;
					ball.y += ball.ySpeed;
				}
				else
				{
					ball.x += paddle.speed;
				}
				//Change paddle's position
				paddle.x += paddle.speed;
				
				//Collision
				collide(ball,paddle);
				brickCollision(ball,m);
				
				//Ball goes off screen
				checkBallBounds();
				
				//Reset Paddle speed and position if needed
				paddle.speed = 0;
				if(paddle.getX() < -100){paddle.setX(-100);}
				if(paddle.getX() > 660){paddle.setX(660);}
			}
		}
	}
	
	/*****************************************************/
	/*********************Checks**************************/
	/*****************************************************/
	/**
	 * Checks if any input keys were pressed
	 * Enter: pause game
	 * Space: launch ball after start or resume of game
	 * Left: moves paddle left
	 * Right: moves paddle right
	 * I,N,C,L,V,E,S: cheats
	 * Alt + Enter: Go to Map Editor Mode
	 */
	public void checkInputs()
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
		{
			if(resetScore){score = 0;resetScore = false;}
			if(Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT) && pause)
			{
				gameOver();
				mapEditor = !mapEditor;
			}
			else if(!mapEditor)
			{
				pause = !pause;
				launch = false;
				if(!pause)
				{
					if(cheat.equals("inc"))
					{
						 usedCheats = true;
						paddle.width += 50;
					}
					else if(cheat.equals("lives"))
					{
						 usedCheats = true;
						lives += 5;
					}
					cheat = "";
				}
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			if(mapEditor)
			{
				launch = !launch;
				if(!launch)
				{
					mapCopy(mapEditorMap, mapEditorMapBackup);
					ball.reset();
					paddle.reset();
				}
			}
			else
			{
				launch = true;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){paddle.speed = -15;}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){paddle.speed = 15;}
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){paddle.speed = 0;}
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){paddle.speed = 0;}
		if(Gdx.input.isKeyJustPressed(Input.Keys.I)){cheat += "i";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.N)){cheat += "n";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.C)){cheat += "c";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.L)){cheat += "L";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.V)){cheat += "v";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.E)){cheat += "e";}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)){cheat += "s";}
		if(Gdx.input.justTouched())
		{
			if(mapEditor && !launch)
			{
				mouseOnBrick(Gdx.input.getX(), (660-Gdx.input.getY()));
			}
		}
	}
	/**
	 * Loops through all bricks in the map to check if they are all destroyed
	 * @param m: Map being checked
	 * @return: returns whether all bricks were destroyed
	 */
	private boolean allBricksGone(Map m)
	{
		for(int x = 0; x < m.rows; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				if(m.map[x][y].getHits() > 0)
				{
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Checks if the ball goes off the screen
	 * Bottom: Loses life
	 * Top, Left, Right: Bounce off
	 */
	public void checkBallBounds()
	{
		//Bottom
		if(ball.y+Ball.radius <= 0)
		{
			lives--;
			launch = false;
			ball.reset();
			paddle.reset();
		}
		//Top
		if(ball.y+Ball.radius >= 660)
		{
			ball.y = 650;
			ball.ySpeed*=-1;
		}
		//Right
		if(ball.x+Ball.radius >= 700)
		{
			ball.x = 690;
			ball.xSpeed*=-1;
		}
		//Left
		if(ball.x-Ball.radius <= 0)
		{
			ball.x = 10;
			ball.xSpeed*=-1;
		}
	}
	
	/*****************************************************/
	/*********************Events**************************/
	/*****************************************************/
	/**
	 * Increments level variable
	 * creates new map
	 * adds to score (200 per remaining life)
	 * resets number of lives
	 * resets ball and paddle
	 */
	public void nextLevel()
	{
		currentLvl++;
		if(currentLvl > 4){currentLvl--;}
		m = new Map(level[currentLvl]);
		score += lives * 200;
		lives = 5;
		launch = false;
		ball.reset();
		paddle.reset();
	}
	/**
	 * Resets game in the case of lose of all lives
	 * Pauses game
	 * Resets lives, score, level, map, paddle, and ball
	 */
	public void gameOver()
	{
		pause = true;
		lives = 5;
		resetScore = true;
		currentLvl = 0;
		m = new Map(level[currentLvl]);
		paddle.reset();
		ball.reset();
		paddle.width = 150;
	}
	
	/*****************************************************/
	/*********************Collision***********************/
	/*****************************************************/
	/**
	 * Adds one hit to the brick that the mouse is on
	 * Adds hit to both mapEditorMap and mapEditorMapBackup
	 * Backup is used to re-make original when player wants to continue editing
	 * @param x: X coordinate
	 * @param y: Y coordinate
	 */
	public void mouseOnBrick(int x, int y)
	{
		for(int i = 0; i < mapEditorMap.rows; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				if(x > mapEditorMap.map[i][j].getX() && x < mapEditorMap.map[i][j].getX()+Brick.width)
				{
					if(y > mapEditorMap.map[i][j].getY() && y < mapEditorMap.map[i][j].getY()+Brick.height)
					{
						mapEditorMap.map[i][j].incHits();
						mapEditorMapBackup.map[i][j].incHits();
					}
				}
			}
		}
	}
	/**
	 * Checks for collision between ball and paddle
	 * @param b: Ball
	 * @param p: Paddle
	 */
	private void collide(Ball b, Paddle p)
	{
		if(b.y-Ball.radius <= p.y + p.height && //if the ball hit the paddle
				b.y-Ball.radius >= p.y &&
				b.x+Ball.radius >= p.x &&
				b.x-Ball.radius <= p.x+p.width)
		{
			b.ySpeed *= -1;	//reverse Y coordinate
			
			//changes X coordinate based on how far from the center the ball hit
			if(((b.x-(p.x + 75))/7) > 5 || ((b.x-(p.x + 75))/7) < -5)
			{
				b.xSpeed = ((b.x-(p.x + (p.width/2)))/7);
			}
			
		}
	}
	/**
	 * Checks for collision between the Ball and all bricks on the Map
	 * @param b: Ball
	 * @param m: Map
	 * @return: Returns if the ball is colliding with a brick
	 */
	public boolean brickCollision(Ball b, Map m)
	{
		for(int x = 0; x < m.rows; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				if(m.map[x][y].getHits()>0)
				{
					if(collide(b,m.map[x][y]))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * Checks if the ball is colliding with a specific brick
	 * @param b: Ball
	 * @param br: Brick
	 * @return: returns if the ball is colliding with the brick
	 */
	public boolean collide(Ball b, Brick br)
	{
		boolean hit = false;
		int offset = 10;
		//Bottom
		if((b.y+Ball.radius) >= br.getY() &&
				(b.y+Ball.radius) <= (br.getY()+Brick.height)&&
				b.x >= br.getX() &&
				b.x <= br.getX()+Brick.width)
		{
			b.y-=offset;
			b.ySpeed*=-1;
			hit = true;
		}
		//Top
		else if((b.y-Ball.radius) >= br.getY() &&//bottom
				(b.y-Ball.radius) <= (br.getY()+Brick.height)&&//top
				b.x >= br.getX() &&
				b.x <= br.getX()+Brick.width)
		{
			b.y+=offset;
			b.ySpeed*=-1;
			hit = true;
		}
		//Right
		else if((b.x-Ball.radius) >= (br.getX()+Brick.width-6) &&
				(b.x-Ball.radius) <= (br.getX()+Brick.width)&&
				b.y >= br.getY()-6 &&
				b.y <= br.getY()+6+Brick.height)
		{
			b.x+=offset;
			b.xSpeed*=-1;
			hit = true;
		}
		//Left
		else if((b.x+Ball.radius) >= br.getX() &&
				(b.x+Ball.radius) <= (br.getX()+Brick.width+6)&&
				b.y >= br.getY()-6 &&
				b.y <= br.getY()+6+Brick.height)
		{
			b.x-=offset;
			b.xSpeed*=-1;
			hit = true;
		}
		
		if(hit)
		{
			br.gotHit();
			score += 100;
			if(score > highscore)
			{
				rewriteHighscore();
			}
			return true;
		}
		return false;
	}
	
	
	/*****************************************************/
	/*********************Drawing*************************/
	/*****************************************************/
	public void drawMapEditor()
	{
		mapEditorMap.draw(shapeRenderer);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0,1,1,0);
		shapeRenderer.rect(0, 0, 700, 25);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < mapEditorMap.rows; y++)
			{
				shapeRenderer.rect((x*70), 660-(((y+2)*30)), Brick.width, Brick.height);
			}
		}
		shapeRenderer.rect(0, 0, 700, 25);
		shapeRenderer.end();
		batch.begin();
		font.setColor(Color.BLACK);
		if(!launch)
		{
			font.draw(batch, "Press Alt+Enter to return to the main menu. Click grid to add bricks. Press space to play.", 70, 17);
		}
		else
		{
			font.draw(batch, "Press Alt+Enter to return to the main menu. Click grid to add bricks. Press space to edit.", 70, 17);
		}
		batch.end();
	}
	/**
	 * Draws the Paddle, Ball, and Map
	 */
	public void drawObjects()
	{
		paddle.draw(shapeRenderer);
		if(mapEditor)
			mapEditorMap.draw(shapeRenderer);
		else
			m.draw(shapeRenderer);
		ball.draw(shapeRenderer);
	}
	/**
	 * Draws the pause menu
	 */
	public void pauseMenu()
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0,1,1,0); //Sets color to cyan
		shapeRenderer.rect(65, 210, 580, 440); // draws main rectangle
		shapeRenderer.end();
		batch.begin();
		batch.draw(title, 90, 350); //inserts title from file
		font.setColor(Color.BLACK);
		font.draw(batch, "To play press the 'ENTER' key.", 100, 300); //Title text
		font.draw(batch, "High Score: " + highscore, 100, 275);
		font.draw(batch, "Hold 'ALT' and press 'ENTER' to enter Map Editor Mode.", 100, 250);
		batch.end();
		bottomBar();
	}
	/**
	 * Draws the bottom bar including the lives and score counters
	 */
	public void bottomBar()
	{
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0,1,1,0); //Set color to cyan
		shapeRenderer.rect(0, 0, 700, 25); //Draw bottom rectangle
		int tempL = lives; //tempL is the number of lives that can be drawn
		if(tempL > 10)tempL = 10; //max of 10 lives can be drawn or else they could go off screen
		for(int x = 0; x < tempL; x++) //loops through a draws tempL number of circles
		{
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.circle(70 + (x*15), 11, Ball.radius);
			shapeRenderer.setColor(Ball.color);
			shapeRenderer.circle(70 + (x*15), 11, Ball.radius-1);
		}
		shapeRenderer.end();
		batch.begin();
		font.setColor(Color.BLACK);
		font.draw(batch, "Lives:", 15, 17); //write text in bottom bar
		font.draw(batch, "Score: " + score, 550, 17);
		if(!launch && !pause){font.draw(batch, "Press Space to play.", 260, 17);} //Tells player how to start if they aren't already playing
		batch.end();
	}
	
	/*****************************************************/
	/*********************Highscore***********************/
	/*****************************************************/
	/**
	 * Writes the highscore to the "My Preferences" file
	 */
	public void rewriteHighscore()
	{
		if(!usedCheats)
		{
			highscore = score;
			Preferences prefs = Gdx.app.getPreferences("My Preferences");
			prefs.putInteger("Highscore", highscore);
			prefs.flush();
		}
	}
	/**
	 * Reads the highscore from the "My Preferences" file
	 */
	public int getHighscore()
	{
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		return prefs.getInteger("Highscore");
	}
	/*****************************************************/
	/***********************Misc**************************/
	/*****************************************************/
	/**
	 * Copies Map m2 to Map m1 by copying each brick
	 * @param m1: Map 1 - Map to store copy
	 * @param m2: Map 2 - Map being copied
	 */
	public void mapCopy(Map m1, Map m2)
	{
		if(m1.rows != m2.rows)
			return;
		for(int x = 0; x < m1.rows; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				m1.map[x][y] = (Brick) m2.map[x][y].clone();
			}
		}
	}
}
