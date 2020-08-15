package com.remnant.graphics;


public class CharDescriptor {

	public float x, y;
	public float width, height;
	public float xOffset, yOffset;
	public float xAdvance;
	public TextureRegion texRegion;
	
	public void print(){
		System.out.println(x + " " + y);
		System.out.println(width + " " + height);
		System.out.println(xOffset + " " + yOffset);
		System.out.println(xAdvance);
		System.out.println(texRegion);
	}
	
}
