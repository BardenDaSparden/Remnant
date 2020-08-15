package com.remnant.physics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.remnant.geometry.Circle;
import com.remnant.geometry.Polygon;
import com.remnant.geometry.Shape;
import com.remnant.geometry.Shape.ShapeType;
import com.remnant.math.Transform;

public class PhysicsBody {

	boolean isSensor;
	boolean isStatic;
	boolean isEnabled = true;
	List<ContactListener> contactListeners;
	Shape shape;
	Transform previous;
	Transform transform;
	
	protected float mass;
	protected float invMass;
	
	protected float angularMass;
	protected float invAngularMass;
	
	protected Vector2f velocity;
	protected float angularVelocity;
	
	protected Vector2f netForce;
	protected float netTorque;
	
	protected float staticFriction;
	protected float kineticFriction;
	
	protected float e;
	
	//TODO temp
	Vector2f velocityDamping;
	float angularVelocityDamping;
	
	protected CollisionInfo previousCollisionInfo;
	protected CollisionInfo collisionInfo;
	
	boolean processStartContact = true;
	boolean processEndContact = false;
	/*
	 * ========================
	 *    bStart   |   bEnd   |
	 * ------------------------
	 *     true    |   false  | -> allow contact start 
	 * ------------------------
	 *     false   |   true   | -> allow contact end
	 * ------------------------
	 * */
	public PhysicsBody(Shape collisionBody){
		isSensor = false;
		isStatic = false;
		contactListeners = new ArrayList<ContactListener>();
		shape = collisionBody;
		previous = new Transform();
		transform = new Transform();
		
		mass = shape.getArea();
		invMass = 1.0f / mass;
		if(shape.getType() == ShapeType.POLYGON){
			Polygon polygon = (Polygon) shape;
			ArrayList<Vector2f> vertices = polygon.getVertices();
			for(int i = 0; i < vertices.size(); i++){
				
			}
			//Rectangle rect = (Rectangle) shape;
			//angularMass = (mass * (rect.width * rect.width + rect.height * rect.height)) / 12.0f;
			//TODO add polygonal angular mass calculation
		} else {
			Circle circle = (Circle) shape;
			angularMass = (mass * (circle.radius * circle.radius)) / 4.0f;
		}
		invAngularMass = 1.0f / angularMass;
		
		velocity = new Vector2f(0, 0);
		angularVelocity = 0;
		
		netForce = new Vector2f();
		netTorque = 0;
		
		e = 1;
		
		velocityDamping = new Vector2f(1, 1);
		angularVelocityDamping = 1;
		
		previousCollisionInfo = new CollisionInfo();
		collisionInfo = new CollisionInfo();
	}
	
	protected void applyImpulse(Vector2f impulse, Vector2f contact){
		netForce.add(impulse);
	}
	
	public void applyForceToCenter(Vector2f f){
		netForce.add(f);
	}
	
	public void applyForce(Vector2f f, Vector2f contact){
		netForce.add(f);
	}
	
	public void applyTorque(float t){
		netTorque += t;
	}
	
	protected void integrate(float dt){
		previous.set(transform);
		
		float invMass = getInvMass();
		
		
		
		velocity.x += netForce.x * invMass;
		velocity.y += netForce.y * invMass;
		angularVelocity += netTorque * getInvAngularMass();
		
		transform.translate(velocity.x * dt, velocity.y * dt);
		transform.rotate(angularVelocity * dt);
		
		//Clamp Velocity and Angular Velocity at low values to 0
		if((float)Math.abs(angularVelocity) < 0.01f)
			angularVelocity = 0;
		if((float)Math.abs(velocity.x) < 0.01f)
			velocity.x = 0;
		if((float)Math.abs(velocity.y) < 0.01f)
			velocity.y = 0;
		
		//Velocity damping to simulate the effect of friction-like interactions
		velocity.x *= velocityDamping.x;
		velocity.y *= velocityDamping.y;
		angularVelocity *= angularVelocityDamping;
		
		netForce.set(0, 0);
		netTorque = 0;
		
	}
	
	protected void onContactBegin(ContactEvent e){
		if(!processStartContact)
			return;
		
		for(int i = 0; i < contactListeners.size(); i++){
			ContactListener listener = contactListeners.get(i);
			listener.onContactBegin(e);
		}
		
		processStartContact = false;
		processEndContact = true;
	}
	
	protected void onContactEnd(ContactEvent e){
		if(!processEndContact)
			return;
		
		for(int i = 0; i < contactListeners.size(); i++){
			ContactListener listener = contactListeners.get(i);
			listener.onContactEnd(e);
		}
		
		processEndContact = false;
		processStartContact = true;
	}
	
	protected void onRayContact(RayContactEvent e){
		for(int i = 0; i < contactListeners.size(); i++){
			ContactListener listener = contactListeners.get(i);
			listener.onRayContact(e);
		}
	}
	
	public void setCollisionInfo(CollisionInfo info){
		previousCollisionInfo.collision = collisionInfo.collision;
		previousCollisionInfo.normal.set(collisionInfo.normal.x, collisionInfo.normal.y);
		previousCollisionInfo.contact.set(collisionInfo.contact.x, collisionInfo.contact.y);
		previousCollisionInfo.depth = collisionInfo.depth;
		
		collisionInfo.collision = info.collision;
		collisionInfo.normal.set(info.normal.x, info.normal.y);
		collisionInfo.contact.set(info.contact.x, info.contact.y);
		collisionInfo.depth = info.depth;
	}
	
	public void setPosition(float x, float y){
		Vector2f pos = transform.getTranslation();
		pos.set(x, y);
	}
	
	public void setRotation(float r){
		transform.setRotation(r);
	}
	
	public void setVelocity(float vx, float vy){
		velocity.x = vx;
		velocity.y = vy;
	}
	
	public void setAngularVelocity(float av){
		angularVelocity = av;
	}
	
	public void setRestitution(float v){
		this.e = v;
	}
	
	public void setFriction(float staticF, float kineticF){
		this.staticFriction = staticF;
		this.kineticFriction = kineticF;
	}
	
	public void setVelocityDamping(Vector2f damping){
		this.velocityDamping = damping;
	}
	
	public void setAngularVelocityDamping(float damping){
		this.angularVelocityDamping = damping;
	}
	
	public Vector2f getVelocity(){
		return velocity;
	}
	
	public float getMass(){
		return mass;
	}
	
	public float getInvMass(){
		return invMass;
	}
	
	public float getAngularMass(){
		return angularMass;
	}
	
	public float getInvAngularMass(){
		return invAngularMass;
	}
	
	public boolean isSensor(){
		return isSensor;
	}
	
	public void setSensor(boolean b){
		isSensor = b;
	}
	
	public void setStatic(boolean b){
		isStatic = b;
		mass = 0;
		invMass = 0;
		angularMass = 0;
		invAngularMass = 0;
	}
	
	public void setEnable(boolean v){
		isEnabled = v;
	}
	
	public boolean isEnabled(){
		return isEnabled;
	}
	
	public Transform getPreviousTransform(){
		return previous;
	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public Shape getShape(){
		return shape;
	}
	
	public void addListener(ContactListener listener){
		contactListeners.add(listener);
	}
	
	public void removeListener(ContactListener listener){
		contactListeners.remove(listener);
	}
	
}
