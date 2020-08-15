package com.remnant.graphics;

import java.util.ArrayList;

public abstract class TypedRenderer implements Comparable<TypedRenderer>{
	
	protected TextureBatch batch;
	protected CameraStack cameraStack;
	protected int layer;
	protected String name;
	
	ArrayList<TypedRenderer> renderers;
	
	public TypedRenderer(String rendererName, int layer){
		this.name = rendererName;
		this.layer = layer;
		renderers = new ArrayList<TypedRenderer>();
	}
	
	public void add(TypedRenderer renderer){
		renderers.add(renderer);
	}
	
	public void remove(TypedRenderer renderer){
		renderers.remove(renderer);
	}
	
	public boolean hasChildRenderers(){
		return renderers.size() > 0;
	}
	
	public abstract void prepare();
	public abstract void drawPositions(double alpha);
	public abstract void drawDiffuse(double alpha);
	public abstract void drawNormals(double alpha);
	public abstract void drawIllumination(double alpha);
	public abstract void drawEmissive(double alpha);
	
//	public abstract void drawPositions();
//	public abstract void drawDiffuse();
//	public abstract void drawNormals();
//	public abstract void drawIllumination();
//	public abstract void drawEmissive();
	
	void print(int tabCount){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < tabCount; i++)
			str.append("\t");
		str.append("Layer: " + layer + " " + name + "\n");
		System.out.print(str.toString());
	}
	
	void printAll(int tabCount){
		if(hasChildRenderers()){
			print(tabCount);
			for(TypedRenderer renderer : renderers)
				renderer.printAll(tabCount + 1);
		} else {
			print(tabCount);
		}
	}
	
	@Override
	public int compareTo(TypedRenderer renderer){
		return (this.layer < renderer.layer) ? -1 :
				(this.layer == renderer.layer) ? 0 : 1;
	}
	
}
