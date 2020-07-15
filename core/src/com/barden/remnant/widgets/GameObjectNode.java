package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class GameObjectNode extends Tree.Node {

	protected Object gameObject;
	
	public GameObjectNode(Object object, String name, Skin skin) {
		super(new Label(name, skin));
		gameObject = object;
	}

}
