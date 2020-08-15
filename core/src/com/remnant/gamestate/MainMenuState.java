package com.remnant.gamestate;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;
import org.joml.Vector2f;

import com.remnant.assets.AudioClip;
import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Color;
import com.remnant.ui.Button;
import com.remnant.ui.FlowLayout;
import com.remnant.ui.HBox;
import com.remnant.ui.Listener;
import com.remnant.ui.UIStage;
import com.remnant.ui.UIStyle;

public class MainMenuState extends GameState {
	
	BackgroundRenderer menuBackgroundRenderer;
	TransitionRenderer transition;
	
	final int FADE_TIME = 75;
	Timer fadeInTimer;
	Timer fadeOutTimer;
	boolean fadingIn = true;
	float opacity = 1;
	int nextState = -1;
	
	UIStage mainStage;
	HBox root;
	Button editorButton;
	Button hostButton;
	Button joinButton;
	
	AudioClip music;
	
	public MainMenuState(Engine game) {
		super(game);
	}
	
	void switchToState(int gamestate){
		nextState = gamestate;
		fadeOutTimer.start();
		fadingIn = false;
	}
	
	@Override
	public void load() {
		menuBackgroundRenderer = new BackgroundRenderer(game.width, game.height);
		transition = new TransitionRenderer(game.width, game.height);
		
		fadeInTimer = new Timer(FADE_TIME);
		fadeOutTimer = new Timer(FADE_TIME);
		fadeOutTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				game.ui.clear();
				game.setGameState(nextState);
			}
		});
		
		fadeInTimer.start();		
	
		game.uirenderer.add(menuBackgroundRenderer);
		game.uirenderer.add(transition);
		
		music = Engine.Loader().loadAudio("resources/sounds/Menu-Dark.ogg");
		music.setGain(0.75f);
		music.setLooping(true);
		music.play();
		
		final int BUTTON_WIDTH = 160;
		final int BUTTON_HEIGHT = 75;
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.getBackgroundColor().set(0.1f, 0.5f, 0.5f, 0.1f);
		
		editorButton = new Button("Map Editor");
		editorButton.setStyle(btnStyle);
		editorButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		editorButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				game.ui.clear();
				game.setGameState(Engine.EDITOR_STATE);
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFocus() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		hostButton = new Button("Host Match");
		hostButton.setStyle(btnStyle);
		hostButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		hostButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				game.ui.clear();
				game.setGameState(Engine.HOST_MP_STATE);
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFocus() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		joinButton = new Button("Join Match");
		joinButton.setStyle(btnStyle);
		joinButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		joinButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				game.ui.clear();
				game.setGameState(Engine.CLIENT_MP_STATE);
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFocus() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		root = new HBox();
		root.setAlignment(FlowLayout.CENTER);
		root.setSpacing(20, 10);
		root.add(editorButton);
		root.add(hostButton);
		root.add(joinButton);
		mainStage = new UIStage(root);
		game.ui.push(mainStage);
	}

	@Override
	public void update() {
		fadeInTimer.tick();
		fadeOutTimer.tick();
		
		if(fadingIn)
			opacity = 1.0f - fadeInTimer.percent();
		 else 
			opacity = fadeOutTimer.percent();
		transition.setOpacity(opacity);
	}

	@Override
	public void unload() {
		game.uirenderer.remove(menuBackgroundRenderer);
		game.uirenderer.remove(transition);
		music.stop();
		fadingIn = true;
	}

}

class BackgroundRenderer extends TypedUIRenderer {
	
	static final int LAYER = 0;
	
	int width;
	int height;
	Camera camera;
	Texture title;
	Texture background;
	
	public BackgroundRenderer(int width, int height){
		super(LAYER);
		this.width = width;
		this.height = height;
		camera = new Camera(width, height);
		background = new Texture("images/ui/background.png");
		title = new Texture("images/ui/title.png");
	}
	
	@Override
	public void draw(){
		
		Vector2f scaleFactor = new Vector2f();
		scaleFactor.x = (float)width / 1920.0f;
		scaleFactor.y = (float)height / 1080.0f;
		
		cameraStack.push();
		cameraStack.current().set(camera);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(0, 0, background.getWidth(), background.getHeight(), 0, scaleFactor.x, scaleFactor.y, background, Color.WHITE);
				batch.draw(0, height / 2 - title.getHeight() / 2 - 100, title.getWidth(), title.getHeight(), 0, scaleFactor.x, scaleFactor.y, title, Color.WHITE);
			batch.end();	
		cameraStack.pop();
	}
}