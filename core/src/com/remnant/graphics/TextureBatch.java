package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.math.Color;
import com.remnant.math.Transform;
import com.remnant.shaders.PassthroughShader;

public class TextureBatch {
	
	BaseRenderer renderer;
	CameraStack stack;
	Texture activeTexture;
	PassthroughShader passthroughShader;
	Shader shader;
	Matrix4f projection;
	Matrix4f view;
	int batchCount = 0;
	
	public TextureBatch(CameraStack cameras, int numVertices){
		this.stack = cameras;
		renderer = new BaseRenderer(numVertices);
		activeTexture = new Texture("images/white.png");
		passthroughShader = PassthroughShader.get();
		shader = passthroughShader;
	}
	
	public void begin(BlendState state){
		glEnable(GL_BLEND);
		glBlendFunc(state.source, state.destination);
	}
	
	public void end(){
		flush();
		glDisable(GL_BLEND);
	}
	
	public void draw(float x, float y, float width, float height, float rot, float scaleX, float scaleY, Texture texture, Vector4f color){
		draw(x, y, width, height, rot, scaleX, scaleY, new TextureRegion(texture), color);
	}
	
	public void draw(float x, float y, float width, float height, float rot, float scaleX, float scaleY, TextureRegion texture, Vector4f color){
		if(!(texture.getBase().getHandle() == activeTexture.getHandle())){
			flush();
			activeTexture = texture.getBase();
		}
		
		float[] tcs = texture.getTexCoords();
		
		float w = (width * scaleX) / 2f;
		float h = (height * scaleY) / 2f;
		float c = (float)Math.cos(rot);
		float s = (float)Math.sin(rot);
		
		float x1 = x + (c * -w - s * -h);
		float y1 = y + (s * -w + c * -h);
		
		float x2 = x + (c * w - s * -h);
		float y2 = y + (s * w + c * -h);
		
		float x3 = x + (c * w - s * h);
		float y3 = y + (s * w + c * h);
		
		float x4 = x + (c * -w - s * h);
		float y4 = y + (s * -w + c * h);
		
		//triangle-1
		renderer.draw(x1, y1, tcs[0], tcs[1], color.x, color.y, color.z, color.w);
		renderer.draw(x2, y2, tcs[2], tcs[3], color.x, color.y, color.z, color.w);
		renderer.draw(x3, y3, tcs[4], tcs[5], color.x, color.y, color.z, color.w);
		
		//triangle-2
		renderer.draw(x1, y1, tcs[0], tcs[1], color.x, color.y, color.z, color.w);
		renderer.draw(x3, y3, tcs[4], tcs[5], color.x, color.y, color.z, color.w);
		renderer.draw(x4, y4, tcs[6], tcs[7], color.x, color.y, color.z, color.w);
	}
	
	public void draw(Vector2f position, Vector2f texcoord, Vector4f color){
		renderer.draw(position, texcoord, color);
	}
	
	public void draw(Transform transform, Mesh mesh, Texture texture, Vector4f materialColor){
		if(!(texture.getHandle() == activeTexture.getHandle())){
			flush();
			activeTexture = texture;
		}
		
		for(int i = 0; i < mesh.getNumVertices(); i++){
			Vertex vertex = mesh.get(i);
			Vector4f color = new Vector4f(vertex.color).mul(materialColor);
			Vector2f position = new Vector2f();
			transform.transform(vertex.position, position);
			renderer.draw(position, vertex.texcoord, color);
		}
	}
	
	protected void drawFullscreenQuad(Texture texture){
		if(!(texture.getHandle() == activeTexture.getHandle())){
			flush();
			activeTexture = texture;
		}
		Camera camera = stack.current();
		float w = camera.getWidth();
		float h = camera.getHeight();
		stack.push();
			draw(0, 0, w, h, 0, 1, 1, texture, Color.WHITE);
		stack.pop();
	}
	
	void flush(){
		Camera camera = stack.current();
		view = camera.getViewMatrix();
		projection = camera.getProjectionMatrix();
		shader.bind();
		shader.setMatrix("view", view);
		shader.setMatrix("projection", projection);
		shader.setTexture("texture0", activeTexture, 0);
		renderer.flushToGL();
		shader.release();
		batchCount++;
	}
	
	public void destroy(){
		renderer.destroy();
	}
	
	public void setShader(Shader program){
		if(program == null)
			shader = passthroughShader;
		else
			shader = program;
	}
}
