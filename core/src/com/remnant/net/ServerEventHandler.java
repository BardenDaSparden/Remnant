package com.remnant.net;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.remnant.engine.Engine;
import com.remnant.gamestate.HostMPState;
import com.remnant.net.packet.PlayerConnectRequest;
import com.remnant.net.packet.PlayerConnectResponse;
import com.remnant.net.packet.PlayerDisconnectRequest;
import com.remnant.net.packet.PlayerDisconnectResponse;
import com.remnant.net.packet.PlayerIDRequest;
import com.remnant.net.packet.PlayerIDResponse;
import com.remnant.net.packet.PlayerTransformUpdate;
import com.remnant.net.packet.WeaponFireRequest;
import com.remnant.net.packet.WeaponFireResponse;
import com.remnant.player.Player;

public class ServerEventHandler extends NetEventHandler {
	
	Server server;
	HostMPState hostState;
	
	public ServerEventHandler(Engine game, HostMPState state, Server server) {
		super(game, state);
		this.hostState = state;
		this.server = server;
	}
	
	//This method is called when the client sends a message
	@Override
	public void process(Connection connection, Object object){
		
		//Creating a new player
		if(object instanceof PlayerIDRequest){
			PlayerIDResponse response = new PlayerIDResponse();
			response.ID = hostState.getNextID();
			connection.sendTCP(response);
		}
		
		//Connect player
		if(object instanceof PlayerConnectRequest){
			PlayerConnectRequest request = (PlayerConnectRequest) object;
			//hostState.connect(PlayerFactory.createPlayer(client, request.ID, request.name, request.x, request.y));
			ArrayList<Player> connectedPlayers = state.playerSystem.getConnectedPlayers();
			PlayerConnectResponse response = new PlayerConnectResponse();
			response.ID = request.ID;
			response.name = request.name;
			response.x = request.x;
			response.y = request.y;
			hostState.playerSystem.net_registerPlayer(response);
			
			for(Player player : connectedPlayers){
				
				if(request.ID == player.getID())
					continue;
				
				response.ID = player.getID();
				response.name = player.getName();
				response.x = player.getX();
				response.y = player.getY();
				server.sendToAllTCP(response);
			}
		}
		
		if(object instanceof PlayerDisconnectRequest){
			PlayerDisconnectRequest request = (PlayerDisconnectRequest) object;
			PlayerDisconnectResponse response = new PlayerDisconnectResponse();
			response.ID = request.ID;
			server.sendToAllTCP(response);
		}
		
		//Update Player Position / Rotation
		if(object instanceof PlayerTransformUpdate){
			PlayerTransformUpdate update = (PlayerTransformUpdate) object;
			server.sendToAllTCP(update);
		}
		
		if(object instanceof WeaponFireRequest){
			WeaponFireRequest request = (WeaponFireRequest) object;
			WeaponFireResponse response = new WeaponFireResponse();
			response.playerID = request.playerID;
			response.isPrimary = request.isPrimary;
			server.sendToAllTCP(response);
		}
	}

	@Override
	public void onConnected(Connection connection) {
		//System.out.println("[SERVER] Connection ID: " + connection.getID());
	}

	@Override
	public void onDisconnected(Connection connection) {
		
	}

	@Override
	public void onIdle(Connection connection) {
		
	}
}
