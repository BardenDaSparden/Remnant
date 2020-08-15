package com.remnant.gamestate;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.FontLoader;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.input.MouseListener;
import com.remnant.math.Color;

public class SplashState extends GameState {
	
	SplashRenderer splashRenderer;
	TransitionRenderer transition;
	
	MouseListener listener;
	
	Vector2f mouse = new Vector2f();
	
	boolean nextState = false;
	
	final int FADE_TIME = 75;
	final int IDLE_TIME = 180;
	Timer fadeInTimer;
	Timer idleTimer;
	Timer fadeOutTimer;
	boolean fadingIn = true;
	float overlayOpacity = 1;
	
	public SplashState(Engine game) {
		super(game);
	}

	@Override
	public void load() {
		listener = new MouseListener() {
			@Override
			public void onScroll(float dy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMove(float dx, float dy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onButtonRelease(int button) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onButtonPress(int button) {
				if(!fadingIn){
					fadeInTimer.stop();
					idleTimer.stop();
					fadeOutTimer.start();
				}
			}
		};
		game.input.getMouse().addListener(listener);
		splashRenderer = new SplashRenderer(game.width, game.height);
		transition = new TransitionRenderer(game.width, game.height);
		game.uirenderer.add(splashRenderer);
		game.uirenderer.add(transition);
		
		fadeInTimer = new Timer(FADE_TIME);
		fadeInTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				idleTimer.start();
				fadingIn = false;
			}
		});
		
		idleTimer = new Timer(IDLE_TIME);
		idleTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				fadeOutTimer.start();
			}
		});
		
		fadeOutTimer = new Timer(FADE_TIME);
		fadeOutTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				nextState = true;
			}
		});
		
		fadeInTimer.start();
		
		game.cursor.setPosition(0, 0);
		game.input.getMouse().setCursorPosition(0, 0, game.width, game.height);
		game.input.getMouse().setGrabbed(true);
		
	}

	@Override
	public void update() {
		
		fadeInTimer.tick();
		idleTimer.tick();
		fadeOutTimer.tick();
		
		if(fadingIn){
			overlayOpacity = 1.0f - fadeInTimer.percent();
		} else {
			overlayOpacity = fadeOutTimer.percent();
		}
		transition.setOpacity(overlayOpacity);
		
		if(nextState)
			game.setGameState(Engine.MAIN_MENU_STATE);
	}

	@Override
	public void unload() {
		game.input.getMouse().removeListener(listener);
		game.uirenderer.remove(splashRenderer);
		game.uirenderer.remove(transition);
	}	
}

class SplashRenderer extends TypedUIRenderer {

	int width, height;
	Camera camera;
	
	Texture splash;
	Texture white;
	BitmapFont font;
	
	Vector4f overlayColor = new Vector4f(0, 0, 0, 1);
	
	String[] libraries = {
			"LWJGL3",
			"JOML",
			"KryoNet"
	};
	
	public SplashRenderer(int width, int height) {
		super(100);
		this.width = width;
		this.height = height;
		splash = new Texture("images/splash1.png");
		white = new Texture("images/white.png");
		font = FontLoader.getFont("fonts/imagine_24.fnt");
		camera = new Camera(width, height);
	}
	
	public void setOverlay(float opacity){
		overlayColor.w = opacity;
	}
	
	@Override
	public void draw() {
		cameraStack.push();
		cameraStack.current().set(camera);
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			batch.draw(0, 0, splash.getWidth(), splash.getHeight(), 0, 1, 1, splash, Color.WHITE);
		batch.end();
		cameraStack.pop();
	}

	
}
