package com.remnant.graphics;

import java.nio.IntBuffer;

import org.barden.util.Debug;
import org.barden.util.GLError;
import org.lwjgl.BufferUtils;

import com.remnant.assets.Texture;
import com.remnant.assets.Texture.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class MultiFramebuffer {

	int width;
	int height;
	Format[] colorFormats;
	Texture depthAttachment;
	Texture[] attachments;
	IntBuffer drawBuffers;
	int framebuffer;
	
	public MultiFramebuffer(int width, int height, Format...colors){
		this.width = width;
		this.height = height;
		colorFormats = colors;
		
		final int NUM_ATTACHMENTS = colors.length;
		final Format DEPTH_FORMAT = Format.DEPTH_STENCIL;
		depthAttachment = new Texture(width, height, DEPTH_FORMAT);
		attachments = new Texture[NUM_ATTACHMENTS];
		drawBuffers = BufferUtils.createIntBuffer(NUM_ATTACHMENTS);
		for(int i = 0; i < NUM_ATTACHMENTS; i++){
			attachments[i] = new Texture(width, height, colors[i]);
			drawBuffers.put(GL_COLOR_ATTACHMENT0 + i);
		}
		drawBuffers.flip();
		
		framebuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthAttachment.getHandle(), 0);
			for(int i = 0; i < NUM_ATTACHMENTS; i++){
				Texture attachment = attachments[i];
				int drawBuffer = drawBuffers.get(i);
				glFramebufferTexture2D(GL_FRAMEBUFFER, drawBuffer, GL_TEXTURE_2D, attachment.getHandle(), 0);
			}
			glDrawBuffers(drawBuffers);
			Debug.logInfo("MultiFramebuffer Error: ", GLError.checkFramebuffer());
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
	}
	
	public void bind(){
		glPushAttrib(GL_VIEWPORT);
		glViewport(0, 0, width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
	}
	
	public void clear(){
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}
	
	public void release(){
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glPopAttrib();
	}
	
	public Texture getDepthAttachment(){
		return depthAttachment;
	}
	
	public Texture getColorAttachment(int attachmentIdx){
		return attachments[attachmentIdx];
	}
	
}
