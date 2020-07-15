package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MaterialProperty extends Property {

	TextureAttribute da;
	TextureAttribute na;
	TextureAttribute sa;
	TextureAttribute ga;
	
	ColorProperty ca;
	
	public MaterialProperty(Skin skin){
		super("Material", skin);
		da = new TextureAttribute(skin);
		da.setName("Diffuse");
		
		na = new TextureAttribute(skin);
		na.setName("Normal");
		
		sa = new TextureAttribute(skin);
		sa.setName("Specular");
		
		ga = new TextureAttribute(skin);
		ga.setName("Glow");
		
		ca = new ColorProperty(skin);
		
		content.addActor(da);
		content.addActor(na);
		content.addActor(sa);
		content.addActor(ga);
		content.addActor(ca);
	}
	
}
