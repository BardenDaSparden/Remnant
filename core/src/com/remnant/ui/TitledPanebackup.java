//package com.remnant.ui;
//
//package com.remnant.ui;
//
//import java.util.ArrayList;
//
//import org.joml.Vector4f;
//
//import com.remnant.graphics.FontLoader;
//
//public class TitledPane extends AnchorPane {
//	
//	final Vector4f BACKGROUND_COLOR = new Vector4f(0.18f, 0.18f, 0.18f, 1f);
//	final Vector4f TITLE_COLOR = new Vector4f(0.18f, 0.18f, 0.18f, 1);
//	final Vector4f TEXT_COLOR = new Vector4f(0.74f, 0.74f, 0.74f, 1);
//	
//	String titleStr;
//	Label title;
//	AnchorPane titleContainer;
//	VBox content;
//	
//	public TitledPane(String titleStr){
//		this.titleStr = titleStr;
//		style.backgroundColor.set(0, 0, 0, 0.0f);
//		setPercentSize(0.5f, 0.5f);
//		setMinSize(250, 0);
//		setSizeMode(PERCENT, PERCENT);
//		
//		UIStyle btnStyle = new UIStyle();
//		btnStyle.backgroundColor.set(0, 0, 0, 0.15f);
//		btnStyle.textColor.set(1, 1, 1, 0.2f);
//		btnStyle.font = FontLoader.getFont("fonts/imagine_24.fnt");
//		
//		UIStyle hoverStyle = new UIStyle();
//		hoverStyle.backgroundColor.set(1, 1, 1, 0.05f);
//		hoverStyle.textColor.set(1, 1, 1, 0.2f);
//		hoverStyle.font = FontLoader.getFont("fonts/imagine_24.fnt");
//		
//		title = new Label(titleStr);
//		title.style.textColor.set(TEXT_COLOR);
//		title.style.font = FontLoader.getFont("fonts/open_sans_16.fnt");
//		title.setText(titleStr);
//		
//		titleContainer = new AnchorPane();
//		titleContainer.style.backgroundColor.set(TITLE_COLOR);
//		titleContainer.setPercentSize(1, 0.1f);
//		titleContainer.setPrefSize(0, 36);
//		titleContainer.setSizeMode(PERCENT, PREFERRED);
//		titleContainer.add(title, Anchor.CENTER);
//		add(titleContainer, Anchor.TOP);
//		
//		content = new VBox();
//		content.style.backgroundColor.set(BACKGROUND_COLOR);
//		content.setPercentSize(1f, 0.8f);
//		content.setSizeMode(PERCENT, PERCENT);
//		content.alignment = VBox.ALIGNMENT_TOP;
//		add(content, Anchor.BOTTOM);
//		
//	}
//	
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
//	
//	
//	public void addToContent(UINode node){
//		content.add(node);
//	}
//	
//}
//
