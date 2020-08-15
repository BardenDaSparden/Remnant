package com.remnant.assets;

public class AssetLoader {

	private AudioLoader audio;
	private TextureLoader textures;
	
	public void create(){
		audio = new AudioLoader();
		textures = new TextureLoader();
		
		audio.create();
		textures.create();
	}
	
	public AudioClip loadAudio(String filename){
		return audio.load(filename);
	}
	
	public void unloadAudio(AudioClip sound){
		audio.unload(sound);
	}
	
	public Texture loadTexture(String filename){
		return textures.load(filename);
	}
	
	public void unloadTexture(Texture texture){
		textures.unload(texture);
	}
	
	public void unloadAllAudio(){
		audio.unloadAll();
	}
	
	public void unloadAllTextures(){
		textures.unloadAll();
	}
	
	public void unloadAll(){
		unloadAllAudio();
		unloadAllTextures();
	}
	
	public void dispose(){
		audio.dispose();
		textures.dispose();
		Texture.deleteAll();
	}
	
}
