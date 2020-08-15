package com.remnant.assets;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;

public class AudioClip {

	String filename;
	
	int sourcePointer;
	
	int bufferPointer;
	
	int channels;
	
	int sampleRate;
	
	protected AudioClip(int source, int buffer, int channels, int sampleRate){
		this.sourcePointer = source;
		this.bufferPointer = buffer;
		this.channels = channels;
		this.sampleRate = sampleRate;
	}
	
	public AudioClip(AudioClip sound){
		this.sourcePointer = sound.sourcePointer;
		this.bufferPointer = sound.bufferPointer;
		this.channels = sound.channels;
		this.sampleRate = sound.sampleRate;
	}
	
	public void setGain(float g){
		alSourcef(sourcePointer, AL_GAIN, g);
	}
	
	public void setLooping(boolean loop){
		int looping = (loop) ? AL_TRUE : AL_FALSE;
		alSourcei(sourcePointer, AL_LOOPING, looping);
	}
	
	public void play(){
		alSourcePlay(sourcePointer);
	}
	
	public void stop(){
		alSourceStop(sourcePointer);
	}
	
	public void dispose(){
		alDeleteSources(sourcePointer);
		alDeleteBuffers(bufferPointer);
	}
	
}
