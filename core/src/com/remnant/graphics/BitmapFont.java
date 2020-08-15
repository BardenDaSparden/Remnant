package com.remnant.graphics;

import org.joml.Vector4f;

import com.remnant.assets.Texture;

public class BitmapFont {
	
	Vector4f color = new Vector4f(1, 1 ,1, 1);
	
	CharDescriptor[] chars;
	int lineHeight;
	Texture baseTexture;
	
	public BitmapFont(CharDescriptor[] chars, int lineHeight, Texture baseTexture){
		this.chars = chars;
		this.lineHeight = lineHeight;
		this.baseTexture = baseTexture;
	}
	
	public int getWidth(String str){
		int width = 0;
		int n = str.length();
		char[] characters = str.toCharArray();
		for(int i = 0; i < n; i++){
			if(chars[characters[i]] == null)
				continue;
			width += (chars[characters[i]].xAdvance);
		}
		return width;
	}
	
	public int getHeight(String str){
		int height = lineHeight;
		int n = str.length();
		char[] characters = str.toCharArray();
		for(int i = 0; i < n; i++){
			if(characters[i] == '\n'){
				height += lineHeight;
			}
		}
		return height;
	}
	
	public void draw(float startX, float startY, String s, TextureBatch batch){
		draw(startX, startY, s, batch, color);
	}
	
	public void draw(float startX, float startY, String s, TextureBatch batch, Vector4f color){
		float x = startX;
		float y = startY;
		char[] strToChars = s.toCharArray();
		
		for(int i = 0; i < strToChars.length; i++){
			char ch = strToChars[i];
			CharDescriptor cd = chars[ch];
			
			//System.out.println(ch + " " + cd);
			
			if(cd == null)
				continue;
			
			TextureRegion region = cd.texRegion;
			Texture base = region.getBase();
			//base.setFilter(Texture.Filter.NEAREST, Texture.Filter.NEAREST);
			float w = region.getWidth();
			float h = region.getHeight();
			float offsetX = w / 2 + cd.xOffset;
			float offsetY = -h / 2 - cd.yOffset;
			//batch.draw(x + (cd.xOffset + w / 2f), y + (-cd.yOffset - h / 2f) , w, h, 0, 1, 1, region, color);
			batch.draw((x + offsetX), (y + offsetY), w, h, 0, 1, 1, region, color);
			
			x += getAdvanceX(cd, ch);
			y += getAdvanceY(cd, ch);
		}
	}
	
	public void drawCentered(float startX, float startY, String s, TextureBatch batch){
		drawCentered(startX, startY, s, batch, color);
	}
	
	public void drawCentered(float startX, float startY, String s, TextureBatch batch, Vector4f color){
		float offsetX = -getWidth(s) / 2.0f;
		float offsetY = getHeight(s) / 2.0f;
		draw(startX + offsetX, startY + offsetY, s, batch, color);
	}
	
	float getAdvanceX(CharDescriptor cd, char ch){
		float advanceX = 0;
		
		if(ch == ' ')
			advanceX = cd.width + cd.xOffset;
		
		if(ch == '\t')
			advanceX = lineHeight;
		else
			advanceX = cd.xAdvance;
		
		return advanceX;
	}
	
	float getAdvanceY(CharDescriptor cd, char ch){
		float advanceY = 0;
		
		if(ch == '\n')
			advanceY = lineHeight;
		
		return advanceY;
	}
}
