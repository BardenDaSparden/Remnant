package com.remnant.player;

import org.joml.Vector2f;

import com.esotericsoftware.kryonet.Client;
import com.remnant.engine.Engine;
import com.remnant.engine.GameObject;
import com.remnant.gamestate.BaseMPState;
import com.remnant.gamestate.GameState;
import com.remnant.geometry.Circle;
import com.remnant.physics.ContactEvent;
import com.remnant.physics.ContactListener;
import com.remnant.physics.Masks;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;
import com.remnant.physics.RayContactEvent;
import com.remnant.weapon.Weapon;
import com.remnant.weapon.WeaponFactory;
import com.remnant.weapon.WeaponID;
import com.remnant.weapon.projectile.DamageArea;

public class Player extends GameObject implements PhysicsObject {
	
	final int PICKUP_RANGE = 20;
	
	int ID = 0;
	boolean isSelf;
	
	Client client;
	String name;
	
	PhysicsBody body;
	
	Weapon primaryWeapon;
	Weapon secondaryWeapon;
	
	//Shield shield;
	
	PickupCollider pickupCollider;
	
	public int health = 10;
	public int maxHealth = 100;
	
	public Player(Engine engine, boolean isSelf){
		super(engine);
		
		this.isSelf = isSelf;
		this.client = engine.client;
		
		body = new PhysicsBody(new Circle(16));
		body.setRestitution(0.25f);
		body.setVelocityDamping(new Vector2f(0.85f, 0.85f));
		body.addListener(new ContactListener() {
			
			@Override
			public void onContactBegin(ContactEvent e) {
				long groupMask = e.other.getGroupMask();
				if((groupMask & Masks.DAMAGE) == Masks.DAMAGE){
					DamageArea area = (DamageArea) e.other;
					if(!area.isExpired){
						health -= area.amount;
						area.isExpired = true;
					}
				}
			}

			@Override
			public void onContactEnd(ContactEvent e) { }

			@Override
			public void onRayContact(RayContactEvent e) { }
			
		});
		
		pickupCollider = new PickupCollider(PICKUP_RANGE);
		pickupCollider.setPlayer(this);
		
		GameState gs = engine.getGameState();
		BaseMPState mps = null;
		if(gs instanceof BaseMPState)
			mps = (BaseMPState) gs;
		
		primaryWeapon = WeaponFactory.createWeapon(client, WeaponID.PISTOL);
		secondaryWeapon = WeaponFactory.createWeapon(client, WeaponID.COMBAT_RIFLE);
		//shield = new Shield(mps.effectsSystem, 30, isSelf);
		
	}
	
	public void setPrimaryWeapon(int weaponID){
		primaryWeapon = WeaponFactory.createWeapon(client, weaponID);
	}
	
	public void setPosition(float x, float y){
		body.getTransform().getTranslation().set(x, y);
		//shield.setPosition(x, y);
		pickupCollider.setPosition(x, y);
	}
	
	public void load(){
		//engine.physics.add(shield);
		engine.physics.add(pickupCollider);
	}
	
	public void unload(){
		//engine.physics.remove(shield);
		engine.physics.remove(pickupCollider);
	}
	
	@Override
	public void onExpire(){}
	
	@Override
	public void update(){
		primaryWeapon.update();
		secondaryWeapon.update();
		pickupCollider.setPosition(body.getTransform().getTranslation().x, body.getTransform().getTranslation().y);
		//shield.setPosition(body.getTransform().getTranslation());
		//shield.update();
	}
	
	public void addHealth(int amt){
		this.health += amt;
		if(health > maxHealth)
			health = maxHealth;
	}
	
	public boolean fullHealth(){
		return health >= maxHealth;
	}
	
	@Override
	public long getGroupMask() {
		return Masks.PLAYER;
	}
	
	@Override
	public long getCollisionMask() {
		return Masks.WALL | Masks.PLAYER;
	}
	
	@Override
	public PhysicsBody getBody() {
		return body;
	}
	
	public void setID(int id){
		this.ID = id;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return name;
	}
	
	public float getX(){
		return body.getTransform().getTranslation().x;
	}
	
	public float getY(){
		return body.getTransform().getTranslation().y;
	}
	
	public Weapon getPrimaryWeapon(){
		return primaryWeapon;
	}
	
	public Weapon getSecondaryWeapon(){
		return secondaryWeapon;
	}
}