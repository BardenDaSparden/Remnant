package com.remnant.ui;

import java.util.ArrayList;
import java.util.Collections;

import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.graphics.BitmapFont;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.math.Color;

public class UISceneRenderer extends TypedUIRenderer {

	static final int LAYER = 9999;
	
	int width, height;
	Camera camera;
	UIStage stage;
	Texture white;
	
	ArrayList<UINode> nodes = new ArrayList<UINode>();
	ArrayList<Window> windows = new ArrayList<Window>();
	MenuBar menuBar = null;
	
	public UISceneRenderer(int width, int height){
		super(LAYER);
		this.width = width;
		this.height = height;
		this.camera = new Camera(width, height);
		white = new Texture("images/white.png");
	}
	
	public void setStage(UIStage stage){
		this.stage = stage;
	}
	
	void drawNode(UINode node){
		
		if(!node.isVisible)
			return;
		
		UIStyle style = node.getStyle();
		Vector4f borderColor = new Vector4f(style.borderColor);
		Vector4f backgroundColor = new Vector4f(style.backgroundColor);
		Vector4f textColor = new Vector4f(style.textColor);
		if(node.isDisabled){
			borderColor.w = 0.15f;
			backgroundColor.w = 0.15f;
			textColor.w = 0.15f;
		}
		
		if(node instanceof Label){
			
			Label label = (Label)node;
			BitmapFont font = style.getFont();
			
			if(label.alignment == Label.ALIGNMENT_CENTER){
				font.drawCentered(node.getX() + style.getTextShadowOffset().x + 0.5f, node.getY() + style.getTextShadowOffset().y + 0.5f, label.getText(), batch, style.getTextShadowColor());
				font.drawCentered(node.getX() + 1, node.getY() + 1, label.getText(), batch, textColor);
			} else if(label.alignment == Label.ALIGNMENT_LEFT){
				font.draw(node.getX() + style.getTextShadowOffset().x + 0.5f, node.getY() + font.getHeight(label.text) / 2 + style.getTextShadowOffset().y + 0.5f, label.getText(), batch, style.getTextShadowColor());
				font.draw(node.getX() + 1, node.getY() + 1 + font.getHeight(label.text) / 2, label.getText(), batch, textColor);
			}
			
		} else if(node instanceof ImageIcon){
			ImageIcon icon = (ImageIcon) node;
			//batch.begin(BlendState.ALPHA_TRANSPARENCY);
			icon.style.backgroundImg.setFilter(Texture.Filter.NEAREST, Texture.Filter.NEAREST);
				batch.draw(node.getX(), node.getY(), node.getWidth() + 1, node.getHeight() + 1, 0, 1, 1, icon.style.backgroundImg, Color.WHITE);
			//batch.end();
		} else if(node instanceof Window){
			Window window = (Window) node;
			if(!window.isDialog){
				float x = node.getX() + 0.5f;
				float y = node.getY() + 0.5f;
				float w = node.getWidth();
				float h = node.getHeight();
				
				if(style.getBorderSize() > 0){
					batch.draw(x, y + h / 2 + (0.5f * style.getBorderSize()), w, style.getBorderSize(), 0, 1, 1, white, borderColor);
					batch.draw(x, y - h / 2 - (0.5f * style.getBorderSize()), w, style.getBorderSize(), 0, 1, 1, white, borderColor);
					batch.draw(x - (w / 2) - (0.5f * style.getBorderSize()), y, style.getBorderSize() * 1.0f, h + (style.getBorderSize() * 1.000f), 0, 1, 1, white, borderColor);
					batch.draw(x + (w / 2) + (0.5f * style.getBorderSize()), y, style.getBorderSize() * 1.0f, h + (style.getBorderSize() * 1.000f), 0, 1, 1, white, borderColor);
				}
				
				Texture t = style.backgroundImg;
				if(t != null)
					batch.draw(x, y, w , h, 0, 1, 1, t, backgroundColor);
				 else 
					batch.draw(x, y, w , h, 0, 1, 1, white, backgroundColor);
					
			}
		} else {
			
			float x = node.getX() + 0.5f;
			float y = node.getY() + 0.5f;
			float w = node.getWidth();
			float h = node.getHeight();
			
			if(style.getBorderSize() > 0){
				batch.draw(x, y + h / 2 + (0.5f * style.getBorderSize()), w, style.getBorderSize(), 0, 1, 1, white, borderColor);
				batch.draw(x, y - h / 2 - (0.5f * style.getBorderSize()), w, style.getBorderSize(), 0, 1, 1, white, borderColor);
				batch.draw(x - (w / 2) - (0.5f * style.getBorderSize()), y, style.getBorderSize() * 1.0f, h + (style.getBorderSize() * 1.000f), 0, 1, 1, white, borderColor);
				batch.draw(x + (w / 2) + (0.5f * style.getBorderSize()), y, style.getBorderSize() * 1.0f, h + (style.getBorderSize() * 1.000f), 0, 1, 1, white, borderColor);
			}
			
			Texture t = style.backgroundImg;
			if(t != null)
				batch.draw(x, y, w , h, 0, 1, 1, t, backgroundColor);
			 else 
				batch.draw(x, y, w , h, 0, 1, 1, white, backgroundColor);
				
		}
	}
	
	void drawPanes(){
		Collections.sort(windows);
		for(int i = 0; i < windows.size(); i++){
			Window pane = windows.get(i);
			if(!pane.isDialog)
				traversePane(pane);
		}
		windows.clear();
	}
	
	void drawMenuBar(){
		if(menuBar != null)
			traverseMenuBar(menuBar);
	}
	
	void drawDialogs(){
		Vector4f overlayColor = new Vector4f(0, 0, 0, 0.75f);
		batch.draw(0, 0, width, height, 0, 1, 1, white, overlayColor);
		Collections.sort(stage.activeDialogs);
		for(int i = 0; i < stage.activeDialogs.size(); i++){
			Window dialog = stage.activeDialogs.get(i);
			traversePane(dialog);
		}
	}
	
	
	void traverseMenuBar(MenuBar menuBar){
		if(menuBar.isParent()){
			drawNode(menuBar);
			for(int i = 0; i < menuBar.getChildren().size(); i++){
				UINode child = menuBar.getChildren().get(i);
				traverse(child);
			}
		} else {
			drawNode(menuBar);
		}
	}
	
	void traversePane(Window pane){
		if(pane.isParent()){
			drawNode(pane);
			for(int i = 0; i < pane.getChildren().size(); i++){
				UINode child = pane.getChildren().get(i);
				traverse(child);
			}
		} else {
			drawNode(pane);
		}
	}
	
	void traverse(UINode node){
		
		if(node instanceof Window){
			Window pane = (Window) node;
			windows.add(pane);
		} else if(node instanceof MenuBar){
			menuBar = (MenuBar) node;
		} else {
			if(node.isParent()){
				drawNode(node);
				for(int i = 0; i < node.getChildren().size(); i++){
					UINode child = node.getChildren().get(i);
					traverse(child);
				}
			} else {
				drawNode(node);
			}
		}
	}
	
	@Override
	public void draw() {
		cameraStack.push();
		cameraStack.current().set(camera);
			if(stage != null){
				batch.begin(BlendState.ALPHA_TRANSPARENCY);
					traverse(stage.getRoot());
					drawPanes();
					drawMenuBar();
					if(stage.isDialogOpen()){
						drawDialogs();
					}
				batch.end();
			}
		cameraStack.pop();
	}
} 