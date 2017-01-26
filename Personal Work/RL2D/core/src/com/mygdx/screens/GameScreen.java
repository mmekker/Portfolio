package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gameobjects.Ball;
import com.mygdx.gameobjects.Car;
import com.mygdx.gameobjects.EndWall;
import com.mygdx.gameobjects.EnemyCar;
import com.mygdx.gameobjects.SideWall;
import com.mygdx.rl2d.RL2D;

/**
 * GameScreen extends Screen and can be created and set to current screen for the RL2D Game.
 * @author mmekker
 */
public class GameScreen implements Screen, InputProcessor {

	// Rendering stuff
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer sr;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;

	// World and Objects
	private World world;
	private Car car;
	private EnemyCar car2;
	private Ball ball;
	private SideWall leftWall;
	private SideWall rightWall;
	private EndWall topWall;
	private EndWall bottomWall;

	//Game Fields
	private int gameTime;
	private int playerScore;
	private int enemyScore;
	private int kickoffTimer;
	private final int GAME_TIME = 6000;
	private final int KICKOFF_TIME = 60;
	private boolean kickoff;

	RL2D game;

	/**
	 * Initialize Game objects and fields.
	 * @param game - Game object to set Screen
     */
	public GameScreen(RL2D game) {
		Gdx.input.setInputProcessor(this);
		batch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		ball = new Ball(world, 0, 0);
		car = new Car(world, 0, (int) -(Gdx.graphics.getHeight()*0.3125f));
		car2 = new EnemyCar(world, 0, (int) (Gdx.graphics.getHeight()*0.3125f));
		car2.setBall(ball);


		leftWall = new SideWall(world, -(int) camera.viewportWidth / 2, 0);
		rightWall = new SideWall(world, (int) camera.viewportWidth / 2, 0);
		topWall = new EndWall(world, 0, -(int) camera.viewportHeight / 2);
		bottomWall = new EndWall(world, 0, (int) camera.viewportHeight / 2);

		gameTime = GAME_TIME;
		playerScore = 0;
		enemyScore = 0;
		kickoffTimer = KICKOFF_TIME;
		kickoff = true;

		this.game = game;
	}

	/**
	 * This render method is called every iteration of the game loop.
	 * @param delta
     */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (kickoff) {
			batch.begin();
			//Draw countdown
			font.setColor(Color.WHITE);
			CharSequence str = Integer.toString(kickoffTimer);
			font.draw(batch, str, Gdx.graphics.getWidth() / 2 - (Gdx.graphics.getHeight()*0.15625f), Gdx.graphics.getHeight() / 2 + (Gdx.graphics.getHeight()*0.03125f));

			batch.end();

			//Decrement kickoffTimer and check if its hit zero.
			kickoffTimer -= Gdx.graphics.getDeltaTime();
			if (kickoffTimer <= 0) {
				kickoff = false;
			}
		} else {
			if (checkForGoal()) { //If there's been a goal then reset players and ball
				kickoff = true;
				kickoffTimer = KICKOFF_TIME;
				car.getBody().setTransform(0, -(Gdx.graphics.getHeight()*0.3125f), 0);
				car.getBody().setLinearVelocity(0, 0);
				car2.getBody().setTransform(0, (Gdx.graphics.getHeight()*0.3125f), 0);
				car2.getBody().setLinearVelocity(0, 0);
				ball.getBody().setTransform(0, 0, 0);
				ball.getBody().setLinearVelocity(0, 0);
			}
			//Update car steering behaviors
			car.update(delta);
			car2.update(delta);
			//Step the world
			world.step(1 / 120f, 6, 2);
			world.clearForces();
			//Decrement gameTime and check if the game is over.
			gameTime -= Gdx.graphics.getDeltaTime();
			if (gameTime <= 0) {
				//Creates new MainMenu and sets the message to whether the player won, lost, or tied
				MainMenuScreen m = new MainMenuScreen(game);
				if (playerScore > enemyScore)
					m.setMessage("You won!");
				else if (playerScore < enemyScore)
					m.setMessage("You lost.");
				else
					m.setMessage("You tied!");
				//Sets the screen to the new MainMenu
				game.changeScreen(m);
			}
		}

		// Rendering
		sr.begin(ShapeType.Filled);

		// Draw target circle
		sr.setColor(Color.CYAN);
		if (car.getTarget() != null) {
			sr.circle(car.getTarget().getPosition().x + (int) camera.viewportWidth / 2,
					car.getTarget().getPosition().y + (int) camera.viewportHeight / 2, 2);
		}
		// Draw walls
		sr.setColor(Color.WHITE);
		// Right Wall
		sr.rect(Gdx.graphics.getWidth() - SideWall.WALL_WIDTH / 2, 0, SideWall.WALL_WIDTH, SideWall.WALL_HEIGHT);
		// Left Wall
		sr.rect(-SideWall.WALL_WIDTH / 2, 0, SideWall.WALL_WIDTH, SideWall.WALL_HEIGHT);
		// Top Wall
		sr.rect(0, Gdx.graphics.getHeight() - EndWall.WALL_HEIGHT, EndWall.WALL_WIDTH, EndWall.WALL_HEIGHT);
		// Bottom Wall
		sr.rect(0, 0, EndWall.WALL_WIDTH, EndWall.WALL_HEIGHT);

		// Goals
		sr.setColor(Color.YELLOW);
		sr.rect(Gdx.graphics.getWidth() / 2 - (Gdx.graphics.getHeight()*0.125f), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()*0.084375f), (Gdx.graphics.getHeight()*0.25f), (Gdx.graphics.getHeight()*0.084375f));
		sr.rect(Gdx.graphics.getWidth() / 2 - (Gdx.graphics.getHeight()*0.125f), 0, (Gdx.graphics.getHeight()*0.25f), (Gdx.graphics.getHeight()*0.08125f));

		sr.end();
		batch.begin();

		car.render(batch);
		car2.render(batch);
		ball.render(batch);

		font.setColor(Color.BLACK);
		// Game Time
		CharSequence strTime = Integer.toString(gameTime);
		font.draw(batch, strTime, Gdx.graphics.getWidth() - (Gdx.graphics.getHeight()*0.140625f), (Gdx.graphics.getHeight()*0.0625f));
		// Player Score
		CharSequence strPSCore = Integer.toString(playerScore);
		font.draw(batch, strPSCore, (Gdx.graphics.getHeight()*0.09375f), (Gdx.graphics.getHeight()*0.0625f));
		// Enemy Score
		CharSequence strEScore = Integer.toString(enemyScore);
		font.draw(batch, strEScore, (Gdx.graphics.getHeight()*0.09375f), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()*0.03125f));

		batch.end();
		// debugRenderer.render(world, camera.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		font.dispose();
	}

	/**
	 * This method checks if the ball is touching the goal. Increments proper player's score
	 * @return - Returns true if a goal has been scored or false if not.
     */
	public boolean checkForGoal() {
		if (ball.getBody().getPosition().x > -(Gdx.graphics.getHeight()*0.13125f) && ball.getBody().getPosition().x < (Gdx.graphics.getHeight()*0.13125f)
				&& ball.getBody().getPosition().y < Gdx.graphics.getHeight() / 2 - (Gdx.graphics.getHeight()*0.09375f)
				&& ball.getBody().getPosition().y > Gdx.graphics.getHeight() / 2 - (Gdx.graphics.getHeight()*0.115625f)) {
			playerScore++;
			return true;
		}
		if (ball.getBody().getPosition().x > -(Gdx.graphics.getHeight()*0.13125f) && ball.getBody().getPosition().x < (Gdx.graphics.getHeight()*0.13125f)
				&& ball.getBody().getPosition().y > -Gdx.graphics.getHeight() / 2 + (Gdx.graphics.getHeight()*0.09375f)
				&& ball.getBody().getPosition().y < -Gdx.graphics.getHeight() / 2 + (Gdx.graphics.getHeight()*0.115625f)) {
			enemyScore++;
			return true;
		}
		return false;
	}

	/** INPUT **/
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		car.clicked(screenX, screenY, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		car.clicked(screenX, screenY, pointer, 0);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	public void setSprite(Texture texture) {
		car.setSprite(texture);
	}

}
