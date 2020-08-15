package com.remnant.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.remnant.net.packet.PlayerConnectRequest;
import com.remnant.net.packet.PlayerConnectResponse;
import com.remnant.net.packet.PlayerDisconnectRequest;
import com.remnant.net.packet.PlayerDisconnectResponse;
import com.remnant.net.packet.PlayerIDRequest;
import com.remnant.net.packet.PlayerIDResponse;
import com.remnant.net.packet.PlayerTransformUpdate;
import com.remnant.net.packet.WeaponFireRequest;
import com.remnant.net.packet.WeaponFireResponse;
import com.remnant.net.packet.WeaponSwitchRequest;
import com.remnant.net.packet.WeaponSwitchResponse;

public class Network {

	public static void register(Server server){
		Kryo kryo = server.getKryo();
		kryo.register(PlayerIDRequest.class);
		kryo.register(PlayerIDResponse.class);
		kryo.register(PlayerConnectRequest.class);
		kryo.register(PlayerConnectResponse.class);
		kryo.register(PlayerDisconnectRequest.class);
		kryo.register(PlayerDisconnectResponse.class);
		kryo.register(PlayerTransformUpdate.class);
		kryo.register(WeaponFireRequest.class);
		kryo.register(WeaponFireResponse.class);
		kryo.register(WeaponSwitchRequest.class);
		kryo.register(WeaponSwitchResponse.class);
	}
	
	public static void register(Client client){
		Kryo kryo = client.getKryo();
		kryo.register(PlayerIDRequest.class);
		kryo.register(PlayerIDResponse.class);
		kryo.register(PlayerConnectRequest.class);
		kryo.register(PlayerConnectResponse.class);
		kryo.register(PlayerDisconnectRequest.class);
		kryo.register(PlayerDisconnectResponse.class);
		kryo.register(PlayerTransformUpdate.class);
		kryo.register(WeaponFireRequest.class);
		kryo.register(WeaponFireResponse.class);
		kryo.register(WeaponSwitchRequest.class);
		kryo.register(WeaponSwitchResponse.class);
	}
	
}
