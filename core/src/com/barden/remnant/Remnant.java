package com.barden.remnant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

public class Remnant extends Game {

	//Logic
	final int UPDATES_PER_SECOND = 60;
	final float DT = 1.0f / (float)UPDATES_PER_SECOND;
	final int MAX_UPDATES_PER_FRAME = 2;
	final double IDEAL_FRAME_LENGTH_NS = 1000000000.0D / (double)UPDATES_PER_SECOND;
	
	int updatesThisFrame = 0;
	long previousTime;
	long currentTime;
	long elapsedTime;
	double accumulator;
	
	SpriteBatch batch;
	
	World world;
	
	GameScreen screen;
	
	Skin skin;
	Stage stage;
	
	Array<Body> bodies = new Array<Body>();
	private void removeExpiredBodies(){
		world.getBodies(bodies);
		for(int i = 0; i < bodies.size; i++){
			Body body = bodies.get(i);
			Object data = body.getUserData();
			if(data instanceof GameObject){
				GameObject goData = (GameObject) data;
				if(goData.isExpired())
					world.destroyBody(body);
			}
		}
	}
	
	Array<Joint> joints = new Array<Joint>();
	private void removeExpiredJoints(){
		world.getJoints(joints);
		for(int i = 0; i < joints.size; i++){
			Joint joint = joints.get(i);
			Object data = joint.getUserData();
			if(data instanceof GameObject){
				GameObject gameObject = (GameObject) data;
				if(gameObject.isExpired())
					world.destroyJoint(joint);
			}
		}
	}
	
	@Override
	public void create() {
		previousTime = TimeUtils.nanoTime();
		currentTime = TimeUtils.nanoTime();
		elapsedTime = 0;
		accumulator = 0.0d;
		
		batch = new SpriteBatch();
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GameContactListener());
		world.setContinuousPhysics(true);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		Viewport viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(viewport);
		
		VisUI.load();
		
		screen = new EditorScreen(this);
		screen.show();
	}

	@Override
	public void render() {
		previousTime = currentTime;
		currentTime = TimeUtils.nanoTime();
		accumulator += (currentTime - previousTime);
		updatesThisFrame = 0;
		
		while(accumulator >= IDEAL_FRAME_LENGTH_NS && updatesThisFrame < MAX_UPDATES_PER_FRAME){
			stage.act();
			world.step(DT, 6, 2);
			
			screen.update();
			
			accumulator -= IDEAL_FRAME_LENGTH_NS;
			updatesThisFrame++;
		}
		
		removeExpiredBodies();
		removeExpiredJoints();
		
		float interpolation = (float) (accumulator / (float)IDEAL_FRAME_LENGTH_NS);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.render(interpolation);
		stage.draw();
	}
	
	@Override
	public void dispose(){
		batch.dispose();
		world.dispose();
		stage.dispose();
		skin.dispose();
		VisUI.dispose();
	}
}