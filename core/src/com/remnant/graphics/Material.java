package com.remnant.graphics;

import org.joml.Vector4f;

public class Material {

	TextureRegion diffuse;
	TextureRegion normal;
	TextureRegion illumination;
	Vector4f color;
	
	public Material(TextureRegion diffuse){
		this(diffuse, null);
	}
	
	public Material(TextureRegion diffuse, TextureRegion normal){
		this(diffuse, normal, null);
	}
	
	public Material(TextureRegion diffuse, TextureRegion normal, TextureRegion illumination){
		this.diffuse = diffuse;
		this.normal = normal;
		this.illumination = illumination;
		this.color = new Vector4f(1, 1, 1, 1);
	}
	
	public Material set(Material material){
		this.diffuse = material.diffuse;
		this.normal = material.normal;
		this.illumination = material.illumination;
		this.color = new Vector4f(material.color);
		return this;
	}
	
	public TextureRegion getDiffuse(){
		return diffuse;
	}
	
	public Material setDiffuse(TextureRegion region){
		this.diffuse = region;
		return this;
	}
	
	public TextureRegion getNormal(){
		return normal;
	}
	
	public Material setNormal(TextureRegion region){
		this.normal = region;
		return this;
	}
	
	public TextureRegion getIllumination(){
		return illumination;
	}
	
	public Material setIllumination(TextureRegion region){
		this.illumination = region;
		return this;
	}
	
	public Vector4f getColor(){
		return color;
	}
	
	public Material setColor(Vector4f c){
		this.color.set(c);
		return this;
	}
	
}
