package com.remnant.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.remnant.engine.Engine;
import com.remnant.gamestate.BaseMPState;
import com.remnant.net.packet.PlayerConnectResponse;
import com.remnant.net.packet.PlayerDisconnectResponse;
import com.remnant.net.packet.PlayerIDResponse;
import com.remnant.net.packet.PlayerTransformUpdate;
import com.remnant.net.packet.WeaponFireResponse;

public class ClientEventHandler extends NetEventHandler {
	
	Client client;
	
	public ClientEventHandler(Engine engine, BaseMPState state) {
		super(engine, state);
		this.client = engine.client;
	}
	
	@Override
	public void process(Connection connection, Object object){
		if(object instanceof PlayerIDResponse){
			PlayerIDResponse response = (PlayerIDResponse) object;
			state.playerSystem.net_setID(response);
			//statenet_setID(response);
		}
		
		if(object instanceof PlayerConnectResponse){
			PlayerConnectResponse response = (PlayerConnectResponse) object;
			state.playerSystem.net_registerPlayer(response);
			//state.net_registerPlayer(response);
		}
		
		if(object instanceof PlayerDisconnectResponse){
			PlayerDisconnectResponse response = (PlayerDisconnectResponse) object;
			state.playerSystem.net_unregisterPlayer(response);
			//state.net_unregisterPlayer(response);
		}
		
		if(object instanceof PlayerTransformUpdate){
			PlayerTransformUpdate update = (PlayerTransformUpdate) object;
			state.playerSystem.net_playerUpdate(update);
			//state.net_playerUpdate(update);
		}
		
		if(object instanceof WeaponFireResponse){
			WeaponFireResponse response = (WeaponFireResponse) object;
			//state.net_playerShoot(response);
			state.weaponSystem.net_playerShoot(response);
		}
	}

	@Override
	public void onConnected(Connection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected(Connection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIdle(Connection connection) {
		// TODO Auto-generated method stub
		
	}
}
