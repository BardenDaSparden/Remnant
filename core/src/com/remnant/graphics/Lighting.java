package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_NEVER;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearStencil;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;

import java.util.ArrayList;
import java.util.List;

import org.barden.util.GPUProfiler;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.geometry.AABB;
import com.remnant.geometry.AABBUtil;
import com.remnant.geometry.Circle;
import com.remnant.geometry.Edge;
import com.remnant.geometry.Polygon;
import com.remnant.geometry.PolygonUtil;
import com.remnant.geometry.Shape;
import com.remnant.math.Color;
import com.remnant.math.Transform;
import com.remnant.physics.Masks;
import com.remnant.physics.Physics;
import com.remnant.physics.PhysicsBody;
import com.remnant.physics.PhysicsObject;
import com.remnant.shaders.PointLightShader;
import com.remnant.shaders.SpotLightShader;

public class Lighting {

	static final float SHADOW_CAST_DISTANCE = 100000;
	static final int MAX_SHADOW_CASTS = 10000;
	
	Physics physics;
	
	Vector3f ambientLight;
	
	ArrayList<Light> lights;	
	int castIdx = 0;
	ShadowCastData[] shadowCasts;
	
	Texture white;
	Shader pointlightShader;
	Shader spotlightShader;
	
	public Lighting(){
		ambientLight = new Vector3f(0.02f, 0.02f, 0.02f);
		lights = new ArrayList<Light>();
		shadowCasts = new ShadowCastData[MAX_SHADOW_CASTS];
		for(int i = 0; i < MAX_SHADOW_CASTS; i++){
			shadowCasts[i] = new ShadowCastData();
		}
		
		white = new Texture("images/white.png");
		pointlightShader = PointLightShader.get();
		spotlightShader = SpotLightShader.get();
	}
	
	public void setPhysics(Physics physics){
		this.physics = physics;
	}
	
	List<PhysicsBody> objectsToCast = new ArrayList<PhysicsBody>();
	AABB objectAABB = new AABB(0, 0);
	
	void castShadows(Light light){
		List<PhysicsObject> objects = physics.getObjects();
		
		AABB lightBounds = new AABB(0, 0);
		AABB objectBounds = new AABB(0, 0);
		
		PhysicsBody lightBody = new PhysicsBody(new Circle((int)light.radius));
		lightBody.setPosition(light.position.x, light.position.y);
		
		AABBUtil.getAABB(lightBody, lightBounds);
		
		castIdx = 0;
		objectsToCast.clear();
		for(int i = 0; i < objects.size(); i++){
			PhysicsObject object = objects.get(i);
			PhysicsBody body = object.getBody();
			if((object.getGroupMask() & Masks.WALL) != Masks.WALL)
				continue;
			
			if(body.isSensor())
				return;
			
			AABBUtil.getAABB(body, objectBounds);
			if(objectBounds.intersects(lightBounds))
				objectsToCast.add(body);
		}
		
		for(int i = 0; i < objectsToCast.size(); i++){
			PhysicsBody body = objectsToCast.get(i);
			castBodyShadow(light, body);
		}
		
	}
	
	Vector2f lightPos = new Vector2f();
	Vector2f L = new Vector2f(0, 0);
	List<Edge> edges = new ArrayList<Edge>();
	void castBodyShadow(Light light, PhysicsBody body){
		lightPos.x = light.position.x;
		lightPos.y = light.position.y;
		L.x = body.getTransform().getTranslation().x - light.position.x;
		L.y = body.getTransform().getTranslation().y - light.position.y;
		L.normalize();
		if(!(body.getShape() instanceof Polygon))
			return;
		Transform transform = body.getTransform();
		Polygon polygon = (Polygon) body.getShape();
		ArrayList<Vector2f> verts = polygon.getVertices(); 
		
		PolygonUtil.transformPolygon(verts, transform);
			edges.clear();
			for(int i = 0; i < verts.size(); i++){
				Vector2f v0 = new Vector2f(verts.get(i));
				Vector2f v1 = new Vector2f(verts.get((i + 1) % verts.size()));
				edges.add(new Edge(v0, v1));
			}
		PolygonUtil.inverseTransformPolygon(verts, transform);
		
		for(int i = 0; i < edges.size(); i++){
			Edge edge = edges.get(i);
			castEdge(light, edge, lightPos);
		}
	}

	Vector2f startDir = new Vector2f();
	Vector2f midDir = new Vector2f();
	Vector2f endDir = new Vector2f();
	Vector2f startCast = new Vector2f();
	Vector2f midCast = new Vector2f();
	Vector2f endCast = new Vector2f();
	Vector2f start = new Vector2f();
	Vector2f mid = new Vector2f();
	Vector2f end = new Vector2f();
	
	void castEdge(Light light, Edge edge, Vector2f origin){
		ShadowCastData data = shadowCasts[castIdx];
		
		startDir.x = edge.start.x - origin.x;
		startDir.y = edge.start.y - origin.y;
		startDir.normalize();
		
		endDir.x = edge.end.x - origin.x;
		endDir.y = edge.end.y - origin.y;
		endDir.normalize();
		
		mid.x = edge.start.x + (edge.end.x - edge.start.x) / 2.0f;
		mid.y = edge.start.y + (edge.end.y - edge.start.y) / 2.0f;
		
		midDir.x = mid.x - origin.x;
		midDir.y = mid.y - origin.y;
		midDir.normalize();
		
		startCast.x = origin.x + startDir.x * SHADOW_CAST_DISTANCE;
		startCast.y = origin.y + startDir.y * SHADOW_CAST_DISTANCE;
		
		midCast.x = origin.x + midDir.x * SHADOW_CAST_DISTANCE;
		midCast.y = origin.y + midDir.y * SHADOW_CAST_DISTANCE;
		
		endCast.x = origin.x + endDir.x * SHADOW_CAST_DISTANCE;
		endCast.y = origin.y + endDir.y * SHADOW_CAST_DISTANCE;
		
//		data.vertices[0].set(edge.start);
//		data.vertices[1].set(edge.end);
//		data.vertices[2].set(startCast);
//		data.vertices[3].set(startCast);
//		data.vertices[4].set(edge.end);
//		data.vertices[5].set(endCast);
		
		data.vertices[0].set(edge.start);
		data.vertices[1].set(mid);
		data.vertices[2].set(startCast);
		
		data.vertices[3].set(mid);
		data.vertices[4].set(midCast);
		data.vertices[5].set(startCast);
		
		data.vertices[6].set(mid);
		data.vertices[7].set(endCast);
		data.vertices[8].set(midCast);
		
		data.vertices[9].set(edge.end);
		data.vertices[10].set(endCast);
		data.vertices[11].set(mid);
		
		castIdx++;
	}
	
	//TODO cleanup this method
	public void drawLights(TextureBatch batch, CameraStack stack, Framebuffer lightBuffer, Texture normalBuffer, Texture positionBuffer){
		glEnable(GL_STENCIL_TEST);
		
		Vector2f texcoord = new Vector2f(0, 0);
		Vector4f color = new Vector4f(1, 1, 1, 1);
		Vector3f cameraPos = new Vector3f();
		float cameraRot = 0;
		Camera current = stack.current();
		cameraPos.x = 0;//current.getTranslation().x;
		cameraPos.y = 0;//current.getTranslation().y;// - (current.getHeight() / 2) + 150 );
		cameraPos.z = 1000;
		cameraRot = -current.getRotation();
		
		lightBuffer.bind();
		lightBuffer.clear(ambientLight.x, ambientLight.y, ambientLight.z, 1);
			for(int i = 0; i < lights.size(); i++){
				Light light = lights.get(i);
				if(!light.isActive)
					continue;
				
				
				glClearStencil(0);
				glClear(GL_STENCIL_BUFFER_BIT);
				
				//Disable Color and Depth writing
				glColorMask(false, false, false, false);
				glDepthMask(false);
				
				if(light.castShadows){
					
					castShadows(light);
					
					
					//System.out.println("Num Casts: " + castIdx);
					
//					//Force all fragments drawn to fail stencil test, and increment stencil buffer ONLY on stencil test failure
//					//All fragments drawn here will be drawn as the inverse stencil mask
					glStencilFunc(GL_NEVER, 1, 0);
					glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);					
					batch.begin(BlendState.ALPHA_TRANSPARENCY);
						for(int j = 0; j < castIdx; j++){
							ShadowCastData data = shadowCasts[j];
							//UPDATE SHADOW CAST DATA
							batch.draw(data.vertices[0], texcoord, color);
							batch.draw(data.vertices[1], texcoord, color);
							batch.draw(data.vertices[2], texcoord, color);
							batch.draw(data.vertices[3], texcoord, color);
							batch.draw(data.vertices[4], texcoord, color);
							batch.draw(data.vertices[5], texcoord, color);
							batch.draw(data.vertices[6], texcoord, color);
							batch.draw(data.vertices[7], texcoord, color);
							batch.draw(data.vertices[8], texcoord, color);
							batch.draw(data.vertices[9], texcoord, color);
							batch.draw(data.vertices[10], texcoord, color);
							batch.draw(data.vertices[11], texcoord, color);
						}
					batch.end();

					glStencilFunc(GL_ALWAYS, 0, 0);
					glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
					batch.begin(BlendState.ALPHA_TRANSPARENCY);
						for(int j = 0; j < objectsToCast.size(); j++){
							PhysicsBody body = objectsToCast.get(j);
							Shape s = body.getShape();
							if(s instanceof Polygon){
								AABBUtil.getAABB(body, objectAABB);
								
								batch.draw(body.getTransform().getTranslation().x, body.getTransform().getTranslation().y, objectAABB.width, objectAABB.height, 0, 1, 1, white, Color.WHITE);
							}
						}
					batch.end();
				}
				
				//Enable Color and Depth writing
				glColorMask(true, true, true, true);
				glDepthMask(true);
				
				glStencilFunc(GL_NOTEQUAL, 1, 1);
				glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
				
				if(light instanceof SpotLight){
					batch.setShader(spotlightShader);
					
					SpotLight spotlight = (SpotLight) light;
					spotlightShader.bind();
					spotlightShader.setTexture("normals", normalBuffer, 1);
					spotlightShader.setVector("light.position", light.position);
					spotlightShader.setVector("light.color", light.color);
					spotlightShader.setVector("light.direction", spotlight.direction);
					spotlightShader.setFloat("light.radius", light.radius);
					spotlightShader.setFloat("light.cutoff", spotlight.cosCutoff);
					spotlightShader.release();
					
					stack.push();
					Camera camera = stack.current();
					batch.begin(BlendState.ADDITIVE);
					batch.draw(0, 0, camera.getWidth(), camera.getHeight(), 0, 1, 1, positionBuffer, Color.WHITE);
					batch.end();
					stack.pop();
				} else if(light instanceof TextureLight){
					TextureLight texLight = (TextureLight) light;
					
					batch.setShader(null);
					batch.begin(BlendState.ADDITIVE);
						batch.draw(light.position.x, light.position.y, texLight.getTexture().getWidth(), texLight.getTexture().getHeight(), 0, 1, 1, texLight.getTexture(), Color.WHITE);
					batch.end();
				} else {
					batch.setShader(pointlightShader);
					
					pointlightShader.bind();
					pointlightShader.setTexture("normals", normalBuffer, 1);
					pointlightShader.setVector("cameraPos", cameraPos);
					pointlightShader.setFloat("cameraRot", cameraRot);
					pointlightShader.setVector("light.position", light.position);
					pointlightShader.setVector("light.color", light.color);
					pointlightShader.setFloat("light.radius", light.radius);
					pointlightShader.release();
					
					stack.push();
					Camera camera = stack.current();
					batch.begin(BlendState.ADDITIVE);
					batch.draw(0, 0, camera.getWidth(), camera.getHeight(), 0, 1, 1, positionBuffer, Color.WHITE);
					batch.end();
					stack.pop();
				}
			}
			glClear(GL_STENCIL_BUFFER_BIT);
		lightBuffer.release();
		glDisable(GL_STENCIL_TEST);
	}

	public Light createPointLight(float x, float y, float radius){
		Light light = new Light(x, y, radius);
		light.getPosition().z = 40;
		lights.add(light);
		return light;
	}
	
	public SpotLight createSpotLight(float x, float y, float radius){
		SpotLight light = new SpotLight(x, y, radius, (float)Math.PI / 8.0f);
		lights.add(light);
		return light;
	}
	
	public TextureLight createTextureLight(float x, float y, Texture texture){
		TextureLight light = new TextureLight(x, y, texture);
		lights.add(light);
		return light;
	}
	
	public void remove(Light light){
		lights.remove(light);
	}
	
	public int getNumLights(){
		return lights.size();
	}
	
	public void clear(){
		lights.clear();
	}
	
}

class ShadowCastData {
	
	final int SIZE = 12;
	Vector2f[] vertices = new Vector2f[SIZE];
	
	public ShadowCastData(){
		for(int i = 0; i < SIZE; i++){
			vertices[i] = new Vector2f();
		}
	}
}