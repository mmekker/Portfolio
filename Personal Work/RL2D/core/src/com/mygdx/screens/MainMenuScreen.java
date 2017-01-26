package com.mygdx.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.rl2d.RL2D;
/**
 * MainMenuScreen extends Screen and can be created and set to current screen for the RL2D Game.
 * @author mmekker
 */
public class MainMenuScreen implements Screen, InputProcessor {
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private BitmapFont font;
	private ArrayList<Texture> carTextures;
	private Texture title = new Texture("title.png");
	private TextureRegion rightArrow = new TextureRegion(new Texture("rightArrow.png"));
	private Texture leftArrow = new Texture("leftArrow.png");
	private int carTextureIndex = 0;
	private RL2D game;
	private boolean mouseOnButton = false;
	private CharSequence message = "";

	public MainMenuScreen(RL2D game) {
		Gdx.input.setInputProcessor(this);
		sr = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();

		carTextures = new ArrayList<Texture>();
		carTextures.add(new Texture("Car.png"));
		carTextures.add(new Texture("Ambulance.png"));
		carTextures.add(new Texture("Audi.png"));
		carTextures.add(new Texture("Black_viper.png"));
		carTextures.add(new Texture("Mini_truck.png"));
		carTextures.add(new Texture("Mini_van.png"));
		carTextures.add(new Texture("Police.png"));
		carTextures.add(new Texture("taxi.png"));
		carTextures.add(new Texture("truck.png"));

		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sr.begin(ShapeType.Filled);
		// Draw Play button
		sr.setColor(Color.WHITE);
		sr.circle(Gdx.graphics.getWidth() / 2, 0, (Gdx.graphics.getHeight()*0.15625f));
		if (mouseOnButton) {
			sr.setColor(Color.CYAN);
		} else {
			sr.setColor(Color.BLACK);
		}
		sr.circle(Gdx.graphics.getWidth() / 2, 0, (Gdx.graphics.getHeight()*0.140625f));
		sr.end();

		batch.begin();
		// Draw current car
		batch.draw(carTextures.get(carTextureIndex), Gdx.graphics.getWidth() / 2 - (Gdx.graphics.getHeight()*0.125f),
				Gdx.graphics.getHeight() / 2 - (Gdx.graphics.getHeight()*0.15625f), (Gdx.graphics.getHeight()*0.25f), (Gdx.graphics.getHeight()*0.375f));
		// Draw arrows
		batch.draw(rightArrow, Gdx.graphics.getWidth() - (Gdx.graphics.getHeight()*0.125f), Gdx.graphics.getHeight() / 2, (Gdx.graphics.getHeight()*0.09375f), (Gdx.graphics.getHeight()*0.09375f));
		batch.draw(leftArrow, (Gdx.graphics.getHeight()*0.03125f), Gdx.graphics.getHeight() / 2, (Gdx.graphics.getHeight()*0.09375f), (Gdx.graphics.getHeight()*0.09375f));
		// Draw title
		batch.draw(title, Gdx.graphics.getWidth() / 2 - (Gdx.graphics.getHeight()*0.165625f), (Gdx.graphics.getHeight()*0.78125f), (Gdx.graphics.getHeight()*0.3125f), (Gdx.graphics.getHeight()*0.15625f));
		font.setColor(Color.WHITE);
		font.draw(batch, message, Gdx.graphics.getWidth() / 2 - (message.length() * 3), (Gdx.graphics.getHeight()*0.3125f));
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		font.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 clickVector = new Vector2(screenX, -(screenY - Gdx.graphics.getHeight()));
		Vector2 buttonVector = new Vector2(Gdx.graphics.getWidth() / 2, 0);
		Vector2 leftArrowVector = new Vector2((Gdx.graphics.getHeight()*0.03125f), Gdx.graphics.getHeight() / 2);
		Vector2 rightArrowVector = new Vector2(Gdx.graphics.getWidth() - (Gdx.graphics.getHeight()*0.125f), Gdx.graphics.getHeight() / 2);
		if (clickVector.dst(buttonVector) < (Gdx.graphics.getHeight()*0.140625f)) {
			GameScreen g = new GameScreen(game);
			g.setSprite(carTextures.get(carTextureIndex));
			game.changeScreen(g);
		}
		if (clickVector.dst(leftArrowVector) < (Gdx.graphics.getHeight()*0.09375f)) {
			carTextureIndex--;
			if (carTextureIndex < 0)
				carTextureIndex = carTextures.size() - 1;
		}
		if (clickVector.dst(rightArrowVector) < (Gdx.graphics.getHeight()*0.09375f)) {
			carTextureIndex++;
			if (carTextureIndex > carTextures.size() - 1)
				carTextureIndex = 0;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector2 clickVector = new Vector2(screenX, -(screenY - Gdx.graphics.getHeight()));
		Vector2 buttonVector = new Vector2(Gdx.graphics.getWidth() / 2, 0);
		if (clickVector.dst(buttonVector) < (Gdx.graphics.getHeight()*0.140625f))
			mouseOnButton = true;
		else
			mouseOnButton = false;
		return true;
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

	public Game getGame() {
		return game;
	}

	public void setGame(RL2D game) {
		this.game = game;
	}

	public void setMessage(CharSequence str) {
		this.message = str;
	}

}
