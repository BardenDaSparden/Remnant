package com.barden.remnant;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Drawable implements Comparable<Drawable> {

	protected Transform transform;
	
	protected Material material;
	
	protected boolean isVisible = true;
	
	protected int zIndex = 0;
	
	public Drawable(){
		transform = new Transform();
		material = new Material();
	}
	
	public void setPosition(float x, float y){
		transform.set(x, y);
	}
	
	public void setDiffuse(TextureRegion region){
		material.diffuse = region;
	}
	
	public void setVisible(boolean value){
		isVisible = value;
	}

	@Override
	public int compareTo(Drawable other) {
		int diff = this.zIndex - other.zIndex;
		if(diff < 0)
			return 1;
		else if(diff > 0)
			return -1;
		else
			return 0;
	}
	
}
