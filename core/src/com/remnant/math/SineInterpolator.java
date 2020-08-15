package com.remnant.math;

public class SineInterpolator extends BaseInterpolator{

	public float interpolate(float start, float end, float weight){
		if(weight == 0){
			return start;
		} else if(weight == 1){
			return end;
		} else {
			return (float) (start + ((end - start) * Math.sin(Math.PI * weight)));
		}
	}
}
