package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class PaletteTab extends Tab {

	String title = "Palette";
	Table table;
	
	public PaletteTab(Skin skin){
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
