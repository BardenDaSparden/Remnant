package com.remnant.ui;

public class ToolsPane extends TitledPane {

	HBox buttonToolbar;
	
	Button pickObjectButton;
	Button transformObjectButton;
	Button deleteObjectButton;
	
	AccordianPane terrainPane;
	AccordianPane spawnsPane;
	AccordianPane lightsPane;
	AccordianPane effectsPane;
	AccordianPane decorationsPane;
	
	public ToolsPane(UINode parent){
		super("Tools");
		super.parent = parent;
		width = 400;
		setPrefSize(width, parent.height);
		setSizeMode(PREFERRED, PREFERRED);
		
		buttonToolbar = new HBox();
		buttonToolbar.setAlignment(FlowLayout.CENTER);
		buttonToolbar.setPrefSize(width, 60);
		buttonToolbar.setSizeMode(PREFERRED, PREFERRED);
		
		UIStyle btnStyle = new UIStyle();
		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
		
		UIStyle hoverStyle = new UIStyle();
		hoverStyle.backgroundColor.set(1, 1, 1, 0.09f);
		
		Button pickObjectButton = new Button("Pick");
		pickObjectButton.setStyle(btnStyle);
		pickObjectButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				pickObjectButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				pickObjectButton.setStyle(btnStyle);
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
		Button transformObjectButton = new Button("Transform");
		transformObjectButton.setStyle(btnStyle);
		transformObjectButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				transformObjectButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				transformObjectButton.setStyle(btnStyle);
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
		Button deleteObjectButton = new Button("Delete");
		deleteObjectButton.setStyle(btnStyle);
		deleteObjectButton.addListener(new Listener() {
			
			@Override
			public void onMouseRelease() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMousePress() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHoverStart() {
				deleteObjectButton.setStyle(hoverStyle);
			}
			
			@Override
			public void onHoverEnd() {
				deleteObjectButton.setStyle(btnStyle);
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
		
		buttonToolbar.add(pickObjectButton);
		buttonToolbar.add(transformObjectButton);
		buttonToolbar.add(deleteObjectButton);
		
		AccordianPane mapPane = new AccordianPane(this, "Map");
		
		AccordianPane terrainPane = new AccordianPane(mapPane, "Terrain");
		mapPane.addToContent(terrainPane);
		
		AccordianPane object1Pane = new AccordianPane(terrainPane, "Object");
		AccordianPane object2Pane = new AccordianPane(terrainPane, "Object");
		AccordianPane object3Pane = new AccordianPane(terrainPane, "Object");
		terrainPane.addToContent(object1Pane);
		terrainPane.addToContent(object2Pane);
		terrainPane.addToContent(object3Pane);
		
		addToContent(buttonToolbar);
		addToContent(mapPane);
	}
	
}
