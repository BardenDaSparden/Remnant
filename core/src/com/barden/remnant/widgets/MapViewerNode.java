package com.barden.remnant.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapViewerNode extends Table {

	MapView view;
	
	public MapViewerNode(Skin skin){
		super(skin);
		view = new MapView(skin);
		setTouchable(Touchable.enabled);
		add(view).fill().expand().grow();
	}
	
	public void setTexture(Texture tex){
		view.texture = tex;
	}
}


@SuppressWarnings("rawtypes")
class MapView extends Container {
	
	Texture texture;
	
	public MapView(Skin skin){
		
	}
	
	@Override
	public void draw(Batch batch, float opacity){
		if(texture == null)
			return;
		batch.draw(texture, getX(), getY());
	}
	
}