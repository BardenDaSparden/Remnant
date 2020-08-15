package com.remnant.ui;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.remnant.graphics.FontLoader;

public class Window extends VBox{
	
	final Vector4f BACKGROUND_COLOR = new Vector4f(0.12f, 0.12f, 0.12f, 1f);
	final Vector4f TITLE_COLOR = new Vector4f(0.155f, 0.155f, 0.155f, 1);
	final Vector4f TEXT_COLOR = new Vector4f(0.74f, 0.74f, 0.74f, 1);
	
	String titleStr;
	
	Label title;
	Button closeButton;
	Button minimizeButton;
	AnchorPane titleContainer;
	VBox content;
	
	boolean isDialog = false;
	
	ArrayList<WindowListener> windowListeners;
	
	public Window(String titleStr){
		this.titleStr = titleStr;
		windowListeners = new ArrayList<WindowListener>();
		
		dragable = true;
		
		int w = 600;
		int h = 300;
		
		//shrink(true);
		style.backgroundColor.set(BACKGROUND_COLOR);
		setPrefSize(w, h);
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
		
		final int BUTTON_WIDTH = 24;
		final int BUTTON_HEIGHT = 24;
		
		closeButton = new Button("X");
		closeButton.setStyle(btnStyle);
		closeButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		closeButton.setSizeMode(PREFERRED, PREFERRED);
		closeButton.addListener(new Listener() {
			
			@Override
			public void onHoverStart() {
				closeButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				closeButton.setStyle(btnStyle);
			}

			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseRelease() {
				onClose();
				Window.this.hide();
			}

			@Override
			public void onFocus() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void offFocus() {
				// TODO Auto-generated method stub
				
			}
		});
		
		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(FlowLayout.TRAILING);
		buttonContainer.setPrefSize(100, BUTTON_HEIGHT);
		buttonContainer.setSizeMode(PREFERRED, PREFERRED);
		buttonContainer.add(closeButton);
		
		titleContainer = new AnchorPane();
		titleContainer.setSpacing(10, 0);
		titleContainer.style.backgroundColor.set(TITLE_COLOR);
		titleContainer.setPrefSize(w, 40);
		titleContainer.setSizeMode(PREFERRED, PREFERRED);
		titleContainer.add(title, AnchorLayout.CENTER);
		titleContainer.add(buttonContainer, AnchorLayout.RIGHT);
		add(titleContainer);

		content = new VBox();
		content.setAlignment(FlowLayout.LEADING);
		//content.shrink(true);
		content.style.backgroundColor.set(BACKGROUND_COLOR);
		content.setPrefSize(w, 300);
		content.setSizeMode(PREFERRED, PREFERRED);
		add(content);
		
	}
	
	public void show(){
		super.show();
		stage.bringWindowToFront(this);
	}
	
	protected void onDataSubmit(WindowData data){
		for(int i = 0; i < windowListeners.size(); i++){
			WindowListener listener = windowListeners.get(i);
			listener.onDataSubmit(data);
		}
	}
	
	protected void onClose(){
		for(int i = 0; i < windowListeners.size(); i++){
			WindowListener listener = windowListeners.get(i);
			listener.onClose();
		}
	}
	
	public void close(){
		if(isVisible){
			onClose();
			hide();
		}
	}
	
	public void addToContent(UINode node){
		content.add(node);
		resizeSelf();
	}
	
	public void addWindowListener(WindowListener listener){
		windowListeners.add(listener);
	}
	
}
