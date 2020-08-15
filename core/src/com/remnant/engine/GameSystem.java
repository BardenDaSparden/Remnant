package com.remnant.engine;

import java.util.ArrayList;

public abstract class GameSystem {

	protected Engine engine;
	
	protected ArrayList<GameEventListener> listeners;
	
	public GameSystem(Engine game){
		this.engine = game;
		listeners = new ArrayList<GameEventListener>();
	}
	
	public abstract void load();
	public abstract void update();
	public abstract void unload();
	
	public void recieve(GameEvent event){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).process(event);
	}
	
}

interface GameEventListener {
	
	public void process(GameEvent event);
	
}

interface GameEvent {
	
}