package com.remnant.ui;

import org.joml.Vector4f;

public class AccordianPane extends Container {

	final int TITLE_HEIGHT = 35;
	
	int initialWidth;
	int initialHeight;
	
	final Vector4f inactiveColor = new Vector4f(0.12f, 0.12f, 0.12f, 1);
	final Vector4f activeColor = new Vector4f(0.20f, 0.20f, 0.20f, 1);
	
	Container title;
	Label titleLabel;
	Label statusLabel;
	String titleStr;
	
	Container body;
	
	boolean isActive = false;
	
	public AccordianPane(UINode parent, String titleStr){
		super.parent = parent;
		this.titleStr = titleStr;
		
		initialWidth = width;
		initialHeight = height;
		
		FlowLayout layout = new FlowLayout();
		layout.setDirection(FlowLayout.VERTICAL);
		layout.setAlignment(FlowLayout.LEADING);
		//layout.useChildrenSize(true);
		
		//style.backgroundColor.set(1, 0, 0, 0.5f);
		setLayout(layout);
		setPrefSize(parent.width , TITLE_HEIGHT);
		setSizeMode(PREFERRED, PREFERRED);
		
		FlowLayout titleLayout = new FlowLayout();
		titleLayout.setDirection(FlowLayout.HORIZONTAL);
		titleLayout.setAlignment(FlowLayout.LEADING);
		titleLayout.setSpacing(10, 0);
		
		title = new Container();
		title.setLayout(titleLayout);
		title.setPrefSize(initialWidth, TITLE_HEIGHT);
		title.setPercentSize(1, 0);
		title.setSizeMode(PREFERRED, PREFERRED);
		//title.style.backgroundColor.set(activeColor);
		title.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				if(isActive){
					hideBody();
				} else {
					showBody();
				}
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
		
		statusLabel = new Label("+");
		statusLabel.setMinSize(20, 20);
		statusLabel.setMaxSize(20, 20);
		statusLabel.setSizeMode(PREFERRED, PREFERRED);
		titleLabel = new Label(titleStr);
		
		title.add(statusLabel);
		title.add(titleLabel);
		
		add(title);
		
		FlowLayout bodyLayout = new FlowLayout();
		bodyLayout.setDirection(FlowLayout.VERTICAL);
		bodyLayout.setAlignment(FlowLayout.LEADING);
		bodyLayout.useChildrenSize(true);
		body = new Container();
		body.setLayout(bodyLayout);
		//body.setPrefSize(400, 400);
		body.setPrefSize(400, 0);
		body.setSizeMode(PREFERRED, PREFERRED);
		//body.style.backgroundColor.set(0, 1, 0, 0.5f);
		body.hide();
		add(body);
		hideBody();
	}
	
	public void addToContent(UINode node){
		body.add(node);
	}
	
	public void show(){
		super.show();
		body.hide();
		if(isActive)
			body.show();
	}
	
	public void hideBody(){
		isActive = false;
		statusLabel.setText("+");
		body.hide();
		//.style.backgroundColor.set(inactiveColor);
		//setPrefSize(initialWidth, TITLE_HEIGHT);
		resizeDescending();
		updateLayout();
		//resizeDescending();
		resizeAscending();
		
		//repositionNodes();
		//resizeAll();
		//resizeParent()
	}
	
	public void showBody(){
		isActive = true;
		statusLabel.setText("-");
		body.show();
		//body.style.backgroundColor.set(inactiveColor);
		//title.style.backgroundColor.set(activeColor);
		//setPrefSize(initialWidth, TITLE_HEIGHT + body.getHeight());
		resizeDescending();
		updateLayout();
		//resizeDescending();
		resizeAscending();
		//repositionNodes();
		//resizeAll();
		//resizeParent();
	}
	
}
