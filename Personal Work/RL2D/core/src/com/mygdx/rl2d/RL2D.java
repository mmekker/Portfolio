package com.mygdx.rl2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.screens.MainMenuScreen;

public class RL2D extends Game {

	@Override
	public void create() {
		MainMenuScreen main = new MainMenuScreen(this);
		this.setScreen(main);
		// this.setScreen(new GameScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	public void changeScreen(Screen screen) {
		this.setScreen(screen);
	}
}
