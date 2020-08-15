package com.remnant.particles;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;
import com.remnant.math.Transform;

public class ParticleRenderer extends TypedRenderer {
	
	static final String NAME = "Particle Renderer";
	
	Particles system;
	ArrayList<Particle> particles;
	Texture white;
	
	public ParticleRenderer(Particles system, int layer){
		super(NAME, layer);
		this.system = system;
		particles = new ArrayList<Particle>();
		white = new Texture("images/white.png");
	}
	
	@Override
	public void prepare() {
		particles = system.particles;
	}

	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			if(p == null)
				continue;
			Transform t = p.transform;
			Vector2f position = t.getTranslation();
			Vector2f scale = t.getScale();
			Texture texture = p.texture;
			batch.draw(position.x, position.y, texture.getWidth(), texture.getHeight(), t.getRotation(), scale.x, scale.y, texture, p.color);
		}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ADDITIVE);
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			if(p == null){
				continue;
			}
			Transform t = p.transform;
			Vector2f position = t.getTranslation();
			Vector2f scale = t.getScale();
			Texture texture = p.texture;
			batch.draw(position.x, position.y, texture.getWidth(), texture.getHeight(), t.getRotation(), scale.x, scale.y, texture, p.color);
		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawIllumination(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			if(p == null)
				continue;
			Transform t = p.transform;
			Vector2f position = t.getTranslation();
			Vector2f scale = t.getScale();
			Texture texture = p.texture;
			Vector4f color = new Vector4f(p.color);
			color.x = p.illum * p.color.x;
			color.y = p.illum * p.color.y;
			color.z = p.illum * p.color.z;
			color.w = p.color.w;
			batch.draw(position.x, position.y, texture.getWidth(), texture.getHeight(), t.getRotation(), scale.x, scale.y, texture, color);
		}
		batch.end();
	}
	
	public void drawEmissive(double alpha){
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			if(p == null)
				continue;
			Transform t = p.transform;
			Vector2f position = t.getTranslation();
			Vector2f scale = t.getScale();
			Texture texture = p.texture;
			Vector4f color = new Vector4f(p.emissiveColor);
			color.x = 1;
			color.y = 1;
			color.z = 1;
			color.w = p.color.w;
			if(p.useDiffuseAsEmissive){
				texture = p.texture;
			} else 
				texture = white;
			batch.draw(position.x, position.y, texture.getWidth(), texture.getHeight(), t.getRotation(), scale.x, scale.y, texture, color);
		}
		batch.end();
	}

}
