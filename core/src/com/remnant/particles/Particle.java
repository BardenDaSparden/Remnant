package com.remnant.particles;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.math.Transform;

public class Particle {

	public Transform transform = new Transform();
	public Texture texture = new Texture("images/white.png");
	public Vector4f color = new Vector4f(1, 1, 1, 1);
	public Vector4f emissiveColor = new Vector4f(1, 1, 1, 1);
	public boolean useDiffuseAsEmissive = false;
	public int life = 0, maxLife = 400;
	public float vx = 0, vy = 0;
	public float av = 0;
	public float ss = 0;
	public float illum = 0;
	
	public Particle create(){
		Particle p = new Particle();
		p.transform.set(transform);
		p.texture = texture;
		p.maxLife = maxLife;
		Vector4f c = new Vector4f(color.x, color.y, color.z, color.w);
		p.color = c;
		return p;
	}
	
}
