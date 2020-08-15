package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class BaseRenderer {

	static final int POSITION_SIZE = 2;
	static final int TEXCOORD_SIZE = 2;
	static final int COLOR_SIZE = 4;
	
	Vector2f[] positions;
	Vector2f[] texcoords;
	Vector4f[] colors;
	
	FloatBuffer positionBuffer;
	FloatBuffer texcoordBuffer;
	FloatBuffer colorBuffer;
	
	final int MAX_VERTICES;
	int currentVertex = 0;
	
	int positionVBO = 0;
	int texcoordVBO = 0;
	int colorVBO = 0;
	int vao = 0;
	
	public BaseRenderer(int numVertices){
		MAX_VERTICES = numVertices;
		
		positions = new Vector2f[MAX_VERTICES];
		texcoords = new Vector2f[MAX_VERTICES];
		colors = new Vector4f[MAX_VERTICES];
		
		for(int i = 0; i < MAX_VERTICES; i++){
			positions[i] = new Vector2f();
			texcoords[i] = new Vector2f();
			colors[i] = new Vector4f();
		}
		
		positionBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * POSITION_SIZE);
		texcoordBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * TEXCOORD_SIZE);
		colorBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * COLOR_SIZE);
		
		positionVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
		glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_DYNAMIC_DRAW);
		
		texcoordVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texcoordVBO);
		glBufferData(GL_ARRAY_BUFFER, texcoordBuffer, GL_DYNAMIC_DRAW);
		
		colorVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_DYNAMIC_DRAW);
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		{
			glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
			glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(0);
			
			glBindBuffer(GL_ARRAY_BUFFER, texcoordVBO);
			glVertexAttribPointer(1, TEXCOORD_SIZE, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(1);
			
			glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
			glVertexAttribPointer(2, COLOR_SIZE, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(2);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		glBindVertexArray(0);
	}
	
	public void draw(Vertex vertex){
		draw(vertex.position, vertex.texcoord, vertex.color);
	}
	
	public void draw(Vector2f position, Vector2f texcoord, Vector4f color){
		draw(position.x, position.y, texcoord.x, texcoord.y, color.x, color.y, color.z, color.w);
	}
	
	public void draw(float vx, float vy, float tu, float tv, float cr, float cg, float cb, float ca){
		//If weve reached batch capacity, send data to GL
		if(!(currentVertex + 1 < MAX_VERTICES))
			flushToGL();
		
		positions[currentVertex].set(vx, vy);
		texcoords[currentVertex].set(tu, tv);
		colors[currentVertex].set(cr, cg, cb, ca);
		++currentVertex;
	}
	
	public void flushToGL(){
		
		for(int i = 0; i < currentVertex; i++){
			Vector2f p = positions[i];
			Vector2f t = texcoords[i];
			Vector4f c = colors[i];
			positionBuffer.put(p.x).put(p.y);
			texcoordBuffer.put(t.x).put(t.y);
			colorBuffer.put(c.x).put(c.y).put(c.z).put(c.w);
		}
		
		positionBuffer.flip();
		texcoordBuffer.flip();
		colorBuffer.flip();
		
		//Update VBO's with new buffer data
		glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, positionBuffer);
		glBindBuffer(GL_ARRAY_BUFFER, texcoordVBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, texcoordBuffer);
		glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
		glBufferSubData(GL_ARRAY_BUFFER, 0, colorBuffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, currentVertex);
		
		currentVertex = 0;
		positionBuffer.clear();
		texcoordBuffer.clear();
		colorBuffer.clear();
	}
	
	public void destroy(){
		positionBuffer.clear();
		texcoordBuffer.clear();
		colorBuffer.clear();
		glDeleteVertexArrays(vao);
		glDeleteBuffers(positionVBO);
		glDeleteBuffers(texcoordVBO);
		glDeleteBuffers(colorVBO);
	}
	
}
