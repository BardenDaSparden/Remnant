package com.remnant.engine;

import java.util.ArrayList;

import org.barden.util.GPUProfiler;

import com.esotericsoftware.kryonet.Client;
import com.remnant.application.Application;
import com.remnant.assets.AssetLoader;
import com.remnant.gamestate.ClientMPState;
import com.remnant.gamestate.EditorState;
import com.remnant.gamestate.GameState;
import com.remnant.gamestate.HostMPState;
import com.remnant.gamestate.MainMenuState;
import com.remnant.gamestate.SplashState;
import com.remnant.graphics.Lighting;
import com.remnant.graphics.Renderer;
import com.remnant.graphics.UIRenderer;
import com.remnant.input.Input;
import com.remnant.input.KeyboardListener;
import com.remnant.input.MouseListener;
import com.remnant.net.Network;
import com.remnant.particles.ParticleRenderer;
import com.remnant.particles.Particles;
import com.remnant.physics.PhysicsDebugRenderer;
import com.remnant.physics.Physics;
import com.remnant.ui.Cursor;
import com.remnant.ui.CursorRenderer;
import com.remnant.ui.UI;
import com.remnant.ui.UISceneRenderer;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {
	
	//Engine Constants
	final String ENGINE_VERSION = "Alpha 0.3";
	final float PHYSICS_TIMESTEP = 1.0f / 60.0f;
	
	//Application that created this engine instance
	public Application application;
	
	//Used for Network communication
	public Client client;
	
	//Engine Systems
	static public AssetLoader assets;
	public Input input;
	public Physics physics;
	public Particles particles;
	public UI ui;
	public Cursor cursor;
	public Lighting lighting;
	public Renderer renderer;
	
	//Engine Renderers
	public UIRenderer uirenderer;
	protected ParticleRenderer particleRenderer;
	protected UISceneRenderer uiSceneRenderer;
	protected CursorRenderer cursorRenderer;
	MouseListener mouseListener;
	
	public enum GState {
		 
		GState(){
			
		}
		
		
	}
	
	//Game States
	public static final int SPLASH_STATE = 0;
	public static final int MAIN_MENU_STATE = 1;
	public static final int HOST_MP_STATE = 2;
	public static final int CLIENT_MP_STATE = 3;
	public static final int EDITOR_STATE = 4;
	public static final int NUM_STATES = 5;
	GameState[] gameStates = new GameState[NUM_STATES];
	int gameStateIDX = SPLASH_STATE;
	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	//Misc Data
	public int width;
	public int height;
	
	//Debugging systems / data
	protected DebugRenderer debugRenderer;
	protected PhysicsDebugRenderer debugPhysicsRenderer;
	KeyboardListener debugKeyListener;
	boolean isDebug = false;
	
	public Engine(Application application){
		this.application = application;
	}
	
	//Called once on program startup
	public void load(){
		
		width = application.getProperties().getWidth();
		height = application.getProperties().getHeight();

		client = new Client();
		Network.register(client);
		client.start();
		
		assets = new AssetLoader();
		input = application.getInputSystem();
		physics = new Physics(PHYSICS_TIMESTEP);
		particles = new Particles();
		ui = new UI(input);
		cursor = new Cursor();
		lighting = new Lighting();
		renderer = new Renderer(width, height, lighting);
		
		uirenderer = new UIRenderer(renderer.getCameraStack(), renderer.spriteBatch());
		particleRenderer = new ParticleRenderer(particles, 1001);
		uiSceneRenderer = new UISceneRenderer(width, height);
		cursorRenderer = new CursorRenderer(width, height);
		cursorRenderer.setCursor(cursor);
		
		mouseListener = new MouseListener() {
			
			@Override
			public void onScroll(float dy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMove(float dx, float dy) {
				cursor.move(dx, dy);
			}
			
			@Override
			public void onButtonRelease(int button) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onButtonPress(int button) {
				// TODO Auto-generated method stub
				
			}
		};
		
		gameStates[SPLASH_STATE] = new SplashState(this);
		gameStates[MAIN_MENU_STATE] = new MainMenuState(this);
		gameStates[HOST_MP_STATE] = new HostMPState(this);
		gameStates[CLIENT_MP_STATE] = new ClientMPState(this);
		gameStates[EDITOR_STATE] = new EditorState(this);
		
		debugRenderer = new DebugRenderer(width, height);
		debugPhysicsRenderer = new PhysicsDebugRenderer(width, height);
		//Add listener for toggling debug mode while playing
		debugKeyListener = new KeyboardListener() {
			
			public void onKeyRelease(int key) { }
			
			public void onKeyPress(int key) {
				if(key == GLFW_KEY_GRAVE_ACCENT)
					toggleDebugMode();
			}

			@Override
			public void onChar(int codepoint) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		
		lighting.setPhysics(physics);
		
		assets.create();
		
		input.getKeyboard().addListener(debugKeyListener);
		input.getMouse().addListener(mouseListener);
		renderer.add(particleRenderer);
		uirenderer.add(uiSceneRenderer);
		uirenderer.add(debugRenderer);
		uirenderer.add(debugPhysicsRenderer);
		uirenderer.add(cursorRenderer);
		
		debugRenderer.setGameState(gameStateIDX);
		debugRenderer.setVersion(ENGINE_VERSION);
		debugPhysicsRenderer.setPhysics(physics);
		
		gameStates[gameStateIDX].load();
	}
	
	double previousTime = 0;
	double currentTime = 0;
	
	public void update(){
		
		previousTime = System.nanoTime();
		particles.update();
		ui.update();
		gameStates[gameStateIDX].update();
		physics.update();
		
		for(int i = 0; i < gameObjects.size(); i++){
			GameObject go = gameObjects.get(i);
			if(go.isExpired){
				go.onExpire();
				gameObjects.remove(i);
			} else {
				if(go.isActive)
					go.update();
			}
		}
		
		uiSceneRenderer.setStage(ui.current());
		debugRenderer.setNumPhysicsObjects(physics.getNumObjects());
		debugRenderer.setNumPhysicsIntersections(physics.getNumIntersections());
		debugRenderer.setNumLights(lighting.getNumLights());
		//debugRenderer.setNumOccluders(lighting.getNumOccluders());
		debugRenderer.setNumDynamicObects(physics.getNumObjects() - physics.getNumStatic());
		debugRenderer.setNumStaticObects(physics.getNumStatic());
		
		currentTime = System.nanoTime();
		debugRenderer.setUpdateTime((currentTime - previousTime) / 1000000);
	}
	
	public void render(double alpha){
		GPUProfiler profiler = GPUProfiler.get();
		profiler.begin();
			profiler.startTask("Engine Render");
				renderer.draw(alpha);
				uirenderer.draw();
//				if(isDebug)
//					renderer.debugDraw();
			profiler.endTask();
		profiler.end();
		debugRenderer.setRenderTime(profiler.getFirstFrameTime());
		//.print();
	}
	
	//Called on application close
	public void unload(){
		
		//Texture.deleteAll();
		
		client.close();
		client.stop();
		
		gameStates[gameStateIDX].unload();
		
		input.getKeyboard().addListener(debugKeyListener);
		input.getMouse().removeListener(mouseListener);
		
		renderer.remove(particleRenderer);
		uirenderer.remove(uiSceneRenderer);
		uirenderer.remove(debugRenderer);
		uirenderer.remove(debugPhysicsRenderer);
		uirenderer.remove(cursorRenderer);
		renderer.destroy();
		
		assets.unloadAll();
		assets.dispose();
	}
	
	//Exit application from the top-down
	public void exit(){
		application.stop();
	}
	
	//Toggle debug mode and rendering its features
	protected void toggleDebugMode(){
		isDebug = !isDebug;
		debugRenderer.toggle();
		debugPhysicsRenderer.toggle();
	}
	
	public GameState getGameState(){
		return gameStates[gameStateIDX];
	}
	
	public void setGameState(int stateIDX){
		if(stateIDX < 0 || stateIDX > NUM_STATES)
			throw new IllegalArgumentException("Game State Index is not valid!");
		gameStates[gameStateIDX].unload();
		gameStateIDX = stateIDX;
		gameStates[gameStateIDX].load();
		debugRenderer.setGameState(stateIDX);
	}
	
	public void add(GameObject go){
		gameObjects.add(go);
	}
	
	public void remove(GameObject go){
		gameObjects.remove(go);
	}
	
	public int numObjects(){
		return gameObjects.size();
	}
	
	public static AssetLoader Loader(){
		return assets;
	}
	
}