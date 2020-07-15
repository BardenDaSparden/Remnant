package com.barden.remnant.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Menu extends Table {

	protected M<Object> select;
	
	public Menu(Skin skin, String menuName, Object...objects){
		SelectBox<Object> sb = new SelectBox<Object>(skin);
		select = new M<Object>(menuName, sb.getStyle());
		select.setItems(objects);
		
		Texture tex = new Texture(Gdx.files.internal("empty.png"));
		TextureRegion region = new TextureRegion(tex);
		TextureRegionDrawable background = new TextureRegionDrawable(region);
		select.getStyle().background = background;
		
		//For easier handling of Widgets
		setFillParent(true); 
		left();
		top();

		add(select);
		
		//select.setSelected(null);
		//select.setSelectedIndex(-1);
		//select.setName("menu");	
	}
	
	public void addChangeListener(ChangeListener listener){
		select.addListener(listener);
	}
	
}

class M<T> extends SelectBox<T> {

	String name;
	SelectBoxStyle style;
	
	public M(String menuName, SelectBoxStyle skin) {
		super(skin);

		name = menuName;
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void draw (Batch batch, float a) {
		validate();

		Drawable background;
//		if (isDisabled() && style.backgroundDisabled != null)
//			background = style.backgroundDisabled;
//		else if (selectBoxList.hasParent() && style.backgroundOpen != null)
//			background = style.backgroundOpen;
//		else if (clickListener.isOver() && style.backgroundOver != null)
//			background = style.backgroundOver;
//		else if (style.background != null)
//			background = style.background;
//		else
		background = super.getStyle().background;
		BitmapFont font = super.getStyle().font;
		Color fontColor = (isDisabled() && super.getStyle().disabledFontColor != null) ? super.getStyle().disabledFontColor : super.getStyle().fontColor;

		Color color = getColor();
		float x = getX(), y = getY();
		float width = getWidth(), height = getHeight();

		batch.setColor(color.r, color.g, color.b, color.a * a);
		if (background != null) background.draw(batch, x, y, width, height);

		T selected = (T) name;
		if (selected != null) {
			if (background != null) {
				width -= background.getLeftWidth() + background.getRightWidth();
				height -= background.getBottomHeight() + background.getTopHeight();
				x += background.getLeftWidth();
				y += (int)(height / 2 + background.getBottomHeight() + font.getData().capHeight / 2);
			} else {
				y += (int)(height / 2 + font.getData().capHeight / 2);
			}
			font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * a);
			drawItem(batch, font, selected, x, y, width);
		}
	}
	
}