package com.gearworks.kraken.input;

import com.badlogic.gdx.math.Vector2;

public class MouseEvent {
	public static final int MOUSE_DOWN = 1;
	public static final int MOUSE_UP = 2;
	public static final int MOUSE_SCROLL = 3;
	public static final int MOUSE_MOVE = 4;
	
	public int event;
	public int button; //Used for up/down event
	public Vector2 mousePosition; //Used for up/down/move event
	public Vector2 mouseScreenPosition; //Used for up/down/move event
	public int delta; //Used for scroll events
	
	public MouseEvent(int event){
		this.event = event;
	}
	public MouseEvent(int event, int button, Vector2 position, Vector2 mouseScreenPosition, int delta){
		this.event = event;
		this.button = button;
		this.mousePosition = position;
		this.mouseScreenPosition = mouseScreenPosition;
		this.delta = delta;
	}
	
	public MouseEvent(){
		event = 0;
		button = -1;
		mousePosition = null;
		mouseScreenPosition = null;
		delta = 0;
	}
	
	public boolean callback(){ return false; }
	
	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		
		MouseEvent evt = (MouseEvent)other;
		if(evt == null) return false; //Eclipse is complaining this is dead code, thats not true if other is not an instance of MouseEvent
		
		return (event == evt.event); //Just want to match the events here
	}
	
}
