package com.remnant.particles;

import java.util.ArrayList;

import org.joml.Vector2f;

public class ParticleEmitter {
	
	protected float posX, posY;
	EmitterShape emissionShape;
	ArrayList<Particle> systemParticles;
	ArrayList<Particle> emitterParticles;
	ArrayList<ParticleBehavior> particleBehaviors;
	ArrayList<EmissionBehavior> emissionBehaviors;
	
	Particle baseParticle = null;
	
	boolean isActive = true;
	boolean isDead = false;
	
	protected ParticleEmitter(EmitterShape shape, ArrayList<Particle> allParticles){
		posX = 0;
		posY = 0;
		emissionShape = shape;
		systemParticles = allParticles;
		emitterParticles = new ArrayList<Particle>();
		particleBehaviors = new ArrayList<ParticleBehavior>();
		emissionBehaviors = new ArrayList<EmissionBehavior>();
	}
	
	public void emit(){
		
		//System.out.println("Particle Emitter: \"emit()\"");
		
		if(!isActive)
			return;
		
		for(int i = 0; i < emissionBehaviors.size(); i++){
			EmissionBehavior behaviour = emissionBehaviors.get(i);
			behaviour.emit(this);
		}
	}
	
	public void update(){
		
		//System.out.println("Particle Emitter: \"update()\"");
		
		for(int i = 0; i < emitterParticles.size(); i++){
			Particle p = emitterParticles.get(i);
			if(p == null)
				continue;
			for(int j = 0; j < particleBehaviors.size(); j++){
				ParticleBehavior pb = particleBehaviors.get(j);
				pb.update(p);
			}
		}
	}
	
	public void setPosition(float x, float y){
		posX = x;
		posY = y;
	}
	
	public void setParticleType(Particle p){
		baseParticle = p;
	}
	
	public void setActive(boolean active){
		isActive = active;
	}
	
	public void destroy(){
		isDead = true;
	}
	
	public float getEmissionPosX(){
		return posX + emissionShape.getEmissionPosX();
	}
	
	public float getEmissionPosY(){
		return posY + emissionShape.getEmissionPosY();
	}
	
	public Particle createParticle(){
		Particle p = baseParticle.create();
		Vector2f pos = p.transform.getTranslation();
		pos.x = getEmissionPosX();
		pos.y = getEmissionPosY();
		
		addParticle(p);
		
		return p;
	}
	
	protected void addParticle(Particle particle){
		emitterParticles.add(particle);
		systemParticles.add(particle);
	}
	
	public void addEmissionBehavior(EmissionBehavior eb){
		emissionBehaviors.add(eb);
	}
	
	public void addParticleBehavior(ParticleBehavior pb){
		particleBehaviors.add(pb);
	}
	
	public void clearEmissionBehaviors(){
		emissionBehaviors.clear();
	}
	
	public void clearParticleBehaviors(){
		particleBehaviors.clear();
	}
}
