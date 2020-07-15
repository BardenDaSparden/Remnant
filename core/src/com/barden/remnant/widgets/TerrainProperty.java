package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class TerrainProperty extends Property {

	DrawableProperty da;
	
	public TerrainProperty(Skin skin){
		super("Terrain", skin);
		da = new DrawableProperty(skin);
		
		content.addActor(da);
		content.align(Align.top);
	}
	
}
