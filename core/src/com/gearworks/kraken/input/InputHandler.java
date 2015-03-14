package com.gearworks.kraken.input;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gearworks.kraken.scene.Renderer;

/**
 * Handles game input
 */
public class InputHandler implements InputProcessor{
	private static InputHandler instance = new InputHandler();
	
	private ArrayList<KeyboardEvent> keyboardListeners;
	private ArrayList<MouseEvent> mouseListeners;
	
	public InputHandler(){
		keyboardListeners = new ArrayList<KeyboardEvent>();
		mouseListeners = new ArrayList<MouseEvent>();
	}
	
	public static KeyboardEvent addListener(KeyboardEvent event){
		instance.keyboardListeners.add(event);
		return event;
	}
	
	public static MouseEvent addListener(MouseEvent event){
		instance.mouseListeners.add(event);
		return event;
	}
	
	public static void removeListener(KeyboardEvent event){
		instance.keyboardListeners.remove(event);
	}
	
	public static void removeListener(MouseEvent event){
		instance.mouseListeners.remove(event);
	}
	
	private boolean dispatchKeyboardEvent(KeyboardEvent cmp){
		for(KeyboardEvent evt : keyboardListeners){
			if(cmp.equals(evt)){
				if(evt.callback()){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean dispatchMouseEvent(MouseEvent cmp){
		for(MouseEvent evt : mouseListeners){
			if(cmp.equals(evt)){
				evt.button = cmp.button;
				evt.delta = cmp.delta;
				evt.mousePosition = cmp.mousePosition;
				evt.mouseScreenPosition = cmp.mouseScreenPosition;
				if(evt.callback()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Takes mouse coordinates and converts them into game (window) coordinates
	 * 
	 * @param coord - Mouse coordinates
	 * @return window coordinates
	 */
	private Vector2 screenCoordsToGame(Vector2 coord){
		Vector3 screenCoord = new Vector3(coord.x, coord.y, 0);
		Renderer.camera().unproject(screenCoord);
		return new Vector2(screenCoord.x, screenCoord.y);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		KeyboardEvent cmp = new KeyboardEvent(KeyboardEvent.KEY_DOWN, keycode);
		return dispatchKeyboardEvent(cmp);
	}
	@Override
	public boolean keyUp(int keycode) {
		KeyboardEvent cmp = new KeyboardEvent(KeyboardEvent.KEY_UP, keycode);
		return dispatchKeyboardEvent(cmp);
	}
	@Override
	public boolean keyTyped(char character) {
		//Unused
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
		MouseEvent cmp = new MouseEvent(MouseEvent.MOUSE_DOWN, button, screenCoordsToGame(mouseScreenPosition), mouseScreenPosition, 0);
		return dispatchMouseEvent(cmp);
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
		MouseEvent cmp = new MouseEvent(MouseEvent.MOUSE_UP, button, screenCoordsToGame(mouseScreenPosition), mouseScreenPosition, 0);
		return dispatchMouseEvent(cmp);
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
		MouseEvent cmp = new MouseEvent(MouseEvent.MOUSE_MOVE, -1, screenCoordsToGame(mouseScreenPosition), mouseScreenPosition, 0);
		return dispatchMouseEvent(cmp);
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
		MouseEvent cmp = new MouseEvent(MouseEvent.MOUSE_MOVE, -1, screenCoordsToGame(mouseScreenPosition), mouseScreenPosition, 0);
		return dispatchMouseEvent(cmp);
	}
	@Override
	public boolean scrolled(int amount) {
		MouseEvent cmp = new MouseEvent(MouseEvent.MOUSE_SCROLL, -1, null, null, amount);
		return dispatchMouseEvent(cmp);
	}

	public static InputProcessor get() {
		return instance;
	}
	
	

}
