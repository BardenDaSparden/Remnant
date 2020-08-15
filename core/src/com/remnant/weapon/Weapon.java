package com.remnant.weapon;

import com.esotericsoftware.kryonet.Client;
import com.remnant.net.packet.WeaponFireRequest;

public abstract class Weapon {
	
	public static final int FIRE_TYPE_SEMI = 0;
	public static final int FIRE_TYPE_AUTO = 1;
	public static final int FIRE_TYPE_CHARGE = 2;
	
	protected Client client;
	protected int ID;
	protected int projectileID;
	protected MarkerData markers;
	protected int firingType;
	
	public Weapon(int weaponID, int projectileID){
		this.ID = weaponID;
		this.projectileID = projectileID;
		markers = new MarkerData();
		firingType = FIRE_TYPE_SEMI;
	}
	
	public abstract void update();
	public abstract boolean canFire();
	public abstract void fire(int playerID, boolean isPrimary);
	
	protected void net_fire(int playerID, boolean isPrimary){
		WeaponFireRequest request = new WeaponFireRequest();
		request.playerID = playerID;
		request.isPrimary = isPrimary;
		client.sendTCP(request);
	}
	
	public void setClient(Client client){
		this.client = client;
	}
	
	public int getID(){
		return ID;
	}
	
	public int getProjectileID(){
		return projectileID;
	}
	
	public MarkerData getMarkerData(){
		return markers;
	}
	
	public int getFiringType(){
		return firingType;
	}
	
}