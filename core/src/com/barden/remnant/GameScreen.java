package com.barden.remnant;

import com.badlogic.gdx.Screen;

public abstract class GameScreen implements Screen {

	protected Remnant game;
	
	public GameScreen(Remnant game){
		this.game = game;
	}
	
	public abstract void show();
	public abstract void hide();
	public abstract void update();
	public abstract void render(float delta);

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
