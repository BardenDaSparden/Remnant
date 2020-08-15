package com.remnant.gamestate;

import java.io.IOException;

import org.barden.util.Debug;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.remnant.engine.Engine;
import com.remnant.net.Network;
import com.remnant.net.ServerEventHandler;

public class HostMPState extends BaseMPState {

	public HostMPState(Engine game) {
		super(game);
	}
	
	Server server;
	ServerEventHandler serverHandler;
	Listener serverListener;
	
	int idx = 0;
	
	public int getNextID(){
		return idx++;
	}
	
	@Override
	public void load() {
		server = new Server();
		Network.register(server);
		serverHandler = new ServerEventHandler(game, this, server);
		server.start();
		try {
			server.bind(8000, 8001);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		serverListener = new Listener(){
			
			@Override
			public void connected(Connection connection){
				serverHandler.onConnected(connection);
			}
			
			@Override
			public void disconnected(Connection connection){
				serverHandler.onDisconnected(connection);
			}
			
			@Override
			public void received(Connection connection, Object object){
				serverHandler.process(connection, object);
			}
			
			@Override
			public void idle(Connection connection){
				serverHandler.onIdle(connection);
			}
			
		};
		
		server.addListener(serverListener);
		
		super.load();
		Debug.logInfo("Network", "Server INIT");
		Debug.print();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void unload() {
		super.unload();
		server.removeListener(serverListener);
		server.stop();
		Debug.logInfo("Network", "Server STOP");
		Debug.print();
	}
	
}