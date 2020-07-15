package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class MaterialNode extends Node {

	public MaterialNode(Skin skin){
		super(new Label("Material", skin));
		add(new ColorNode(skin));
	}
	
}

