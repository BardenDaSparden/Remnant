package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.ArrayList;
import java.util.Collections;

import org.barden.util.GPUProfiler;
import org.joml.Vector2f;

import com.remnant.assets.Texture.Filter;
import com.remnant.assets.Texture.Format;
import com.remnant.math.Color;
import com.remnant.shaders.CompositeShader;
import com.remnant.shaders.GaussianBlurShader;
import com.remnant.shaders.PositionShader;

public class Renderer {

	final Vector2f HORIZONTAL = new Vector2f(1, 0);
	final Vector2f VERTICAL = new Vector2f(0, 1);
	final float BLUR_SIZE = 1f;
	
	int width;
	int height;
	
	CameraStack cameraStack;
	Lighting lighting;
	
	TextureBatch batch;
	
	ArrayList<TypedRenderer> renderSystems;
	
	Framebuffer positionTarget;
	Framebuffer diffuseTarget;
	Framebuffer normalTarget;
	Framebuffer glowTarget;
	Framebuffer lightTarget;
	Framebuffer[] bloomTargets;
	Framebuffer bloomTarget;
	
	Shader gbufferShader;
	Shader positionShader;
	Shader gBlurShader;
	Shader compositeShader;
	
	public Renderer(int width, int height, Lighting lighting){
		this.width = width;
		this.height = height;
		this.lighting = lighting;
		
		cameraStack = new CameraStack(width, height, 5);
		renderSystems = new ArrayList<TypedRenderer>();
		
		batch = new TextureBatch(cameraStack, 100000);
		
		positionTarget = new Framebuffer(width, height, Format.RGBA_FLOAT_16);
		diffuseTarget = new Framebuffer(width, height);
		normalTarget = new Framebuffer(width, height);
		glowTarget = new Framebuffer(width, height);
		lightTarget = new Framebuffer(width, height, Format.RGBA_FLOAT_16);
		bloomTarget = new Framebuffer(width, height);
		
		positionShader = PositionShader.get();
		gBlurShader = GaussianBlurShader.get();
		compositeShader = CompositeShader.get();
		
		//Generate ping-pong-able framebuffer chain, with each pair being down-sampled by a factor of 2
		int fboWidth = width;
		int fboHeight= height;
		
		bloomTargets = new Framebuffer[8];
		for(int i = 0; i < 8; i+=2){
			bloomTargets[i] 	= new Framebuffer(fboWidth, fboHeight);
			bloomTargets[i+1] 	= new Framebuffer(fboWidth, fboHeight);
			bloomTargets[i].getColorTexture().setFilter(Filter.LINEAR, Filter.LINEAR);
			bloomTargets[i+1].getColorTexture().setFilter(Filter.LINEAR, Filter.LINEAR);
			fboWidth /= 2;
			fboHeight /= 2;
		}
	}
	
	public void clear(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}
	                    
	
	void drawPositionPass(double alpha){
		positionTarget.bind();
		positionTarget.clear(0, 0, 0, 1);
			batch.setShader(positionShader);
			for(int i = 0; i < renderSystems.size(); i++){
				TypedRenderer system = renderSystems.get(i);
				system.drawPositions(alpha);
			}
			batch.setShader(null);
		positionTarget.release();
	}
	
	void drawDiffusePass(double alpha){
		diffuseTarget.bind();
		diffuseTarget.clear(0, 0, 0, 1);
			for(int i = 0; i < renderSystems.size(); i++){
				TypedRenderer system = renderSystems.get(i);
				system.drawDiffuse(alpha);
			}
		diffuseTarget.release();
	}
	
	void drawNormalPass(double alpha){
		normalTarget.bind();
		normalTarget.clear(0.5f, 0.5f, 1, 1);
			for(int i = 0; i < renderSystems.size(); i++){
				TypedRenderer system = renderSystems.get(i);
				system.drawNormals(alpha);
			}
		normalTarget.release();
	}
	
	void drawGlowPass(double alpha){
		glowTarget.bind();
		glowTarget.clear(0, 0, 0, 1);
			for(int i = 0; i < renderSystems.size(); i++){
				TypedRenderer system = renderSystems.get(i);
				system.drawIllumination(alpha);
			}
		glowTarget.release();
	}
	
	void drawEmissive(double alpha){
		lightTarget.bindForWrite();
		//lightTarget.clear(0, 0, 0, 1);
			for(int i = 0; i < renderSystems.size(); i++){
				TypedRenderer system = renderSystems.get(i);
				system.drawEmissive(alpha);
			}
		lightTarget.release();
	}
	
	void drawLights(){
		//lighting.castShadows();
		lighting.drawLights(batch, cameraStack, lightTarget, normalTarget.getColorTexture(), positionTarget.getColorTexture());
	}
	
	void drawBloom(){
		Framebuffer buffer = bloomTargets[0];
		Framebuffer buffer2 = bloomTargets[1];
		batch.setShader(gBlurShader);
		
		//Horizontal Pass
		gBlurShader.bind();
			gBlurShader.setFloat("texelSize", BLUR_SIZE / buffer.getWidth());
			gBlurShader.setVector("blurDir", HORIZONTAL);
		gBlurShader.release();
		buffer.bind();
		buffer.clear(0, 0, 0, 1);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.drawFullscreenQuad(glowTarget.getColorTexture());
			batch.end();
		buffer.release();
		
		//Vertical Pass
		gBlurShader.bind();
			gBlurShader.setFloat("texelSize", BLUR_SIZE / buffer.getHeight());
			gBlurShader.setVector("blurDir", VERTICAL);
		gBlurShader.release();
		buffer2.bind();
		buffer2.clear(0, 0, 0, 1);
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.drawFullscreenQuad(buffer.getColorTexture());
			batch.end();
		buffer2.release();
		
		for(int i = 2; i < bloomTargets.length; i+=2){
			Framebuffer prev = bloomTargets[i-1];
			Framebuffer ping = bloomTargets[i];
			Framebuffer pong = bloomTargets[i+1];
			float texSizeH = BLUR_SIZE / ping.getWidth();
			float texSizeV = BLUR_SIZE / ping.getHeight();
			
			//Horizontal Pass
			gBlurShader.bind();
				gBlurShader.setFloat("texelSize", texSizeH);
				gBlurShader.setVector("blurDir", HORIZONTAL);
			gBlurShader.release();
			ping.bind();
			ping.clear(0, 0, 0, 1);
				batch.begin(BlendState.ALPHA_TRANSPARENCY);
					batch.drawFullscreenQuad(prev.getColorTexture());
				batch.end();
			ping.release();
			
			//Vertical Pass
			gBlurShader.bind();
				gBlurShader.setFloat("texelSize", texSizeV);
				gBlurShader.setVector("blurDir", VERTICAL);
			gBlurShader.release();
			pong.bind();
			pong.clear(0, 0, 0, 1);
				batch.begin(BlendState.ALPHA_TRANSPARENCY);
					batch.drawFullscreenQuad(ping.getColorTexture());
				batch.end();
			pong.release();
		}
		batch.setShader(null);	
	}
	
	public void draw(double alpha){
		
		for(int i = 0; i < renderSystems.size(); i++){
			TypedRenderer system = renderSystems.get(i);
			system.prepare();
		}
		
		drawPositionPass(alpha);
		drawDiffusePass(alpha);
		drawNormalPass(alpha);
		drawGlowPass(alpha);
		
		drawLights();
		batch.setShader(null);
		drawEmissive(alpha);
		
		cameraStack.push();
		Camera camera = cameraStack.current();
		camera.identity();
		camera.setSize(width, height);
		
			drawBloom();
			bloomTarget.bind();
			bloomTarget.clear(0, 0, 0, 0);
				batch.begin(BlendState.ADDITIVE);
					batch.drawFullscreenQuad(bloomTargets[1].getColorTexture());
					batch.drawFullscreenQuad(bloomTargets[3].getColorTexture());
					batch.drawFullscreenQuad(bloomTargets[5].getColorTexture());
					batch.drawFullscreenQuad(bloomTargets[7].getColorTexture());
				batch.end();
			bloomTarget.release();
			
			
			batch.setShader(compositeShader);
			compositeShader.bind();
				compositeShader.setTexture("light", lightTarget.getColorTexture(), 1);
				compositeShader.setTexture("bloom", bloomTarget.getColorTexture(), 2);
				batch.begin(BlendState.ALPHA_TRANSPARENCY);
					batch.drawFullscreenQuad(diffuseTarget.getColorTexture());
				batch.end();
			compositeShader.release();
		batch.setShader(null);
		cameraStack.pop();
	}
	
	public void debugDraw(){
		float halfW = width / 2;
		float halfH = height / 2;
		
		float w = width / 6;
		float h = height / 6;
		
		batch.setShader(null);
		cameraStack.push();
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(halfW - w / 2 - 20, halfH - h / 2 - 20, w, h, 0, 1, 1, positionTarget.getColorTexture(), Color.WHITE);
			batch.end();
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(halfW - w / 2 - 20, halfH - (h / 2 + h) - 20, w, h, 0, 1, 1, diffuseTarget.getColorTexture(), Color.WHITE);
			batch.end();
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(halfW - w / 2 - 20, halfH - (h / 2 + h + h) - 20, w, h, 0, 1, 1, normalTarget.getColorTexture(), Color.WHITE);
			batch.end();
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(halfW - w / 2 - 20, halfH - (h / 2 + h + h + h) - 20, w, h, 0, 1, 1, glowTarget.getColorTexture(), Color.WHITE);
			batch.end();
			batch.begin(BlendState.ALPHA_TRANSPARENCY);
				batch.draw(halfW - w / 2 - 20, halfH - (h / 2 + h + h + h + h) - 20, w, h, 0, 1, 1, lightTarget.getColorTexture(), Color.WHITE);
			batch.end();
		cameraStack.pop();
	}
	
	public void destroy(){
		
	}
	
	void addAll(TypedRenderer system){
		if(system.hasChildRenderers()){
			renderSystems.add(system);
			system.batch = batch;
			system.cameraStack = cameraStack;
			for(TypedRenderer subSystem : system.renderers)
				addAll(subSystem);
		} else {
			renderSystems.add(system);
			system.batch = batch;
			system.cameraStack = cameraStack;
		}
	}
	
	void removeAll(TypedRenderer system){
		if(system.hasChildRenderers()){
			renderSystems.remove(system);
			for(TypedRenderer subSystem : system.renderers)
				removeAll(subSystem);
		} else {
			renderSystems.remove(system);
		}
	}
	
	public void add(TypedRenderer system){
		addAll(system);
		//system.printAll(0);
		Collections.sort(renderSystems);
		//system.printAll(0);
		//System.out.println(renderSystems.size());
	}
	
	public void remove(TypedRenderer system){
		removeAll(system);
		//system.printAll(0);
		Collections.sort(renderSystems);
	}
	
	public CameraStack getCameraStack(){
		return cameraStack;
	}
	
	public TextureBatch spriteBatch(){
		return batch;
	}
}