package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class TabPane extends Table {

	int numTabs;
	HorizontalGroup tabGroup;
	public Table content;
	
	public TabPane(Skin skin, Actor...tabObjects){
		super(skin);
		
		tabGroup = new HorizontalGroup();
		
		for(Actor a : tabObjects)
			tabGroup.addActor(a);
		
		tabGroup.align(Align.center);
		tabGroup.space(4);
		
		content = new Table(skin);
		
		add(tabGroup).top().center().fill().row();
		add(content).top().fill().expand();
		
	}
	
}
