package com.remnant.ui;

import java.util.ArrayList;

public class MenuBar extends AnchorPane {

	ArrayList<Menu> menus = new ArrayList<Menu>();
	
	Label mapName;
	
	public MenuBar() {
		dragable = false;
		style.backgroundColor.set(0.18f, 0.18f, 0.18f, 1);
		setPrefSize(0, 48);
		setPercentSize(1, 0);
		setSizeMode(PERCENT, PREFERRED);
		mapName = new Label("");
		add(mapName, AnchorLayout.CENTER);
	}
	
	public void setMapName(String mapName){
		this.mapName.setText("[MAP] " + mapName);
	}
	
	public void onMenuOpen(Menu menu){
		for(int i = 0; i < menus.size(); i++){
			Menu m = menus.get(i);
			if(menu != m)
				m.closeMenu();
		}
	}
	
	public void addMenu(Menu menu){
		menu.menuBar = this;
		add(menu, AnchorLayout.LEFT);
		menus.add(menu);
		setZIndex(10000);
		menu.setZIndex(10000);
	}
	
}
