package com.barden.remnant.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.barden.remnant.Remnant;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Remnant");
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());  
		config.setMaximized(true);
		config.useVsync(true);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 1);
		new Lwjgl3Application(new Remnant(), config); 
	}	
}