package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
/**
 * This is the Car class. Cars use the Steerable interface to arrive at a point.
 * This point is either moved by the player or the game to control where the car is going.
 * @author mmekker
 */
public class Car implements Steerable<Vector2> {
	private Sprite sprite;
	private Body body;
	private BodyDef carDef;
	private Target target;
	private boolean tagged;
	private float maxLinearSpeed, maxLinearAcceleration;
	private float maxAngularSpeed, maxAngularAcceleration;
	private float zeroLinearSpeedThreshold;
	public static final int CAR_WIDTH = Gdx.graphics.getWidth()/18;
	public static final int CAR_HEIGHT = Gdx.graphics.getHeight()/29;
	private float boundingRadius = CAR_HEIGHT;

	SteeringBehavior<Vector2> steeringBehavior;
	SteeringAcceleration<Vector2> steeringOutput;

	/**
	 * Constructor for Car. Creates the body of the car, sets steerable fields, and sets up the steering behavior
	 * @param world - world for car to be created in
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Car(World world, int x, int y) {
		//Create body definition
		carDef = new BodyDef();
		carDef.type = BodyType.DynamicBody;
		carDef.position.set(x, y);
		//Create body in world
		body = world.createBody(carDef);
		body.setFixedRotation(true);
		//Create shape of the car
		PolygonShape carShape = new PolygonShape();
		carShape.setAsBox(CAR_WIDTH / 2, CAR_HEIGHT);
		//Create the fixture definition
		FixtureDef carFixtureDef = new FixtureDef();
		carFixtureDef.shape = carShape;
		carFixtureDef.density = 0.5f;
		carFixtureDef.friction = 0.2f;
		carFixtureDef.restitution = 1f;
		carFixtureDef.filter.categoryBits = Category.CATEGORY_CAR.value;
		carFixtureDef.filter.maskBits = (short) (Category.CATEGORY_BALL.value | Category.CATEGORY_WALL.value);
		//Create fixture in the body
		body.createFixture(carFixtureDef);
		carShape.dispose();
		
		//Set Steerable fields
		tagged = false;
		maxLinearSpeed = Gdx.graphics.getHeight()*3.125f;
		maxLinearAcceleration = Gdx.graphics.getHeight()*1.5625f;
		maxAngularSpeed = Gdx.graphics.getHeight()*0.3125f;
		maxAngularAcceleration = Gdx.graphics.getHeight()*0.15625f;
		zeroLinearSpeedThreshold = 0;
		sprite = new Sprite(new Texture("Car.png"));
		sprite.setSize(CAR_WIDTH, CAR_HEIGHT+(Gdx.graphics.getHeight()*0.03125f));
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2 - 3);
		body.setUserData(sprite);
		
		//Initialize target and setup behavior
		target = new Target(world,0,0);
		Arrive<Vector2> arrive = new Arrive<Vector2>(this, target);
		arrive.setTimeToTarget(0.1f);
		arrive.setArrivalTolerance(2f);
		arrive.setDecelerationRadius(10f);
		this.setBehavior(arrive);
		steeringOutput = new SteeringAcceleration<Vector2>(new Vector2(0, 0));
	}

	/**
	 * This is the update method for Car. This method uses the steering behavior
	 * to calculate how much to steer the car and places that vector in steeringOutput.
	 * It then applies that steering to the car.
	 * @param delta - time difference
	 */
	public void update(float delta) {
		// Steering Behavior
		if (steeringBehavior != null) {
			steeringBehavior.calculateSteering(steeringOutput);
			applySteering(delta);
		}
	}

	/**
	 * Scales steeringOutput by delta and applies force to car
	 * @param delta - time difference
	 */
	private void applySteering(float delta) {
		if (!steeringOutput.linear.isZero()) {
			Vector2 force = steeringOutput.linear.scl(delta);
			body.applyForceToCenter(force.scl(Gdx.graphics.getHeight()*312.5f), true);
		}
		setOrientation(vectorToAngle(getLinearVelocity()));
	}
	
	public void render(SpriteBatch batch) {
		float angle = (float) (this.getOrientation() * (180 / Math.PI));
		sprite.rotate(angle - sprite.getRotation());
		sprite.setPosition(body.getPosition().x + Gdx.graphics.getWidth() / 2 - 5,
				body.getPosition().y + Gdx.graphics.getHeight() / 2 - 6f);
		sprite.draw(batch);
	}
	
	
	/**Input**/
	/**
	 * Events triggered when the field is clicked.
	 * Sets the target of the car to the screen coordinates that were clicked.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 */
	public void clicked(int screenX, int screenY, int pointer, int button) {
		setTarget(new Vector2(screenX-Gdx.graphics.getWidth()/2, -(screenY-Gdx.graphics.getHeight()/2)));
	}
	
	
	/**Math utils**/
	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}
	
	/**Getters and Setters**/
	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		body.setTransform(body.getPosition().x, body.getPosition().y, orientation);
	}

	@Override
	public Location<Vector2> newLocation() {
		return null;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroLinearSpeedThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		this.zeroLinearSpeedThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	public SteeringBehavior<Vector2> getBehavior() {
		return steeringBehavior;
	}

	public void setBehavior(SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Texture texture) {
		this.sprite.setTexture(texture);
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Vector2 vec) {
		this.target.getBody().setTransform(vec,0);
	}
}
