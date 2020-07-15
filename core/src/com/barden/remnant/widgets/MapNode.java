package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.barden.remnant.Map;

public class MapNode extends GameObjectNode {

	Map map;
	
	public MapNode(Object object, String name, Skin skin) {
		super(object, name, skin);
		if(object instanceof Map)
			map = (Map)object;
	}
	
}
