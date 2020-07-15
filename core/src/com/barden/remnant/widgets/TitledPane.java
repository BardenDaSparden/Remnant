package com.barden.remnant.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class TitledPane extends Table {	
	
	public TextButton titleLabel;
	public Table content;
	
	public TitledPane(String title, Skin skin){
		titleLabel = new TextButton(title, skin);
		titleLabel.align(Align.center);
		titleLabel.pad(15);
		
		content = new Table(skin);
		
		add(titleLabel).top().fill(1, 0).expand(1, 0).row();
		add(content).top().fill().expand();
	}
	
}