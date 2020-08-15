package com.remnant.graphics;

import com.remnant.ui.EditorAttribute;

@EditorAttribute(name = "Mesh")
public class Mesh {

	Vertex[] vertices;
	
	public Mesh(int numVertices){
		vertices = new Vertex[numVertices];
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = new Vertex();
		}
	}
	
	public Vertex get(int vertexIdx){
		return vertices[vertexIdx];
	}
	
	public int getNumVertices(){
		return vertices.length;
	}
	
}