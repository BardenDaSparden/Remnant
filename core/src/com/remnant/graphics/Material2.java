package com.remnant.graphics;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.ui.EditorAttribute;

public class Material2 {

	Texture diffuse;
	Texture normal;
	Texture illumination;
	Texture emissive;
	
	Vector4f baseColor;
	Vector4f illuminationColor;
	Vector4f emissiveColor;
	
	public Material2(){
		diffuse = new Texture("images/white.png");
		normal = new Texture("images/normal.png");
		illumination = new Texture("images/white.png");
		emissive = new Texture("images/white.png");
		baseColor = new Vector4f(1, 1, 1, 1);
		illuminationColor = new Vector4f(1, 1, 1, 1);
		emissiveColor = new Vector4f(1, 1, 1, 1);
	}
	
	public Texture getDiffuse(){
		return diffuse;
	}
	
	public void setDiffuse(Texture r){
		diffuse = r;
	}
	
	public Texture getNormal(){
		return normal;
	}
	
	public void setNormal(Texture r){
		normal = r;
	}
	
	public Texture getIllumination(){
		return illumination;
	}
	
	public void setIllumination(Texture r){
		illumination = r;
	}
	
	public Texture getEmissive(){
		return emissive;
	}
	
	public void setEmissive(Texture r){
		emissive = r;
	}
	
	public Vector4f getBaseColor(){
		return baseColor;
	}
	
	public void setBaseColor(Vector4f c){
		baseColor.set(c);
	}
	
	public void setBaseColor(float r, float g, float b, float a){
		baseColor.set(r, g, b, a);
	}
	
	public Vector4f getIlluminationColor(){
		return illuminationColor;
	}
	
	public void setIlluminationColor(Vector4f c){
		illuminationColor.set(c);
	}
	
	public void setIlluminationColor(float r, float g, float b, float a){
		illuminationColor.set(r, g, b, a);
	}
	
	public Vector4f getEmissiveColor(){
		return emissiveColor;
	}
	
	public void setEmissiveColor(Vector4f c){
		emissiveColor.set(c);
	}
	
	public void setEmissiveColor(float r, float g, float b, float a){
		emissiveColor.set(r, g, b, a);
	}
}
