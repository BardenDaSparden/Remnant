package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class CreateTab extends Tab {

	String title = "Create";
	Table table;
	
	public CreateTab(Skin skin){
		super(false, false);
		table = new Table(skin);
	}

	@Override
	public Table getContentTable() {
		return table;
	}

	@Override
	public String getTabTitle() {
		return title;
	}
	
}
