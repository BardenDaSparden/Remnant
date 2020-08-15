package com.remnant.graphics;

public abstract class TypedUIRenderer implements Comparable<TypedUIRenderer> {

	protected CameraStack cameraStack;
	protected TextureBatch batch;
	private int layer;
	
	public TypedUIRenderer(int layerIndex){
		layer = layerIndex;
	}
	
	protected void setCameraStack(CameraStack stack){
		cameraStack = stack;
	}
	
	protected void setTextureBatch(TextureBatch textureBatch){
		batch = textureBatch;
	}
	
	public abstract void draw();
	
	@Override
	public int compareTo(TypedUIRenderer renderer){
		return (this.layer < renderer.layer) ? -1 :
				(this.layer == renderer.layer) ? 0 : 1;
	}
	
}
