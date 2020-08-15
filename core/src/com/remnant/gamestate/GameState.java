package com.remnant.gamestate;

import com.remnant.engine.Engine;

public abstract class GameState {

	protected Engine game;
	
	public GameState(Engine game){
		this.game = game;
	}
	
	public abstract void load();
	public abstract void update();
	public abstract void unload();
	
}
