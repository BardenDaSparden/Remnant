package com.remnant.ui;

import org.joml.Vector2f;

public class ScrollPane extends Container {
	
	FlowLayout contentLayout;
	VBox content;
	VScrollBar vScroll;
	
	boolean vScrollVisible = false;
	boolean hScrollVisible = false;
	
	int scrollOffsetX = 0;
	int scrollOffsetY = 0;
	
	public ScrollPane(int width, int height){
		FlowLayout layout = new FlowLayout();
		layout.setDirection(FlowLayout.HORIZONTAL);
		layout.setAlignment(FlowLayout.LEADING);
		setLayout(layout);
		setPrefSize(width, height);
		setSizeMode(PREFERRED, PREFERRED);
		contentLayout = new FlowLayout();
		contentLayout.setDirection(FlowLayout.VERTICAL);
		contentLayout.setAlignment(FlowLayout.LEADING);
		content = new VBox();
		content.setLayout(contentLayout);
		content.setPrefSize(width - VScrollBar.WIDTH, height);
		content.style.backgroundColor.set(0, 0, 0, 0.2f);
		vScroll = new VScrollBar(this, height);
		add(content);
		add(vScroll);
		
		
//		for(int i = 0; i < 25; i++){
//			Label l = new Label(i * i + "" );
//			content.add(l);
//		}
		
	}
	
	public void scrollUp(){
		scrollOffsetY += 20;
		contentLayout.setScrollOffset(scrollOffsetX, scrollOffsetY);
		contentLayout.positionNodes();
	}
	
	public void scrollDown(){
		scrollOffsetY -= 20;
		contentLayout.setScrollOffset(scrollOffsetX, scrollOffsetY);
		contentLayout.positionNodes();
	}
	
	public void add(UINode node){
		super.add(node);
		Vector2f extents = contentLayout.getChildrenExtents();
		if(extents.x > width)
			hScrollVisible = true;
		else
			hScrollVisible = false;
		
		if(extents.y > height)
			vScrollVisible = true;
		else
			vScrollVisible = false;
	}
	
}

class VScrollBar extends VBox{
	
	public static final int WIDTH = 16;
	
	ScrollPane scrollPane;
	
	Button upButton;
	Button downButton;
	Container barContainer;
	
	public VScrollBar(ScrollPane pane, int height){
		scrollPane = pane;
		style.backgroundColor.set(0, 0, 0, 0.2f);
		setPrefSize(WIDTH, height);
		setSizeMode(PREFERRED, PREFERRED);
		
		final int BUTTON_SIZE = WIDTH;
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(1, 1, 1, 0.1f);
		
		upButton = new Button("UP");
		upButton.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
		upButton.setStyle(btnStyle);
		upButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				scrollPane.scrollUp();
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
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
		
		downButton = new Button("DOWN");
		downButton.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
		downButton.setStyle(btnStyle);
		downButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				scrollPane.scrollDown();
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverEnd() {
				// TODO Auto-generated method stub
				
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
		
		barContainer = new Container();
		barContainer.setPrefSize(WIDTH, height - (BUTTON_SIZE * 2));
		barContainer.style.backgroundColor.set(0, 0, 0, 0.1f);
		
		add(upButton);
		add(barContainer);
		add(downButton);
		
	}
	
}