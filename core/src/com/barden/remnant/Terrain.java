package com.barden.remnant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Terrain extends GameObject {

	Drawable drawable;
	
	public Terrain(GameWorld world){
		super(world);
		drawable = new Drawable();
		drawable.setDiffuse(new TextureRegion(new Texture(Gdx.files.internal("floor.png"))));
	}

	public void setPosition(float x, float y){
		drawable.setPosition(x, y);
	}
	
	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}
	
	@Override
	public String getType(){
		return "Terrain";
	}
	
}
