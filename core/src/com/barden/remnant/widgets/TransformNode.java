package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class TransformNode extends Node {

	public TransformNode(Skin skin){
		super(new Label("Transforms", skin));
	}
	
}