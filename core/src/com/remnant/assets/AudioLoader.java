package com.remnant.assets;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryStack;

public class AudioLoader {

	private HashMap<String, AudioClip> audio;
	
	public void create(){
		audio = new HashMap<String, AudioClip>();
	}
	
	private AudioClip loadNew(String filename){
		
		ShortBuffer audioBuffer = null;
		int sourcePointer = -1;
		int bufferPointer = -1;
		int channels = 0;
		int sampleRate = 0;
		
		try(MemoryStack stack = stackPush()){
			IntBuffer channelBuffer = stack.mallocInt(1);
			IntBuffer sampleRateBuffer = stack.mallocInt(1);
			
			audioBuffer = stb_vorbis_decode_filename(filename, channelBuffer, sampleRateBuffer);
			
			channels = channelBuffer.get(0);
			sampleRate = sampleRateBuffer.get(0);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		int format = -1;
		if(channels == 1)
			format = AL_FORMAT_MONO16;
		else if(channels == 2)
			format = AL_FORMAT_STEREO16;
		
		bufferPointer = alGenBuffers();
		alBufferData(bufferPointer, format, audioBuffer, sampleRate);
		free(audioBuffer);
		
		sourcePointer = alGenSources();
		alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
		
		AudioClip sound = new AudioClip(sourcePointer, bufferPointer, channels, sampleRate);
		audio.put(filename, sound);
		return sound;
	}
	
	private boolean contains(String filename){
		return audio.containsKey(filename);
	}
	
	public AudioClip load(String filename){
		if(contains(filename)) {
			System.out.println(filename + " is loaded already!");
			return audio.get(filename);
		} else { 
			System.out.println(filename + " is being loaded!");
			return loadNew(filename);
		}
	}
	
	public void unload(AudioClip sound){
		sound.dispose();
		audio.remove(sound.filename);
	}
	
	public void unloadAll(){
		for(Map.Entry<String, AudioClip> entry : audio.entrySet()){
			AudioClip sound = entry.getValue();
			sound.dispose();
		}
		audio.clear();
	}
	
	public void dispose(){
		unloadAll();
	}
}
