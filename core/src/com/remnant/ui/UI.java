package com.remnant.ui;

import java.util.ArrayList;

import com.remnant.input.Input;

public class UI {

	Input input;
	ArrayList<UIStage> stages;
	int current;
	
	public UI(Input input){
		this.input = input;
		stages = new ArrayList<UIStage>(10);
		current = -1;
	}
	
	public void update(){
		UIStage current = current();
		if(current == null)
			return;
		current.update();
	}
	
	public void push(UIStage stage){
		if(current() != stage){
			current++;
			stages.add(current, stage);
			if(stage != null)
				stage.setInput(input);
		}
		
	}
	
	public UIStage current(){
		if(current < 0 || current > stages.size())
			return null;
		return stages.get(current);
	}
	
	public void pop(){
		current--;
	}
	
	public void clear(){
		stages.clear();
		current = -1;
	}
}
