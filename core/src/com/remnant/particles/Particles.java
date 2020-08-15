package com.remnant.particles;

import java.util.ArrayList;
import org.joml.Vector2f;
import com.remnant.math.Transform;

public class Particles{

	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Particle> particlesToRemove = new ArrayList<Particle>();
	
	ArrayList<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();
	ArrayList<ParticleEmitter> emittersToRemove = new ArrayList<ParticleEmitter>();
	
	public Particles(){
		
	}
	
	public void update(){
		
		for(int i = 0; i < emitters.size(); i++){
			ParticleEmitter emitter = emitters.get(i);
			if(emitter.isDead){
				emittersToRemove.add(emitter);
				continue;
			}
			emitter.emit();
			emitter.update();
		}
		
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			p.life++;
			boolean isDead = (p.life >= p.maxLife);
			if(!isDead){
				Transform t = p.transform;
				Vector2f pos = t.getTranslation();
				float rot = t.getRotation();
				Vector2f scale = t.getScale();
				
				pos.x += p.vx;
				pos.y += p.vy;
				rot += p.av;
				scale.x += p.ss;
				scale.y += p.ss;
				
				t.setRotation(rot);
			} else {
				particlesToRemove.add(p);
			}
		}
		
		for(int i = 0; i < emittersToRemove.size(); i++){
			ParticleEmitter e = emittersToRemove.get(i);
			emitters.remove(e);
		}
		
		for(int i = 0; i < particlesToRemove.size(); i++){
			Particle p = particlesToRemove.get(i);
			particles.remove(p);
		}
	}
	
	public ParticleEmitter createEmitter(EmitterShape shape){
		ParticleEmitter emitter = new ParticleEmitter(shape, particles);
		emitters.add(emitter);
		return emitter;
	}
	
	public void removeEmitter(ParticleEmitter emitter){
		emitters.remove(emitter);
	}
	
	public void clearParticles(){
		particles.clear();
	}
	
	public int getNumParticles(){
		return particles.size();
	}
	
	public int getNumEmitters(){
		return emitters.size();
	}	
}
