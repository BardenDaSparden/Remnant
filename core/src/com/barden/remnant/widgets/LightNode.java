package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.barden.remnant.GameObject;

public class LightNode extends GameObjectNode {

	public LightNode(GameObject object, Skin skin) {
		super(object, object.toString(), skin);
	}
	
}
