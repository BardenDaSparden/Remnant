package com.remnant.graphics;

import java.util.ArrayList;
import java.util.Collections;

public class UIRenderer {

	protected CameraStack cameraStack;
	protected TextureBatch batch;
	boolean bSort = false;
	ArrayList<TypedUIRenderer> systems;
	
	public UIRenderer(CameraStack cameraStack, TextureBatch batch){
		this.cameraStack = cameraStack;
		this.batch = batch;
		systems = new ArrayList<TypedUIRenderer>();
	}
	
	public void draw(){
		if(bSort)
			Collections.sort(systems);
		
		for(int i = 0; i < systems.size(); i++){
			TypedUIRenderer renderer = systems.get(i);
			renderer.draw();
		}
	}
	
	public void add(TypedUIRenderer system){
		system.setCameraStack(cameraStack);
		system.setTextureBatch(batch);
		systems.add(system);
		bSort = true;
	}
	
	public void remove(TypedUIRenderer system){
		systems.remove(system);
	}
	
}
