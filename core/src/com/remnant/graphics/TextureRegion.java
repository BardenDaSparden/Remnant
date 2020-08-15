package com.remnant.graphics;

import com.remnant.assets.Texture;

public class TextureRegion {

	protected Texture baseTexture;
	protected float width;
	protected float height;
	protected float[] texCoords;
	
	public TextureRegion(Texture texture){
		this(texture, 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public TextureRegion(Texture texture, float x, float y, float width, float height){
		this.baseTexture = texture;
		this.width = width;
		this.height = height;
		texCoords = new float[8];
		
		float baseWidth = baseTexture.getWidth();
		float baseHeight = baseTexture.getHeight();
		
		float minX = x / baseWidth;
		float maxX = minX + (width / baseWidth);
		
		float minY = (y / baseHeight);
		float maxY = (minY + (height / baseHeight));
		
		float texelWidth = 1.0f / baseTexture.getWidth();
		float texelHeight = 1.0f / baseTexture.getHeight();
		
		float offsetX = texelWidth / 8.0f;
		float offsetY = texelHeight / 8.0f;
		
		texCoords[0] = minX + offsetX;
		texCoords[1] = minY + offsetY;
		
		texCoords[2] = maxX + offsetX;
		texCoords[3] = minY + offsetY;
		
		texCoords[4] = maxX + offsetX;
		texCoords[5] = maxY + offsetY;
		
		texCoords[6] = minX + offsetX;
		texCoords[7] = maxY + offsetY;
	}
	
	public void flip(boolean xAxis, boolean yAxis){
		if(xAxis){
			float minX = texCoords[0];
			float maxX = texCoords[2];
			
			texCoords[0] = maxX;
			texCoords[6] = maxX;
			
			texCoords[2] = minX;
			texCoords[4] = minX;
		}
		
		if(yAxis){
			float minY = texCoords[1];
			float maxY = texCoords[5];
			
			texCoords[1] = maxY;
			texCoords[3] = maxY;
			
			texCoords[5] = minY;
			texCoords[7] = minY;
		}
	}
	
	public Texture getBase(){
		return baseTexture;
	}
	
	public float[] getTexCoords(){
		return texCoords;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}
