package com.remnant.engine;

import com.remnant.application.Application;
import com.remnant.application.ApplicationProperties;

public class Remnant extends Application {
	
	public static void main(String[] args){
		launch(args, new Remnant());
	}
	
	public Remnant(){
		ApplicationProperties properties = getProperties();
		properties.setTitle("Remnant");
		properties.setSize(1920, 1080);
		properties.setBorderless(false);
		properties.setFullscreen(true);
		properties.setVSync(false);
	}
	
	Engine engine;
	
	public void load(){
		engine = new Engine(this);
		engine.load();
	}
	
	public void update(){
		engine.update();
	}
	
	public void render(double alpha){
		engine.render(alpha);
	}
	
	public void unload(){
		engine.unload();
	}
	
}