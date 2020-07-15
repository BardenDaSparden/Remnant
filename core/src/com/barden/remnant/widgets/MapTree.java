package com.barden.remnant.widgets;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.barden.remnant.GameObject;
import com.barden.remnant.Layer;
import com.barden.remnant.Map;

public class MapTree extends Tree {
	
	Skin skin;
	
	public MapTree(Skin skin){
		super(skin);
		this.skin = skin;
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				System.out.println(event.getTarget());
				super.clicked(event, x, y);
			}
		});
	}
	
	public void loadMap(Map map){
		Node root = new Node(new Label("Structure", skin));
		
		MapNode mapNode = new MapNode(map, map.getName(), skin);
		
		ArrayList<Layer> layers = map.getLayers();
		ArrayList<GameObject> layerObjects = null;
		
		for(int i = 0; i < layers.size(); i++){
			Layer layer = layers.get(i);
			
			LayerNode layerNode = new LayerNode(layer, layer.getName(), skin);
			
			layerObjects = layer.getObjects();
			for(int j = 0; j < layerObjects.size(); j++){
				GameObject object = layerObjects.get(j);
				GameObjectNode objectNode = new GameObjectNode(object, object.getType() + " " + object.getName(), skin);
				layerNode.add(objectNode);
			}
			
			mapNode.add(layerNode);
		}
		
		root.add(mapNode);
		add(root);
	}
	
	public void clear(){
		clear();
	}
	
}
