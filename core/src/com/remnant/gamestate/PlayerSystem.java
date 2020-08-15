package com.remnant.gamestate;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2f;

import com.remnant.engine.Engine;
import com.remnant.engine.GameSystem;
import com.remnant.graphics.Camera;
import com.remnant.math.Transform;
import com.remnant.net.packet.PlayerConnectRequest;
import com.remnant.net.packet.PlayerConnectResponse;
import com.remnant.net.packet.PlayerDisconnectRequest;
import com.remnant.net.packet.PlayerDisconnectResponse;
import com.remnant.net.packet.PlayerIDResponse;
import com.remnant.net.packet.PlayerTransformUpdate;
import com.remnant.player.Player;
import com.remnant.player.PlayerCamera;
import com.remnant.player.PlayerController;
import com.remnant.player.PlayerFactory;
import com.remnant.player.PlayerHUDRenderer;
import com.remnant.player.PlayerRenderer;

public class PlayerSystem extends GameSystem {

	BaseMPState multiplayerState;
	
	public PlayerSystem(Engine engine) {
		super(engine);
	}

	String[] PLAYER_NAMES = {
			"Rick",
			"Shmoop",
			"Chiko",
			"Muffin",
			"Drake",
			"Tank",
			"Thud",
			"Jinx"
	};
	
	String[] PLAYER_VERBS = {
			"-Fraggin",
			"-TeeBaggin",
			"-Shootin",
			"-Tootin",
			"-Smurfin",
			"-Boof",
			"-Fud"
	};
	
	Random random = new Random();
	
	ArrayList<Player> players;
	
	Player player;
	PlayerCamera playerCamera;
	PlayerController playerController;
	PlayerRenderer playerRenderer;
	PlayerHUDRenderer playerHUDRenderer;
	
	boolean isPaused = false;
	
	public void setGameState(BaseMPState state){
		multiplayerState = state;
	}
	
	String getRandomName(){
		String name = PLAYER_NAMES[random.nextInt(PLAYER_NAMES.length)] + " " + PLAYER_VERBS[random.nextInt(PLAYER_VERBS.length)];
		return name;
	}
	
	public void net_setID(PlayerIDResponse response){
		player.setID(response.ID);
		PlayerConnectRequest request = new PlayerConnectRequest();
		request.ID = player.getID();
		request.name = player.getName();
		request.x = player.getX();
		request.y = player.getY();
		connect(player);
		engine.client.sendTCP(request);
	}
	
	public void net_registerPlayer(PlayerConnectResponse response){
		Player player = PlayerFactory.createPlayer(engine, false, response.ID, response.name, response.x, response.y);
		connect(player);
	}
	
	public void net_unregisterPlayer(PlayerDisconnectResponse response){
		Player player = getPlayerByID(response.ID);
		disconnect(player);
	}
	
	public void net_playerUpdate(PlayerTransformUpdate update){
		Player player = getPlayerByID(update.ID);
		if(player != null){
			player.getBody().getTransform().setTranslation(new Vector2f(update.x, update.y));
			player.getBody().getTransform().setRotation(update.orient);
		}
	}
	
	boolean isPlayerConnected(int ID){
		boolean found = false;
		for(Player player : players){
			if(ID == player.getID()){
				found = true;
				break;
			}
		}
		return found;
	}
	
	boolean isPlayerConnected(Player p){
		boolean found = false;
		for(Player player : players){
			if(p.getID() == player.getID()){
				found = true;
				break;
			}
		}
		return found;
	}
	
	public Player getPlayerByID(int ID){
		Player p = null;
		for(Player player : players){
			if(ID == player.getID()){
				p = player;
				break;
			}
		}
		return p;
	}
	
	public void disconnect(Player player){
		if(isPlayerConnected(player)){
			engine.remove(player);
			playerRenderer.remove(player);
			//TODO translate action to GameEvent, add event listener to weaponSystem
			multiplayerState.weaponSystem.weaponRenderer.remove(player);
			players.remove(player);
			engine.physics.remove(player);
			player.unload();
		}
	}
	
	public void connect(Player player){
		if(!isPlayerConnected(player)){
			engine.add(player);
			playerRenderer.add(player);
			multiplayerState.weaponSystem.weaponRenderer.add(player);
			players.add(player);
			engine.physics.add(player);
			player.load();
		}
	}
	
	public ArrayList<Player> getConnectedPlayers(){
		return players;
	}
	
	@Override
	public void load() {
		players = new ArrayList<Player>();
		
		player = PlayerFactory.createPlayer(engine, true, -1, getRandomName(), 0 , 512);
		playerCamera = new PlayerCamera(engine.width, engine.height, player);
		playerController = new PlayerController(engine.client, player, engine.input, engine.lighting);
		playerController.setListening(true);
		
		playerRenderer = new PlayerRenderer();
		playerHUDRenderer = new PlayerHUDRenderer(engine.width, engine.height, playerController);
		
		engine.uirenderer.add(playerHUDRenderer);
		engine.renderer.add(playerRenderer);
	}

	@Override
	public void update() {
		playerController.update();
		
		if(!isPaused){
			playerCamera.update();
			Camera rendererCamera = engine.renderer.getCameraStack().current();
			rendererCamera.set(playerCamera);
			Transform transform = player.getBody().getTransform();
			PlayerTransformUpdate update = new PlayerTransformUpdate();
			update.ID = player.getID();
			update.x = transform.getTranslation().x;
			update.y = transform.getTranslation().y;
			update.orient = transform.getRotation();
			engine.client.sendTCP(update);
		}
		
	}

	public void disconnectSelf(){
		PlayerDisconnectRequest request = new PlayerDisconnectRequest();
		request.ID = player.getID();
		engine.client.sendTCP(request);
	}
	
	@Override
	public void unload() {
		PlayerDisconnectRequest request = new PlayerDisconnectRequest();
		request.ID = player.getID();
		engine.client.sendTCP(request);
		
		isPaused = false;
		
		disconnect(player);
		players.remove(player);
		engine.renderer.remove(playerRenderer);
		engine.uirenderer.remove(playerHUDRenderer);
	}
	
}