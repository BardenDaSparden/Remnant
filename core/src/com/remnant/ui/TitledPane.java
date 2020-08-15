package com.remnant.ui;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.remnant.graphics.FontLoader;

public class TitledPane extends Container {
	
	final Vector4f BACKGROUND_COLOR = new Vector4f(0.18f, 0.18f, 0.18f, 1f);
	final Vector4f TITLE_COLOR = new Vector4f(0.18f, 0.18f, 0.18f, 1);
	final Vector4f TEXT_COLOR = new Vector4f(0.74f, 0.74f, 0.74f, 1);
	
	String titleStr;
	Label title;
	Container titleContainer;
	Container content;
	
	public TitledPane(String titleStr){
		this.titleStr = titleStr;
		style.backgroundColor.set(0, 0, 0, 0.0f);
		FlowLayout titlePaneLayout = new FlowLayout();
		titlePaneLayout.setDirection(FlowLayout.VERTICAL);
		titlePaneLayout.setAlignment(FlowLayout.LEADING);
		//titlePaneLayout.useChildrenSize(true);
		setLayout(titlePaneLayout);
		setPrefSize(200, 200);
		setSizeMode(PREFERRED, PREFERRED);
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
		btnStyle.textColor.set(1, 1, 1, 0.2f);
		btnStyle.font = FontLoader.getFont("fonts/imagine_24.fnt");
		
		UIStyle hoverStyle = new UIStyle();
		hoverStyle.backgroundColor.set(1, 1, 1, 0.05f);
		hoverStyle.textColor.set(1, 1, 1, 0.2f);
		hoverStyle.font = FontLoader.getFont("fonts/imagine_24.fnt");
		
		title = new Label(titleStr);
		title.style.textColor.set(TEXT_COLOR);
		title.style.font = FontLoader.getFont("fonts/open_sans_16.fnt");
		title.setText(titleStr);
		
		FlowLayout layout = new FlowLayout();
		layout.setDirection(FlowLayout.HORIZONTAL);
		layout.setAlignment(FlowLayout.CENTER);
		
		titleContainer = new Container();
		titleContainer.setLayout(layout);
		titleContainer.style.backgroundColor.set(TITLE_COLOR);
		titleContainer.setPrefSize(200, 36);
		titleContainer.setSizeMode(PREFERRED, PREFERRED);
		titleContainer.add(title);
		add(titleContainer);
		
		FlowLayout layout2 = new FlowLayout();
		layout2.setDirection(FlowLayout.VERTICAL);
		layout2.setAlignment(FlowLayout.LEADING);
		
		content = new Container();
		content.setLayout(layout2);
		content.style.backgroundColor.set(BACKGROUND_COLOR);
		content.setSizeMode(PREFERRED, PREFERRED);
		add(content);
		
	}
	
	public void setPrefSize(int prefWidth, int prefHeight){
		super.setPrefSize(prefWidth, prefHeight);
		setSize(prefWidth, prefHeight);
	}
	
	public void setSize(int width, int height){
		if(titleContainer != null && content != null){
			titleContainer.prefWidth = width;
			titleContainer.prefHeight = 36;
			content.prefWidth = width;
			content.prefHeight = height - 36;
		}
	}
	
//	protected void resizeDescending(){
//		super.resizeDescending();
//		if(titleContainer != null && content != null){
//			int windowHeight = super.height;
//			int remaining = windowHeight - (titleContainer.height * 1);
//			
//			content.prefHeight = remaining;
//			content.heightMode = PREFERRED;
//			if(!content.isVisible){
//				content.prefHeight = 0;
//			}
//			super.resizeDescending();
//		}
//	}
	
	
	public void addToContent(UINode node){
		content.add(node);
	}
	
}
