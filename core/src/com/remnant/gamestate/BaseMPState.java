package com.remnant.gamestate;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.io.IOException;

import org.barden.util.Debug;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.remnant.assets.SoundClip;
import com.remnant.assets.AudioClip;
import com.remnant.engine.Engine;
import com.remnant.input.KeyboardListener;
import com.remnant.map.Map;
import com.remnant.map.MapIO;
import com.remnant.map.MapRenderer;
import com.remnant.net.ClientEventHandler;
import com.remnant.net.packet.PlayerIDRequest;
import com.remnant.ui.EditorProperties;
import com.remnant.ui.UIStage;
import com.remnant.weapon.WeaponID;

public class BaseMPState extends GameState {

	public BaseMPState(Engine game) {
		super(game);
	}
	
	ClientEventHandler eventHandler;
	Listener clientListener;
	
	public EffectsSystem effectsSystem;
	public WeaponSystem weaponSystem;
	//public PickupSystem pickupSystem;
	public PlayerSystem playerSystem;
	
	Map map;
	MapRenderer mapRenderer;
	AudioClip music;
	
	@Override
	public void load() {
		
		game.client.start();
		
		try {
			game.client.connect(5000, "127.0.0.1", 8000, 8001);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		eventHandler = new ClientEventHandler(game, this);
		clientListener = new Listener(){
			
			@Override
			public void connected(Connection connection){
				eventHandler.onConnected(connection);
			}
			
			@Override
			public void disconnected(Connection connection){
				eventHandler.onDisconnected(connection);
			}
			
			@Override
			public void received(Connection connection, Object object){
				eventHandler.process(connection, object);
			}
			
			@Override
			public void idle(Connection connection){
				eventHandler.onIdle(connection);
			}
			
		};
		
		effectsSystem = new EffectsSystem(game);
		effectsSystem.load();
		
		weaponSystem = new WeaponSystem(game);
		weaponSystem.setGameState(this);
		weaponSystem.load();
		
//		pickupSystem = new PickupSystem(game);
//		pickupSystem.load();
//		
//		pickupSystem.createHealthPickup(0, 200);
//		pickupSystem.createHealthPickup(0, -200);
//		pickupSystem.createHealthPickup(200, 0);
//		pickupSystem.createHealthPickup(-200, 0);
//		
//		pickupSystem.createWeaponPickup(0, 400, WeaponID.ASSAULT_RIFLE);
//		pickupSystem.createWeaponPickup(0, -400, WeaponID.COMBAT_RIFLE);
//		pickupSystem.createWeaponPickup(-400, 0, WeaponID.RIFLE);
//		pickupSystem.createWeaponPickup(400, 0, WeaponID.PISTOL);
		
		
		playerSystem = new PlayerSystem(game);
		playerSystem.setGameState(this);
		playerSystem.load();
		
		music = Engine.Loader().loadAudio("resources/sounds/ambiance-scifi-1.ogg");
		music.setGain(1);
		music.setLooping(true);
		music.play();
		
		game.input.getMouse().setGrabbed(true);

		
		game.input.getMouse().setCursorPosition(0, 0, game.width, game.height);
		game.input.getMouse().setGrabbed(true);
		game.cursor.setVisible(false);
		game.client.addListener(clientListener);
		
		PlayerIDRequest request = new PlayerIDRequest();
		game.client.sendTCP(request);
		
		loadMap("Test.map");
		
		Debug.logInfo("Network", "Client INIT");
		Debug.print();
	}
	
	public void loadMap(String mapName){
		map = new Map(game, "");
		MapIO.read(mapName, map, new EditorProperties());
		mapRenderer = new MapRenderer();
		mapRenderer.setMap(map);
		game.renderer.add(mapRenderer);
	}
	
	@Override
	public void update() {
		playerSystem.update();
		weaponSystem.update();
		effectsSystem.update();
		//pickupSystem.update();
	}

	@Override
	public void unload() {
		game.client.removeListener(clientListener);
		
		//pickupSystem.unload();
		effectsSystem.unload();
		playerSystem.unload();
		weaponSystem.unload();
		map.unload();
		
		music.stop();
		
		game.renderer.remove(mapRenderer);
		
		game.physics.clear();
		game.lighting.clear();
		
		Debug.logInfo("Network", "Client STOP");
		Debug.print();
	}
	
}
