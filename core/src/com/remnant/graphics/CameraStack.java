package com.remnant.graphics;

public class CameraStack {

	Camera[] cameras;
	int current;
	
	public CameraStack(int width, int height, int size){
		if(size <= 1)
			throw new IllegalArgumentException("Stack size must be > 1");
		cameras = new Camera[size];
		for(int i = 0; i < size; i++){
			cameras[i] = new Camera(width, height);
		}
		current = 0;
	}
	
	public void clear(){
		cameras[0].identity();
		current = 0;
	}
	
	public void push(){
		if(current == cameras.length - 1)
			throw new IllegalStateException("Stack size of (" + (current + 1) + ") reached");
		current++;
	}
	
	public void pop(){
		if(current == 0)
			throw new IllegalStateException("End of stack rached");
		current--;
	}
	
	public void set(Camera c){
		cameras[current].set(c);
	}
	
	public Camera get(Camera c){
		return c.set(cameras[current]);
	}
	
	public Camera current(){
		return cameras[current];
	}
	
}
