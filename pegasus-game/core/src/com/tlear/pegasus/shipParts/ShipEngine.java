package com.tlear.pegasus.shipParts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tlear.pegasus.Ship;

public abstract class ShipEngine extends ShipPart implements Part {
	// Extra variables
	
	/* Texture */
	protected Texture imgFwd;
	protected Texture imgBwd;

	protected TextureRegion texFwd;
	protected TextureRegion texBwd;
	protected TextureRegion texNeutral;

	/* Model */
	
	protected float maxSpeed;
	protected float thrust;
	
	protected Vector2 velocity;
	
	protected int thrustDirection;	// -1, 0, 1 for BACK, NONE, FWD
	
	public ShipEngine(Vector2 offset, Ship parent) {
		super(offset, parent);
		// Assuming no texture
		
		maxSpeed = 0;
		velocity = new Vector2(0, 0);
		thrust = 0;
		thrustDirection = 0;
		
		imgFwd = imgBwd = null;
		texFwd = texBwd = null;
	}
	
	public ShipEngine(Vector2 offset, Ship parent, float w, float h) {
		super(offset, parent, w, h);
		
		maxSpeed = 0;
		velocity = new Vector2(0, 0);
		thrust = 0;
		thrustDirection = 0;
		
		imgFwd = imgBwd = null;
		texFwd = texBwd = null;
	}
	
	public ShipEngine(Vector2 offset, Ship parent, float w, float h, String texFileName, String texFileNameFwd, String texFileNameBwd) {
		super(offset, parent, w, h, texFileName);
		
		maxSpeed = 0;
		velocity = new Vector2(0, 0);
		thrust = 0;
		thrustDirection = 0;
		
		imgFwd = new Texture(Gdx.files.internal("shipParts/" + texFileNameFwd));
		imgBwd = new Texture(Gdx.files.internal("shipParts/" + texFileNameBwd));
		
		texFwd = new TextureRegion(imgFwd);
		texBwd = new TextureRegion(imgBwd);
		texNeutral = new TextureRegion(tex);
	}
	
	/* Textures */
	@Override
	public TextureRegion getTextureRegion() {
		switch (thrustDirection) {
		case -1:
			return texBwd;
		case 0:
			return texNeutral;
		case 1:
			return texFwd;
		default:
			throw new Error("Invalid Texture in basicEngine");
		}
	}
	
	/* DRAW AND UPDATE */
	@Override
	public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		// We draw as normal, but we need to update Tex first
		tex = getTextureRegion();
		//System.out.println(thrustDirection);
		super.draw(batch, shapeRenderer);
	}
	
	@Override
	public void update() {
		thrust(thrustDirection);
		resetThrustDirection();
	}
	
	/* Model */
	
	/* Engine only */
	// Fire engine in a direction
	public void thrust(int sign) {
		
		Vector2 deVel = new Vector2(velocity);
		deVel.nor();
		deVel.scl(-thrust/2);
		velocity.add(deVel);
		Vector2 tmp = new Vector2(velocity);
/*		
		if (Math.abs(velocity.len()) <= 2.0) {
			velocity = new Vector2(0, 0);
		}
	*/	
		if (sign != 0) {
			float dx = (float) Math.cos(degToRad * (angle + 90));
			float dy = (float) Math.sin(degToRad * (angle + 90));
			Vector2 tmp2 = new Vector2(dx, dy);
			tmp2.nor();
			tmp2.scl(thrust * sign);
			tmp.add(tmp2);
			
			if (tmp.len() <= maxSpeed) {
				velocity.add(tmp2);
			}
		}

		
	}
	public void increaseThrust() {
		//thrust(1);
		thrustDirection = 1;
	}
	public void decreaseThrust() {
		//thrust(-1);
		thrustDirection = -1;
	}
	private void resetThrustDirection() {
		thrustDirection = 0;
	}
	public void zero() {
		velocity = new Vector2(0, 0);
		thrustDirection = 0;
	}
	
	// Getters
	
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public Vector2 getVelocity() {
		return new Vector2(velocity);
	}
}
