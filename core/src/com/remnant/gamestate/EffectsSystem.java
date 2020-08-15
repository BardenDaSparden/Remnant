package com.remnant.gamestate;

import java.util.Random;

import org.barden.util.Timer;
import org.barden.util.TimerCallback;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.SoundClip;
import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.engine.GameSystem;
import com.remnant.graphics.Light;
import com.remnant.particles.EmissionBehavior;
import com.remnant.particles.EmitterShape;
import com.remnant.particles.Particle;
import com.remnant.particles.ParticleBehavior;
import com.remnant.particles.ParticleEmitter;
import com.remnant.particles.Particles;

public class EffectsSystem extends GameSystem {

	final int NUM_LIGHTS = 10;
	EffectLight[] lightPool = new EffectLight[NUM_LIGHTS];
	
	SoundClip rocketExplosion;
	
	public EffectsSystem(Engine game) {
		super(game);
		for(int i = 0; i < NUM_LIGHTS; i++){
			Light light = engine.lighting.createPointLight(0, 0, 512);
			lightPool[i] = new EffectLight(light, 10);
		}
		rocketExplosion = new SoundClip("resources/sounds/rocket-explosion.wav");
	}

	public boolean lightsAvailable(){
		boolean a = false;
		for(int i = 0; i < NUM_LIGHTS; i++){
			EffectLight el = lightPool[i];
			if(!el.isUsed){
				a = true;
				break;
			}
		}
		return a;
	}
	
	public EffectLight getAvailableLight(){
		EffectLight light = null;
		for(int i = 0; i < NUM_LIGHTS; i++){
			EffectLight el = lightPool[i];
			if(!el.isUsed){
				light = el;
				break;
			}
		}
		return light;
	}
	
	public EffectsSystem at(float x, float y, Vector2f normal){
		position.x = x;
		position.y = y;
		direction.set(normal);
		return this;
	}
	
	public EffectsSystem emitImpactEffect(){
		
		if(lightsAvailable()){
			EffectLight effect = getAvailableLight();
			effect.light.getPosition().x = position.x;
			effect.light.getPosition().y = position.y;
			effect.light.radius = 512;
			effect.light.castShadows = false;
			effect.setTime(7);
			effect.setColor(0.95f, 0.75f, 0.75f);
			effect.activate();
		}
		
		sparkFXEmitter.setActive(true);
		sparkFXEmitter.setPosition(position.x, position.y);
		sparkFXEmitter.emit();
		sparkFXEmitter.setActive(false);
		return this;
	}
	
	public void emitRocketExplosion(){
		
		if(lightsAvailable()){
			EffectLight effect = getAvailableLight();
			effect.light.getPosition().x = position.x + direction.x * 5;
			effect.light.getPosition().y = position.y + direction.y * 5;
			effect.light.radius = 200;
			effect.light.castShadows = false;
			effect.setTime(12);
			effect.setColor(1, 0.75f, 0.75f);
			effect.activate();
		}
		
		sparkFXEmitter.setActive(true);
		sparkFXEmitter.setPosition(position.x, position.y);
		sparkFXEmitter.emit();
		sparkFXEmitter.setActive(false);
		
		ringFXEmitter.setActive(true);
		ringFXEmitter.setPosition(position.x, position.y);
		ringFXEmitter.emit();
		ringFXEmitter.setActive(false);
		
		rocketExplosion.play();
	}
	
	public EffectsSystem emitHealthPickupEffect(){
		healthFXEmitter.setActive(true);
		healthFXEmitter.setPosition(position.x, position.y);
		healthFXEmitter.emit();
		healthFXEmitter.setActive(false);
		return this;
	}
	
	Particles particles;
	
	ParticleEmitter sparkFXEmitter;
	
	ParticleEmitter ringFXEmitter;
	
	ParticleEmitter healthFXEmitter;
	
	Random random = new Random();
	
	Vector2f position;
	Vector2f direction;
	
	@Override
	public void load() {
		particles = engine.particles;
		sparkFXEmitter = particles.createEmitter(new EmitterShape() {
			@Override
			public float getEmissionPosY() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public float getEmissionPosX() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		sparkFXEmitter.addEmissionBehavior(new EmissionBehavior() {
			float aoi = (float)Math.PI / 1.5f;
			float baseSpeed = 5;
			final int NUM_PARTICLES = 4;
			@Override
			public void emit(ParticleEmitter emitter) {
				for(int i = 0; i < NUM_PARTICLES; i++){
					Particle p = emitter.createParticle();
					float angle = (float)Math.atan2(direction.y, direction.x) + (-aoi / 2.0f + (random.nextFloat() * aoi));
					float speed = baseSpeed + (random.nextFloat() * 2);
					p.transform.setScale(1, 0.25f);
					p.transform.setRotation(angle + (float)-Math.PI / 2);
					//p.transform.setScale(scale, scale / 4);
					p.vx = (float)Math.cos(angle) * speed;
					p.vy = (float)Math.sin(angle) * speed;
					p.color.set(0.95f, 0.85f, 0.89f, 1);
					p.emissiveColor.set(p.color);
					p.useDiffuseAsEmissive = false;
					p.maxLife = 10;
					p.illum = 1;
				}
			}
		});
		sparkFXEmitter.addParticleBehavior(new ParticleBehavior() {
			@Override
			public void update(Particle particle) {
				particle.color.w = ((float)particle.maxLife - particle.life) / ((float)particle.maxLife);
				particle.vx *= 0.87f;
				particle.vy *= 0.87f;
				particle.transform.getScale().y += 0.075f;
			}
		});
		Particle spark = new Particle();
		spark.texture = new Texture("images/particles/spark.png");
		sparkFXEmitter.setParticleType(spark);
		sparkFXEmitter.setActive(false);
		
		ringFXEmitter = particles.createEmitter(new EmitterShape() {
			
			@Override
			public float getEmissionPosY() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public float getEmissionPosX() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		ringFXEmitter.addEmissionBehavior(new EmissionBehavior() {
			
			@Override
			public void emit(ParticleEmitter emitter) {
				Particle p = emitter.createParticle();
				p.maxLife = 9;
				p.ss = 0.35f;
				p.transform.setScale(0.01f, 0.01f);
				p.illum = 0.75f;
				p.useDiffuseAsEmissive = true;
				p.color.set(1, 0.75f, 0.25f, 1);
			}
		});
		
		ringFXEmitter.addParticleBehavior(new ParticleBehavior() {
			
			@Override
			public void update(Particle particle) {
				particle.color.w = ((float)particle.maxLife - particle.life) / ((float)particle.maxLife);
			}
		});
		
		Particle ringParticle = new Particle();
		ringParticle.texture = new Texture("images/particles/ring_small.png");
		ringFXEmitter.setParticleType(ringParticle);
		ringFXEmitter.setActive(false);
		
		healthFXEmitter = particles.createEmitter(new EmitterShape() {
			
			@Override
			public float getEmissionPosY() {
				return 0;
			}
			
			@Override
			public float getEmissionPosX() {
				return 0;
			}
		});
		healthFXEmitter.addEmissionBehavior(new EmissionBehavior() {
			
			final int R = 1;
			final int NUM_PARTICLES = 4;
			
			Vector2f offset = new Vector2f();
			
			@Override
			public void emit(ParticleEmitter emitter) {
				
				for(int i = 0; i < NUM_PARTICLES; i++){
					Particle p = emitter.createParticle();
					float a = ((float)i / (float)NUM_PARTICLES) * (float)Math.PI * 2.0f;// + (float)Math.PI / 6;
					offset.x = (float)Math.cos(a) * R;
					offset.y = (float)Math.sin(a) * R;
					p.transform.getTranslation().x += offset.x;
					p.transform.getTranslation().y += offset.y;
					p.transform.setRotation(a);
					p.transform.setScale(1.2f, 1.2f);
					p.vx = offset.x / 4.0f;
					p.vy = offset.y / 4.0f;
					p.maxLife = 22;
					p.color.set(1f, 0.15f, 0.15f, 1);
					p.illum = 0.0f;
					p.color.w = 1.0f;
					p.useDiffuseAsEmissive = true;
					p.ss = 0.06f;
				}
			}
		});
		
		healthFXEmitter.addParticleBehavior(new ParticleBehavior() {
			
			@Override
			public void update(Particle particle) {
				particle.color.w = ((float)particle.maxLife - particle.life) / ((float)particle.maxLife);
			}
		});
		
		Particle smokeParticle = new Particle();
		smokeParticle.texture = new Texture("images/particles/spark2.png");
		healthFXEmitter.setParticleType(smokeParticle);
		healthFXEmitter.setActive(false);
		
		position = new Vector2f();
		direction = new Vector2f();
	}

	@Override
	public void update() {
		for(int i = 0; i < NUM_LIGHTS; i++){
			EffectLight el = lightPool[i];
			el.update();
		}
	}

	@Override
	public void unload() {
		
	}

}

class EffectLight {
	
	Light light;
	Vector4f color = new Vector4f(0, 0, 0, 1);
	Timer lifeTimer;
	boolean isUsed = false;
	
	public EffectLight(final Light light, int timeAlive){
		this.light = light;
		lifeTimer = new Timer(timeAlive);
		lifeTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				lifeTimer.restart();
				lifeTimer.stop();
				light.deactivate();
				isUsed = false;
			}
		});
	}
	
	public void setTime(int time){
		lifeTimer = new Timer(time);
		lifeTimer.onComplete(new TimerCallback() {
			@Override
			public void onTimerComplete() {
				lifeTimer.restart();
				lifeTimer.stop();
				light.deactivate();
				isUsed = false;
			}
		});
	}
	
	public void setColor(float r, float g, float b){
		color.x = r;
		color.y = g;
		color.z = b;
	}
	
	public boolean isUsed(){
		return isUsed;
	}
	
	public void update(){
		lifeTimer.tick();
		float w = 1.0f - lifeTimer.percent();
		light.color.x = color.x * w;
		light.color.y = color.y * w;
		light.color.z = color.z * w;
	}
	
	public void activate(){
		lifeTimer.start();
		light.activate();
		light.color.set(color.x, color.y, color.z);

		isUsed = true;
	}
	
}