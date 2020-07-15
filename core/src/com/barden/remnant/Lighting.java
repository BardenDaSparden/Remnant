package com.barden.remnant;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Lighting {
	
	ArrayList<Light> lights;
	
	public Lighting(){
		lights = new ArrayList<Light>();
	}
	
	public Light createLight(float x, float y, float r, float g, float b){
		Light light = new Light().setPosition(x, y).setColor(r, g, b, 1);
		lights.add(light);
		return light;
	}
	
	public void removeLight(Light light){
		lights.remove(light);
	}
	
	public void draw(SpriteBatch batch) {
		for(int i = 0; i < lights.size(); i++){
			Light light = lights.get(i);
			Drawable drawable = light.drawable;
			Transform transform = drawable.transform;
			TextureRegion diffuse = drawable.material.diffuse;
			
			if(!drawable.isVisible)
				continue;
			
			if(diffuse != null){
				//TODO may need to draw at center
				batch.draw(diffuse, transform.getPosition().x - diffuse.getRegionWidth() / 2f, transform.getPosition().y - diffuse.getRegionHeight() / 2f);
			}
			
		}
	}
	
}
