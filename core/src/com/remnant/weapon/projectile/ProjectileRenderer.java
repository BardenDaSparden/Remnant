package com.remnant.weapon.projectile;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.TypedRenderer;
import com.remnant.math.Color;
import com.remnant.math.Math2D;

public class ProjectileRenderer extends TypedRenderer {

	static final int LAYER = 100;
	static final String NAME = "Projectile Renderer";
	
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	Texture diffuse;
	Texture illum;
	Texture white;
	Vector4f color;
	
	public ProjectileRenderer(){
		super(NAME, LAYER);
		diffuse = new Texture("images/white.png");
		illum = new Texture("images/white.png");
		color = new Vector4f(0.85f, 1, 0.92f, 1);
	}
	
	public void add(Projectile projectile){
		projectiles.add(projectile);
	}
	
	public void remove(Projectile projectile){
		projectiles.remove(projectile);
	}
	
	float calcWidth(HitscanProjectile proj){
		return Math2D.distance(proj.start, proj.end);
	}
	
	float calcHeight(Projectile proj){
		return 1f;
	}
	
	float calcAngle(HitscanProjectile proj){
		return (float)Math.atan2((proj.end.y - proj.start.y), (proj.end.x - proj.start.x));
	}
	
	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPositions(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < projectiles.size(); i++){
			Projectile p = projectiles.get(i);
			if(p instanceof HitscanProjectile){
				HitscanProjectile proj = (HitscanProjectile) p;
				float x = (proj.end.x + proj.start.x) / 2.0f;
				float y = (proj.end.y + proj.start.y) / 2.0f;
				float w = calcWidth(proj);
				float h = calcHeight(proj);
				float r = calcAngle(proj);
				batch.draw(x, y, w, h, r, 1, 1, diffuse, Color.WHITE);
			}
		}
		batch.end();
	}

	@Override
	public void drawDiffuse(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < projectiles.size(); i++){
			Projectile p = projectiles.get(i);
			if(p instanceof HitscanProjectile){
				HitscanProjectile proj = (HitscanProjectile) p;
				float x = (proj.end.x + proj.start.x) / 2.0f;
				float y = (proj.end.y + proj.start.y) / 2.0f;
				float w = calcWidth(proj);
				float h = calcHeight(proj);
				float r = calcAngle(proj);
				color.w = 1.0f - ((float)proj.life / (float)proj.maxLife);
				batch.draw(x, y, w, h, r, 1, 1, diffuse, color);
			}
		}
		batch.end();
	}

	@Override
	public void drawNormals(double alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawIllumination(double alpha) {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < projectiles.size(); i++){
			Projectile p = projectiles.get(i);
			if(p instanceof HitscanProjectile){
				HitscanProjectile proj = (HitscanProjectile) p;
				float x = (proj.end.x + proj.start.x) / 2.0f;
				float y = (proj.end.y + proj.start.y) / 2.0f;
				float w = calcWidth(proj);
				float h = calcHeight(proj);
				float r = calcAngle(proj);
				color.w = (1.0f - ((float)proj.life / (float)proj.maxLife));
				batch.draw(x, y, w, h, r, 1, 1, diffuse, color);
			}
		}
		batch.end();
	}

	@Override
	public void drawEmissive(double alpha){
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
		for(int i = 0; i < projectiles.size(); i++){
			Projectile p = projectiles.get(i);
			if(p instanceof HitscanProjectile){
				HitscanProjectile proj = (HitscanProjectile) p;
				float x = (proj.end.x + proj.start.x) / 2.0f;
				float y = (proj.end.y + proj.start.y) / 2.0f;
				float w = calcWidth(proj);
				float h = calcHeight(proj);
				float r = calcAngle(proj);
				batch.draw(x, y, w, h, r, 1, 1, illum, Color.WHITE);
			}
			
		}
		batch.end();
	}
	
}
