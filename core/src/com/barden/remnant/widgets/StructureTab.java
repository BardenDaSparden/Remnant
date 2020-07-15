package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class StructureTab extends Tab {

	String title = "Structure";
	Table table;
	
	public StructureTab(Skin skin){
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
