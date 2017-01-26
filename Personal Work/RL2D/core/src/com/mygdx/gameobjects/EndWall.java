package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * EndWalls are similar to SideWalls. Their purpose is to keep cars and balls
 * inside to field.
 * 
 * @author mmekker
 *
 */
public class EndWall {
	private String side;
	private Sprite sprite;
	private Body body;

	public static final float WALL_WIDTH = Gdx.graphics.getWidth();
	public static final float WALL_HEIGHT = Gdx.graphics.getHeight()*0.08333f;

	public EndWall(World world, int x, int y) {
		BodyDef wallDef = new BodyDef();
		wallDef.type = BodyDef.BodyType.StaticBody;
		wallDef.position.set(x, y);
		body = world.createBody(wallDef);
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(WALL_WIDTH / 2, WALL_HEIGHT);
		FixtureDef wallFixtureDef = new FixtureDef();
		wallFixtureDef.shape = wallShape;
		wallFixtureDef.density = 1.0f;
		wallFixtureDef.filter.categoryBits = Category.CATEGORY_WALL.value;
		wallFixtureDef.filter.maskBits = -1;
		body.createFixture(wallFixtureDef);
		wallShape.dispose();
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
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

}
