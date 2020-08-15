package com.remnant.geometry;

public class AABB {

	public float x;
	public float y;
	public float width;
	public float height;
	
	public AABB(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public boolean contains(float px, float py){
		return 	(Math.abs(this.x - px) * 2 <= (this.width)) && 
				(Math.abs(this.y - py) * 2 <= (this.height));
	}
	
	public boolean intersects(AABB other){
		return 	(Math.abs(this.x - other.x) * 2 <= (this.width + other.width)) && 
				(Math.abs(this.y - other.y) * 2 <= (this.height + other.height));
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof AABB){
			AABB bounds = (AABB)other;
			int selfX = Math.round(x);
			int selfY = Math.round(y);
			int selfW = Math.round(width);
			int selfH = Math.round(height);
			int otherX = Math.round(bounds.x);
			int otherY = Math.round(bounds.y);
			int otherW = Math.round(bounds.width);
			int otherH = Math.round(bounds.height);
			return (selfX == otherX) && (selfY == otherY) && (selfW == otherW) && (selfH == otherH);
		} else {
			return false;
		}
	}
}
