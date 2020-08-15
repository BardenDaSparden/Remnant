package com.remnant.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.barden.util.Debug;

import com.remnant.assets.Texture;
import com.remnant.assets.Texture.Format;

public class Framebuffer {
	
	protected int width;
	protected int height;
	protected Texture colorAttachment;
	protected Texture depthAttachment;
	protected int renderbufferHandle;
	protected int framebufferHandle;
	
	public Framebuffer(int width, int height){
		this(width, height, Format.RGBA_BYTE_8);
	}
	
	public Framebuffer(int width, int height, Format format){
		this(width, height, format, false);
	}
	
	public Framebuffer(int width, int height, Format format, boolean generateDepthTexture){
		this.width = width;
		this.height = height;
		this.colorAttachment = new Texture(width, height, format);
		this.depthAttachment = new Texture(width, height, Format.DEPTH_STENCIL);
		this.framebufferHandle = 0;
		load();
	}
	
	public void bind(){
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferHandle);
	}
	
	public void bindForWrite(){
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebufferHandle);
	}
	
	public void clear(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}
	
	public void clear(float r, float g, float b, float a){
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(0, 0, 0, 0);
	}
	
	private void load(){
		framebufferHandle = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferHandle);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthAttachment.getHandle(), 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorAttachment.getHandle(), 0);
		//Debug.logInfo("Graphics -> Framebuffer LOAD: ", "Framebuffer completeness returned: " + (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE));
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void destroy(){
		glDeleteFramebuffers(framebufferHandle);
	}
	
	public Texture getColorTexture(){
		return colorAttachment;
	}
	
	public Texture getDepthTexture(){
		return depthAttachment;
	}
	
	public void release(){
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glPopAttrib();
	}
	
}
