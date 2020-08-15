package com.remnant.application;

import java.util.ArrayList;

public class ApplicationProperties {

	private String title;
	
	private int width;
	
	private int height;
	
	private boolean bVsync;
	
	private boolean bFullscreen;
	
	private boolean bResizable;
	
	private boolean bBorderless;
	
	ArrayList<ApplicationPropertiesListener> listeners;
	
	public ApplicationProperties(){
		title = "";
		width = 800;
		height = 600;
		bVsync = false;
		bFullscreen = false;
		bResizable = false;
		bBorderless = false;
		listeners = new ArrayList<ApplicationPropertiesListener>();
	}
	
	public void setTitle(String newTitle){
		title = newTitle;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
		onResize(width, height);
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setVSync(boolean vsync){
		bVsync = vsync;
	}
	
	public boolean isVSync(){
		return bVsync;
	}
	
	public void setFullscreen(boolean fullscreen){
		bFullscreen = fullscreen;
	}
	
	public boolean isFullscreen(){
		return bFullscreen;
	}
	
	public void setResizable(boolean resizable){
		bResizable = resizable;
	}
	
	public boolean isResizable(){
		return bResizable;
	}
	
	public void setBorderless(boolean borderless){
		bBorderless = borderless;
	}
	
	public boolean isBorderless(){
		return bBorderless;
	}
	
	void onResize(int width, int height){
		for(int i = 0; i < listeners.size(); i++){
			ApplicationPropertiesListener listener = listeners.get(i);
			listener.onResize(width, height);
		}
	}
	
	void onVSyncChange(boolean vsync){
		for(int i = 0; i < listeners.size(); i++){
			ApplicationPropertiesListener listener = listeners.get(i);
			listener.onVSyncChange(vsync);
		}
	}
	
	public void addListener(ApplicationPropertiesListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(ApplicationPropertiesListener listener){
		listeners.remove(listener);
	}
	
}
