package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Balls are spawned into the world to collide with cars and be scored in goals.
 * 
 * @author mmekker
 *
 */
public class Ball {
	private Sprite sprite;
	private Body body;

	private final float BALL_RADIUS = Gdx.graphics.getWidth()/30;

	public Ball(World world, int x, int y) {
		BodyDef ballDef = new BodyDef();
		ballDef.type = BodyDef.BodyType.DynamicBody;
		ballDef.position.set(x, y);
		body = world.createBody(ballDef);
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(BALL_RADIUS);
		FixtureDef ballFixtureDef = new FixtureDef();
		ballFixtureDef.shape = ballShape;
		ballFixtureDef.density = 0.01f;
		ballFixtureDef.friction = 0.1f;
		ballFixtureDef.restitution = 1f;
		ballFixtureDef.filter.categoryBits = Category.CATEGORY_BALL.value;
		ballFixtureDef.filter.maskBits = (short) (Category.CATEGORY_CAR.value | Category.CATEGORY_WALL.value);
		body.createFixture(ballFixtureDef);
		ballShape.dispose();

		sprite = new Sprite(new Texture("ball.png"));
		sprite.setSize(BALL_RADIUS * 2, BALL_RADIUS * 2);
		body.setUserData(sprite);
	}

	public void render(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x + Gdx.graphics.getWidth() / 2 - BALL_RADIUS,
				body.getPosition().y + Gdx.graphics.getHeight() / 2 - BALL_RADIUS);
		sprite.draw(batch);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public float getRadius(){
		return this.BALL_RADIUS;
	}

}
