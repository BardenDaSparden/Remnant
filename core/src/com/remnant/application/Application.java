package com.remnant.application;

import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.barden.util.Debug;
import org.barden.util.GPUProfiler;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;

import com.remnant.input.Input;

public abstract class Application {
	
	protected ApplicationProperties mSettings;
	protected Input mInput;
	long mWindow;
	GLFWErrorCallback errorCB;
	boolean isRunning = true;
	
	public Application(){
		mSettings = new ApplicationProperties();
		mInput = null;
		mWindow = -1L;
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public abstract void load();
	public abstract void update();
	public abstract void render(double alpha);
	public abstract void unload();
	
	public ApplicationProperties getProperties(){
		return mSettings;
	}
	
	public Input getInputSystem(){
		return mInput;
	}
	
	protected static void launch(String[] args, Application application){
		glfwSetErrorCallback(application.errorCB = GLFWErrorCallback.createPrint(System.err));
		
		ApplicationProperties settings = application.mSettings;
		long window = application.mWindow;
		
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DECORATED, (settings.isBorderless()) ? GLFW_FALSE : GLFW_TRUE);
		
		long fullscreen = (settings.isFullscreen()) ? glfwGetPrimaryMonitor() : 0L;
		
		window = glfwCreateWindow(settings.getWidth(), settings.getHeight(), settings.getTitle(), fullscreen, 0L);
		if(window == 0L)
			throw new RuntimeException("Failed to create GLFW window");
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
//		if(settings.isVSync())
//			glfwSwapInterval(1);
		
		
		application.mInput = new Input();
		application.mInput.register(window);
		
		long device = alcOpenDevice((ByteBuffer)null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		long context = alcCreateContext(device, (IntBuffer) null);
		alcSetThreadContext(context);
		
		AL.createCapabilities(deviceCaps);
		GL.createCapabilities();
		
		long previousTime = System.nanoTime();
		long currentTime = previousTime;
		long unprocessedUpdateTime = 0;
		final long UPDATE_STEP = (long)(1000000000D / 60.0D);
		final long MAX_UPDATE_LENGTH = (long)((1000000000D / 60.0D) * 3);
		final int MAX_UPDATES_PER_RENDER = 4;
		int updates = 0;
		
		application.load();
		
		Debug.print();
		
		while(!glfwWindowShouldClose(window) && application.isRunning){
			previousTime = currentTime;
			currentTime = System.nanoTime();
			unprocessedUpdateTime += (currentTime - previousTime);
			
			if(unprocessedUpdateTime > MAX_UPDATE_LENGTH)
				unprocessedUpdateTime = MAX_UPDATE_LENGTH;
			
			updates = 0;
			while(unprocessedUpdateTime >= UPDATE_STEP && updates < MAX_UPDATES_PER_RENDER){
				unprocessedUpdateTime -= UPDATE_STEP;
				application.update();
				updates++;
			}
			application.mInput.update();
			
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
				double alpha = 1.0 - ((double)unprocessedUpdateTime / (double)UPDATE_STEP);
				application.render(alpha);				
			
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		
		application.mInput.unregister();
		application.unload();
		
        alcDestroyContext(context);
        alcCloseDevice(device);
		glfwTerminate();
	}
	
}
