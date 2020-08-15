package com.remnant.ui;

public class Menu extends AnchorPane {
	
	MenuBar menuBar;
	
	Button menuButton;
	UIStyle defaultStyle;
	UIStyle openStyle;
	
	MenuBody menuBody;
	
	boolean isFirstItem = true;
	boolean isMenuOpen = false;
	
	public Menu(String menuName){
		setPrefSize(100, 0);
		setPercentSize(0, 1);
		setSizeMode(PREFERRED, PERCENT);
		style.backgroundColor.set(0, 0, 0, 0.0f);
		defaultStyle = new UIStyle();
		defaultStyle.backgroundColor.set(0.18f, 0.18f, 0.18f, 1);
		defaultStyle.textColor.set(1, 1, 1, 1);
		
		openStyle = new UIStyle();
		openStyle.backgroundColor.set(0.1f, 0.1f, 0.1f, 1);
		openStyle.textColor.set(1, 1, 1, 1);
		
		menuButton = new Button(menuName);
		menuButton.setPrefSize(100, 48);
		menuButton.setSizeMode(PREFERRED, PARENT);
		menuButton.setStyle(defaultStyle);
		add(menuButton, AnchorLayout.TOP);
		
		menuBody = new MenuBody();
		add(menuBody, AnchorLayout.TOP);
		
		closeMenu();
	}
	
	void updateMenuPosition(){
		float newX = menuButton.posX - width / 2f + menuBody.getWidth() / 2f;
		float newY = menuButton.posY - height / 2f - menuBody.getHeight() / 2f;
		menuBody.setPosition(newX, newY);
	}
	
	void openMenu(){
		menuButton.setStyle(openStyle);
		isMenuOpen = true;
		menuBar.onMenuOpen(this);
		menuBody.show();
		updateMenuPosition();
	}
	
	void closeMenu(){
		menuButton.setStyle(defaultStyle);
		isMenuOpen = false;
		menuBody.hide();
		updateMenuPosition();
	}
	
	protected void onMouseRelease(){
		super.onMouseRelease();
		if(isMenuOpen)
			closeMenu();
		else
			openMenu();
	}
	
	public void addItem(MenuItem item){
		
		item.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				closeMenu();
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
		
		if(!isFirstItem){
			HorizontalSeperatorSmall sep = new HorizontalSeperatorSmall(1);
			sep.hide();
			menuBody.add(sep);
		}
		
		item.hide();
		menuBody.add(item);
		
		
		isFirstItem = false;
		
	}
	
}

class MenuBody extends VBox{
	
	public MenuBody(){
		style.backgroundColor.set(0.1f, 0.1f, 0.1f, 1);
		setPrefSize(200, 400);
		setSizeMode(PREFERRED, PREFERRED);
	}
	
	public void add(UINode node){
		super.add(node);
		int totalHeight = 0;
		for(int i = 0; i < children.size(); i++){
			UINode child = children.get(i);
			totalHeight += child.getHeight();
		}
		setPrefSize(prefWidth, totalHeight);
	}
	
}