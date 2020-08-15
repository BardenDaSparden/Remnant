package com.remnant.assets;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_UNSIGNED_INT_24_8;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.barden.util.Debug;
import org.barden.util.GLVersion;
import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture {
	
	public enum Format{
		DEPTH_STENCIL(GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8), DEPTH_INT_24(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT), RED_FLOAT_16(GL_R16F, GL_RED, GL_FLOAT), RED_FLOAT_32(GL_R32F, GL_RED, GL_FLOAT), RGB_BYTE_8(GL_RGB8, GL_RGB, GL_UNSIGNED_BYTE), RGBA_BYTE_8(GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE), RGBA_FLOAT_16(GL_RGBA16F, GL_RGBA, GL_FLOAT), RGBA_FLOAT_32(GL_RGBA32F, GL_RGBA, GL_FLOAT), RGB_FLOAT_32(GL_RGB32F, GL_RGB, GL_FLOAT);
		
		public int internalFormat;
		public int format;
		public int type;
		
		Format(int internalFormat, int format, int type){
			this.internalFormat = internalFormat;
			this.format = format;
			this.type = type;
		}
		
	}
	
	public enum Filter{
		NEAREST(GL_NEAREST), LINEAR(GL_LINEAR), TRILINEAR(GL_LINEAR_MIPMAP_LINEAR);
		
		int filterType;
		Filter(int filterType){
			this.filterType = filterType;
		}
		
		int getFilterType(){
			return filterType;
		}
		
	}
	
	public enum Wrap{
		CLAMP(GL_CLAMP_TO_EDGE), REPEAT(GL_REPEAT);
		
		int clampType;
		Wrap(int clampType){
			this.clampType = clampType;
		}
		
		int getWrapType(){
			return clampType;
		}
	}
	
	/**
	 * Hash map used for keeping track of textures that have been loaded from a file, to avoid redundant loading of textures
	 */
	private static ConcurrentHashMap<String, Texture> loadedTextures = new ConcurrentHashMap<String, Texture>();
	
	/**
	 * Integer representing the texture in OpenGL
	 */
	private int textureHandle = -1;
	
	/**
	 * Width of the texture in pixels
	 */
	private int width;
	
	/**
	 * Height of the texture in pixels
	 */
	private int height;
	
	/**
	 * The current OpenGL texture format, as well as the associated data-type
	 */
	private Format format;
	
	/**
	 * The current OpenGL minification mode
	 */
	private Filter filterMinification;
	
	/**
	 * The current OpenGL magnification mode
	 */
	private Filter filterMagnification;
	
	/**
	 * The current OpenGL wrap mode
	 */
	private Wrap wrap = Wrap.CLAMP;
	
	/**
	 * String that represents the file location relative to the folder "assets" in the class-path
	 */
	private String filePath = null;
	
	public Texture(String filePath){
		this.filePath = filePath;
		
		//If our texture is already loaded
		if(loadedTextures.containsKey(filePath)){
			Texture loadedTexture = loadedTextures.get(filePath);
			this.textureHandle = loadedTexture.textureHandle;
			this.width = loadedTexture.width;
			this.height = loadedTexture.height;
			this.format = loadedTexture.format;
			this.filterMinification = loadedTexture.filterMinification;
			this.filterMagnification = loadedTexture.filterMagnification;
		} else {
			load();
		}
	}
	
	public Texture(int width, int height, Format format){
		this(width, height, format, Filter.LINEAR, Filter.LINEAR);
	}
	
	public Texture(int width, int height, Format format, Filter minFilter, Filter magFilter){
		this.textureHandle = glGenTextures();
		this.width = width;
		this.height = height;
		this.filterMinification = minFilter;
		this.filterMagnification = magFilter;
		this.format = format;
		
		int internalFormat = format.internalFormat;
		int _format = format.format;
		int type = format.type;
		
		glBindTexture(GL_TEXTURE_2D, textureHandle);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap.getWrapType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap.getWrapType());
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter.getFilterType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter.getFilterType());
		
		if(type == GL_FLOAT){
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, _format, type, (FloatBuffer)null);
		} else if(type == GL_UNSIGNED_BYTE){
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, _format, type, (ByteBuffer)null);
		} else if(type == GL_UNSIGNED_INT_24_8){
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, _format, type, (IntBuffer)null);
		} else if(type == GL_UNSIGNED_INT){
			glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, _format, type, (IntBuffer)null);
		}
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		//Debug.logInfo("Resources", "Texture -> Create -> " + "[" + width + "][" + height + "][" + format.toString() + "]");
	}
	
	public void bind(int textureUnit){
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		glBindTexture(GL_TEXTURE_2D, textureHandle);
	}
	
	public void load(){
		InputStream stream = ClassLoader.getSystemResourceAsStream(filePath);
		PNGDecoder decoder = null;
		
		try {
			decoder = new PNGDecoder(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.width = decoder.getWidth();
		this.height = decoder.getHeight();
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		
		try {
			decoder.decodeFlipped(buffer, width * 4, de.matthiasmann.twl.utils.PNGDecoder.Format.RGBA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buffer.flip();
		
		textureHandle = glGenTextures();
		
		boolean anisotropicSupported = GLVersion.isExtensionSupported("GL_EXT_texture_filter_anisotropic");
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureHandle);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, Wrap.CLAMP.getWrapType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, Wrap.CLAMP.getWrapType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, Filter.LINEAR.getFilterType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, Filter.LINEAR.getFilterType());
//		if(anisotropicSupported){
//			float anisoMax = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
//			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoMax);
//		}
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		this.filterMinification = Filter.LINEAR;
		this.filterMagnification = Filter.LINEAR;
		this.wrap = Wrap.CLAMP;
		
		int li = filePath.lastIndexOf('/');
		int li2 = filePath.lastIndexOf('.');
		String textureName = filePath.substring(li + 1, li2);
		
		//Debug.logInfo("Resources", "Texture -> Load -> " + textureName);
		
		loadedTextures.put(filePath, this);
	}
	
	public void delete(){
		glDeleteTextures(textureHandle);
		
		if(filePath != null)
			loadedTextures.remove(filePath);
	}
	
	public static void deleteAll(){
//		for(Map.Entry<String, Texture> entry : loadedTextures.entrySet()){
//			Texture texture = entry.getValue();
//			texture.delete();
//		}
		
		Iterator<Map.Entry<String, Texture>> it = loadedTextures.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Texture> item = (Map.Entry<String, Texture>) it.next();
			item.getValue().delete();
		}
		
		loadedTextures.clear();
	}
	
	public Texture setFilter(Filter minFilter, Filter magFilter){
		glBindTexture(GL_TEXTURE_2D, textureHandle);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter.getFilterType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter.getFilterType());
		glBindTexture(GL_TEXTURE_2D, 0);
		return this;
	}
	
	public Texture setWrap(Wrap wrap){
		glBindTexture(GL_TEXTURE_2D, textureHandle);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap.getWrapType());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap.getWrapType());
		glBindTexture(GL_TEXTURE_2D, 0);
		return this;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getHandle(){
		return textureHandle;
	}
	
	public void release(){
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getFilePath(){
		return filePath;
	}
	
}
