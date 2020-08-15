package com.remnant.assets;

import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;

import java.io.FileNotFoundException;

public class SoundClip
{
	private int source;
	private int data;
	
	public SoundClip(String path){
		data = alGenBuffers();
		source = alGenSources();
			//WaveData soundData = null;
		
		//alBufferData(data, soundData.format, soundData.data, soundData.samplerate);
		//alSourcei(source, AL_BUFFER, data);
	}
	
	public void play(){
		play(false);
	}
	
	public void play(boolean looping){
		int boolToInt = (looping) ? AL_TRUE : AL_FALSE;
		//alSourcei(source, AL_LOOPING, boolToInt);
		//alSourcePlay(source);
	}
	
	public void stop(){
		//alSourceStop(source);
	}
	
	public void setGain(float gain){
		if(gain > 1.0f)
			gain = 1.0f;
		if(gain < 0.0f)
			gain = 0.0f;
		//alSourcef(source, AL_GAIN, gain);
	}
}
