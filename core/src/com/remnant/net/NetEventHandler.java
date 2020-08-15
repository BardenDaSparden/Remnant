package com.remnant.net;

import com.esotericsoftware.kryonet.Connection;
import com.remnant.engine.Engine;
import com.remnant.gamestate.BaseMPState;

public abstract class NetEventHandler {
	
	Engine engine;
	BaseMPState state;
	
	public NetEventHandler(Engine engine, BaseMPState state){
		this.engine = engine;
		this.state = state;
	}
	
	public abstract void onConnected(Connection connection);
	public abstract void onDisconnected(Connection connection);
	public abstract void process(Connection connection, Object object);
	public abstract void onIdle(Connection connection);
	
}
