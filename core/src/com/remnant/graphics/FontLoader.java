package com.remnant.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.remnant.assets.Texture;

public class FontLoader {
	
	static HashMap<String, BitmapFont> loadedFonts = new HashMap<String, BitmapFont>();
	
	public static BitmapFont getFont(String fontPath){
		
		if(!isFontLoaded(fontPath))
			loadFont(fontPath);
		
		return loadedFonts.get(fontPath);
	}
	
	static void loadFont(String path){
		InputStream stream = ClassLoader.getSystemResourceAsStream(path);
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			
			int lineHeight = 0;
			CharDescriptor[] chars = new CharDescriptor[256];
			Texture baseTexture = null;
			
			String line = "";
			while((line = reader.readLine()) != null){
				
				StringTokenizer st = new StringTokenizer(line, " ");
				String type = st.nextElement().toString();
				
				if(type.equals("common")){
					String element = st.nextElement().toString();
					String[] tokens = element.split("=");
					lineHeight = Integer.parseInt(tokens[1]);
					//System.out.println("Line Height: " + lineHeight);
				}
				
				if(type.equals("page")){
					st.nextElement();
					String element = st.nextElement().toString().trim();
					String[] tokens = element.split("=");
					String texName = tokens[1].substring(1, tokens[1].length() - 1);
					int lios = path.lastIndexOf("/");
					String texPath = path.substring(0, lios + 1) + texName;
					baseTexture = new Texture(texPath);
					//System.out.println(baseTexture);
				}
				
				if(type.equals("char")){
					int idx = parseFromToken(st.nextElement().toString());
					int x = parseFromToken(st.nextElement().toString());
					int y = parseFromToken(st.nextElement().toString());
					int width = parseFromToken(st.nextElement().toString());
					int height = parseFromToken(st.nextElement().toString());
					int xOffset = parseFromToken(st.nextElement().toString());
					int yOffset = parseFromToken(st.nextElement().toString());
					int xAdvance = parseFromToken(st.nextElement().toString());
					
					//the y coordinate is flipped to convert from the bitmap font's texCoord.y to OpenGL's texCoord.y
					
					y = (int) (baseTexture.getHeight() - (y + height));
					
					CharDescriptor cd = new CharDescriptor();
					cd.x = x;
					cd.y = y;
					
					cd.width = width;
					cd.height = height;
					
					cd.xOffset = xOffset;
					cd.yOffset = yOffset;
					
					cd.xAdvance = xAdvance;
					cd.texRegion = new TextureRegion(baseTexture, x, y, width, height);
					
					chars[idx] = cd;
				}
			}
			
			BitmapFont font = new BitmapFont(chars, lineHeight, baseTexture);
			loadedFonts.put(path, font);
			reader.close();
			
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	static int parseFromToken(String token){
		return Integer.parseInt(token.split("=")[1]);
	}
	
	static boolean isFontLoaded(String path){
		return loadedFonts.containsKey(path);
	}
	
}
