package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class PhysicsNode extends Node {

	public PhysicsNode(Skin skin){
		super(new Label("Physics", skin));
	}
	
}