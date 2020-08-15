package com.remnant.graphics;

import org.joml.Vector2f;

import com.remnant.math.Transform;

public class MeshUtil {

	public static Mesh createRectangle(float width, float height){
		Mesh mesh = new Mesh(6);
		
		Vertex v0 = mesh.get(0);
		Vertex v1 = mesh.get(1);
		Vertex v2 = mesh.get(2);
		
		Vertex v3 = mesh.get(3);
		Vertex v4 = mesh.get(4);
		Vertex v5 = mesh.get(5);
		
		float w = width / 2;
		float h = height / 2;
		
		v0.position.set(-w, -h);
		v0.texcoord.set(0, 0);
		v0.color.set(1, 1, 1, 1);
		
		v1.position.set(w, -h);
		v1.texcoord.set(1, 0);
		v1.color.set(1, 1, 1, 1);
		
		v2.position.set(w, h);
		v2.texcoord.set(1, 1);
		v2.color.set(1, 1, 1, 1);
		
		v3.position.set(-w, -h);
		v3.texcoord.set(0, 0);
		v3.color.set(1, 1, 1, 1);
		
		v4.position.set(w, h);
		v4.texcoord.set(1, 1);
		v4.color.set(1, 1, 1, 1);
		
		v5.position.set(-w, h);
		v5.texcoord.set(0, 1);
		v5.color.set(1, 1, 1, 1);
		
		return mesh;
	}
	
	public static boolean testTriangle(Transform transform, Mesh mesh, int start, int end, float x, float y){
		Vector2f translation = transform.getTranslation();
		int i;
		int j;
		boolean result = false;
		for(i = start, j = end - 1; i < (end); j = i++){
			Vector2f vi = new Vector2f(mesh.get(i).position).add(translation);
			Vector2f vj = new Vector2f(mesh.get(j).position).add(translation);
			
			if( (vi.y > y) != (vj.y > y) && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + (vi.x) ){
				result = !result;
			}	
		}
		return result;
	}
	
	public static boolean meshContains(Transform transform, Mesh mesh, float x, float y){
		boolean t1 = testTriangle(transform, mesh, 0, 3, x, y);
		boolean t2 = testTriangle(transform, mesh, 3, 6, x, y);
		return t1 || t2;
	}
	
}
