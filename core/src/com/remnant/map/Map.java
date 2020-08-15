package com.remnant.map;

import java.util.ArrayList;

import org.joml.Vector3f;

import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.geometry.AABB;
import com.remnant.geometry.AABBUtil;
import com.remnant.graphics.Light;
import com.remnant.graphics.MeshUtil;

public class Map {
	
	Engine engine;
	protected String name;
	boolean isDirty = false;
	boolean isOpened = false;
	
	ArrayList<Terrain> terrainObjects;
	ArrayList<Light> lights;
	ArrayList<Collider> colliders;
	ArrayList<Pickup> pickups;
	
	public Map(Engine engine, String mapName){
		this.engine = engine;
		name = mapName;
		terrainObjects = new ArrayList<Terrain>();
		colliders = new ArrayList<Collider>();
		lights = new ArrayList<Light>();
		pickups = new ArrayList<Pickup>();
	}
	
	public void open(){
		isOpened = true;
	}
	
	public void close(){
		isOpened = false;
		unload();
	}
	
	public boolean isOpen(){
		return isOpened;
	}
	
	public void update(){
		
	}
	
	public void createTerrainAt(float x, float y, TerrainDefinition properties){
		Terrain terrain = new Terrain(engine);
		terrain.transform.setTranslation(x, y);
		terrain.transform.setRotation(properties.orientation);
		terrain.mesh = MeshUtil.createRectangle(properties.size, properties.size);
		terrain.material.setDiffuse(new Texture(properties.diffusePath));
		terrain.material.setNormal(new Texture(properties.normalPath));
		terrain.material.setEmissive(new Texture(properties.emissivePath));
		terrain.size = properties.size;
		terrain.type = properties.type;
		terrainObjects.add(terrain);
		engine.add(terrain);
		isDirty = true;
	}
	
	public Terrain pickTerrainAt(float x, float y){
		Terrain picked = null;
		for(int i = terrainObjects.size() - 1; i > -1; i--){
			Terrain terrain = terrainObjects.get(i);
			if(MeshUtil.meshContains(terrain.transform, terrain.mesh, x, y)){
				picked = terrain;
				break;
			}
		}
		return picked;
	}
	
	public void removeTerrainAt(float x, float y){
		Terrain picked = pickTerrainAt(x, y);
		if(picked != null){
			removeTerrain(picked);
		}
	}
	
	public void removeTerrain(Terrain obj){
		terrainObjects.remove(obj);
		engine.remove(obj);
		isDirty = true;
	}
	
	public void createLightAt(float x, float y, LightDefinition properties){
		Light light = engine.lighting.createPointLight(x, y, properties.radius);
		light.color.set(properties.color.x, properties.color.y, properties.color.z);
		light.castShadows = properties.castShadows;
		light.getPosition().z = properties.z;
		lights.add(light);
		isDirty = true;
	}
	
	public Light pickLightAt(float x, float y){
		Light closest = null;
		float d = Float.MAX_VALUE;
		for(int i = lights.size() - 1; i > -1; i--){
			Light light = lights.get(i);
			Vector3f lightPos = light.getPosition();
			float dx = x - lightPos.x;
			float dy = y - lightPos.y;
			float dtemp = (dx * dx) + (dy * dy);
			if(dtemp < d){
				closest = light;
				d = dtemp;
			}
		}
		return closest;
	}
	
	public void removeLightAt(float x, float y){
		Light picked = pickLightAt(x, y);
		if(picked != null){
			float dx = (x - picked.getPosition().x);
			float dy = (y - picked.getPosition().y);
			if(picked.radius * picked.radius > (dx * dx) + (dy * dy)){
				removeLight(picked);
			}
		}
	}
	
	public void removeLight(Light light){
		engine.lighting.remove(light);
		lights.remove(light);
		isDirty = true;
	}
	
	public void createColliderAt(float x, float y, ColliderDefinition properties){
		Collider collider = new Collider(engine, properties);
		collider.body.getTransform().setTranslation(x, y);
		colliders.add(collider);
		engine.add(collider);
		engine.physics.add(collider);
		isDirty = true;
	}
	
	public Collider pickColliderAt(float x, float y){
		Collider picked = null;
		AABB bounds = new AABB(0, 0);
		for(int i = colliders.size() - 1; i > -1; i--){
			Collider collider = colliders.get(i);
			AABBUtil.getAABB(collider.body, bounds);
			if(bounds.contains(x, y)){
				picked = collider;
				break;
			}
		}
		return picked;
	}
	
	public void removeColliderAt(float x, float y){
		Collider picked = pickColliderAt(x, y);
		if(picked != null){
			removeCollider(picked);
		}
	}
	
	public void removeCollider(Collider collider){
		engine.remove(collider);
		engine.physics.remove(collider);
		colliders.remove(collider);
		isDirty = true;
	}
	
	public void createPickupAt(float x, float y, PickupDefinition properties){
		Pickup pickup = null;
		if(properties.type.equals(PickupDefinition.HEALTH_TYPE)){
			pickup = new HealthPickup(engine, properties.respawnTime);
			pickup.setPosition(x, y);
			((HealthPickup) pickup).setHealthAmount(properties.value);
		} else if(properties.type.equals(PickupDefinition.WEAPON_TYPE)){
			pickup = new WeaponPickup(engine, properties.respawnTime);
			pickup.setPosition(x, y);
			((WeaponPickup) pickup).setWeaponID(properties.value);
		}
		engine.add(pickup);
		engine.physics.add(pickup);
		pickups.add(pickup);
		isDirty = true;
	}
	
	public Pickup pickPickupAt(float x, float y){
		Pickup picked = null;
		AABB bounds = new AABB(0, 0);
		for(int i = pickups.size() - 1; i > -1; i--){
			Pickup pickup = pickups.get(i);
			AABBUtil.getAABB(pickup.body, bounds);
			if(bounds.contains(x, y)){
				picked = pickup;
				break;
			}
		}
		return picked;
	}
	
	public void removePickupAt(float x, float y){
		Pickup picked = pickPickupAt(x, y);
		if(picked != null){
			removePickup(picked);
		}
	}
	
	public void removePickup(Pickup pickup){
		engine.remove(pickup);
		engine.physics.remove(pickup);
		engine.lighting.remove(pickup.light);
		pickups.remove(pickup);
		isDirty = true;
	}
	
	public void unload(){
		
		//Remove object references from engine
		for(int i = 0; i < terrainObjects.size(); i++)
			engine.remove(terrainObjects.get(i));
		for(int i = 0; i < colliders.size(); i++)
			engine.remove(colliders.get(i));
		for(int i = 0; i < pickups.size(); i++)
			engine.remove(pickups.get(i));
		engine.lighting.clear();
		engine.physics.clear();
		
		
		//Remove object references from map
		lights.clear();
		terrainObjects.clear();
		colliders.clear();
		pickups.clear();
	}
	
	public boolean isDirty(){
		return isDirty;
	}
	
	public void setDirty(boolean b){
		isDirty = b;
	}
	
	public String getName(){
		return name;
	}
	
}
