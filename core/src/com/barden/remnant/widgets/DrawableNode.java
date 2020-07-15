package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class DrawableNode extends Node {

	public DrawableNode(Skin skin){
		super(new Label("Drawable", skin));
		add(new MaterialNode(skin));
		add(new TransformNode(skin));
	}
	
}
