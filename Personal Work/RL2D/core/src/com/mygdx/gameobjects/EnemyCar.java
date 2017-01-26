package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Small extension of the Car class. Update method changed to add AI
 * functionality.
 * 
 * @author mmekker
 *
 */
public class EnemyCar extends Car {
	private Ball ball;
	private float counter = 0;
	private float last = 0;

	public EnemyCar(World world, int x, int y) {
		super(world, x, y);
		super.setMaxLinearSpeed(Gdx.graphics.getHeight()*0.9375f);
		super.setMaxLinearAcceleration(Gdx.graphics.getHeight()*0.78125f);
	}

	/**
	 * Similar update method to Car but has AI functionality
	 */
	@Override
	public void update(float delta) {
		if (ball != null) {
			Vector2 rightBottomCorner = new Vector2(Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
			Vector2 leftBottomCorner = new Vector2(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2);
			Vector2 rightTopCorner = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
			Vector2 leftTopCorner = new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
			if(ball.getBody().getPosition().dst(rightBottomCorner) < (Gdx.graphics.getHeight()*0.15625f) ||
					ball.getBody().getPosition().dst(leftBottomCorner) < (Gdx.graphics.getHeight()*0.15625f) ||
					ball.getBody().getPosition().dst(rightTopCorner) < (Gdx.graphics.getHeight()*0.15625f) ||
					ball.getBody().getPosition().dst(leftTopCorner) < (Gdx.graphics.getHeight()*0.15625f)) {
				this.setTarget(new Vector2(0,0));
			}
			else {
				if(counter > last + 0.5f) {
					Vector2 goalVec = new Vector2(0, -Gdx.graphics.getHeight()/2);
					Vector2 offset = new Vector2(ball.getBody().getPosition().sub(goalVec).nor().scl(ball.getRadius()));
					this.setTarget(ball.getBody().getPosition().add(offset));
					last = counter;
				}
			}
		}
		counter += delta;
		super.update(delta);
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
}
