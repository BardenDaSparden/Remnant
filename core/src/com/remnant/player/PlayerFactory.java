package com.remnant.player;

import com.remnant.engine.Engine;

public class PlayerFactory {
	
	public static Player createPlayer(Engine game, boolean isSelf, int ID, String name, float x, float y){
		Player player = new Player(game, isSelf);
		player.ID = ID;
		player.name = name;
		player.setPosition(x, y);
		return player;
	}
	
}
