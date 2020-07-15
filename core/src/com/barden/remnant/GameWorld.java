package com.barden.remnant;

import java.util.ArrayList;

public class GameWorld {

	protected Remnant game;
	
	protected ArrayList<GameObject> objects;
	private ArrayList<GameObject> objectsToRemove;
	
	public GameWorld(Remnant game){
		this.game = game;
		objects = new ArrayList<GameObject>();
		objectsToRemove = new ArrayList<GameObject>();
	}
	
	public void update(){
		for(int i = 0; i < objects.size(); i++){
			GameObject object = objects.get(i);
			if(object.isExpired()){
				objectsToRemove.add(object);
			} else {
				if(object.isActive)
					object.update();
			}
		}
		
		if(objectsToRemove.size() > 0){
			for(int i = 0; i < objectsToRemove.size(); i++){
				GameObject object = objectsToRemove.get(i);
				objects.remove(object);
			}
			objectsToRemove.clear();
		}
	}
	
	public void add(GameObject obj) {
		objects.add(obj);
	}
	
	public void remove(GameObject obj){
		objectsToRemove.add(obj);
	}
	
	public void clear() {
		objects.clear();
		objectsToRemove.clear();
	}
	
}
