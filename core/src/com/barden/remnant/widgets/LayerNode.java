package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.barden.remnant.Layer;

public class LayerNode extends GameObjectNode {

	protected Layer layer;
	
	public LayerNode(Object object, String name, Skin skin) {
		super(object, name, skin);
		if(object instanceof Layer) {
			layer = (Layer) object;
		} else {
			throw new IllegalArgumentException("Object is a NOT Layer");
		}
	}
	
}
