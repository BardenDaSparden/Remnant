package com.barden.remnant;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class GameRenderer {

	SpriteBatch batch;
	
	OrthographicCamera camera;
	
	FrameBuffer diffuseBuffer;
	FrameBuffer lightBuffer;
	
	ArrayList<Drawable> drawables;
	
	public GameRenderer(SpriteBatch batch){
		this.batch = batch;
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(width, height);
		
		drawables = new ArrayList<Drawable>();
	}
	
	public void initBuffers(int width, int height){
		diffuseBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);
		lightBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);
	}
	
	public void setCamera(OrthographicCamera camera){
		this.camera = camera;
	}
	
	public void draw(Lighting lighting, GameWorld world){
		
		//Add all available drawable objects to queue
		ArrayList<GameObject> objects = world.objects;
		for(int i = 0; i < objects.size(); i++){
			GameObject object = objects.get(i);
			Drawable drawable = object.getDrawable();
			if(drawable != null)
				drawables.add(drawable);
		}
		
		//Sort drawables by z-index
		Collections.sort(drawables);
		
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		//diffuseBuffer.begin();
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
				//Draw objects
				for(int i = 0; i < drawables.size(); i++){
					Drawable drawable = drawables.get(i);
					Transform transform = drawable.transform;
					TextureRegion diffuse = drawable.material.diffuse;
					
					if(!drawable.isVisible)
						continue;
					
					if(diffuse != null){
						//TODO may need to draw at center
						batch.draw(diffuse, transform.getPosition().x - diffuse.getRegionWidth() / 2f, transform.getPosition().y - diffuse.getRegionHeight() / 2f);
					}
				}
			batch.end();
		//diffuseBuffer.end();
		
		batch.disableBlending();
		//Clear queue
		drawables.clear();
	}
}
