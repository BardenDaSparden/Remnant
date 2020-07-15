package com.barden.remnant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Light implements IDrawable  {
	
	Drawable drawable;
	
	public Light() {
		drawable = new Drawable();
		drawable.setDiffuse(new TextureRegion(new Texture(Gdx.files.internal("light256.png"))));
	}
	
	public Light setPosition(float x, float y){
		drawable.setPosition(x, y);
		return this;
	}
	
	public Light setColor(float r, float g, float b, float a){
		drawable.material.color.set(r, g, b, a);
		return this;
	}
	
	@Override
	public Drawable getDrawable() {
		return drawable;
	}
	
}
