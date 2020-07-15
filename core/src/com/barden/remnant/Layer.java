package com.barden.remnant;

import java.util.ArrayList;

public class Layer {

	String name;
	ArrayList<GameObject> objects;
	boolean isVisible;
	
	public Layer(){
		name = "Layer";
		objects = new ArrayList<GameObject>();
		isVisible = true;
	}
	
	public void load(GameWorld world){
		for(int i = 0; i < objects.size(); i++){
			GameObject object = objects.get(i);
			world.add(object);
		}
	}
	
	public void unload(GameWorld world){
		for(int i = 0; i < objects.size(); i++){
			GameObject object = objects.get(i);
			world.remove(object);
		}
	}
	
	public String getName(){
		return name;
	}
	
	public Layer setName(String name){
		this.name = name;
		return this;
	}
	
	public Layer setVisible(boolean visible){
		this.isVisible = visible;
		for(int i = 0; i < objects.size(); i++){
			GameObject object = objects.get(i);
			Drawable drawable = object.getDrawable();
			if(drawable != null) {
				drawable.isVisible = visible;
			}
		}
		return this;
	}
	
	public ArrayList<GameObject> getObjects(){
		return objects;
	}
	
	public Layer add(GameObject object){
		objects.add(object);
		return this;
	}
	
	public Layer remove(GameObject object){
		objects.remove(object);
		return this;
	}
	
}
