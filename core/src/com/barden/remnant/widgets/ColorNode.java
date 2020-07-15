package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class ColorNode extends Node {

	public ColorNode(Skin skin){
		super(new Label("Node", skin));
	}
	
}