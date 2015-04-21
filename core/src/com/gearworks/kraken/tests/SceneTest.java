package com.gearworks.kraken.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.gearworks.kraken.Kraken;
import com.gearworks.kraken.input.InputHandler;
import com.gearworks.kraken.input.KeyboardEvent;
import com.gearworks.kraken.input.MouseEvent;
import com.gearworks.kraken.scene.Geometry;
import com.gearworks.kraken.scene.Renderer;
import com.gearworks.kraken.scene.Spatial;
import com.gearworks.kraken.utils.Behavior;
import com.gearworks.kraken.utils.State;

public class SceneTest extends TestState{
	private boolean mouseDown;
	private int direction = 1;
	private MouseEvent mouseDownEvt;
	private MouseEvent mouseUpEvt;
	private MouseEvent mouseScrollEvt;
	private KeyboardEvent spaceUpEvt;

	@Override
	public void onEnter() {
		System.out.println("Enter SceneTest");
		
		/**
		 * SPATIAL TESTS
		 */
		//Spatial validation
		Spatial test = new Spatial();
		assert !test.isValid();
		test.validate();
		assert test.isValid();
		test.setLocalTranslation(1, 1, 1);
		assert !test.isValid();
		assert test.getLocalTranslation().equals(new Vector3(1, 1, 1));
		System.out.println("\tSpatial validates properly.");
		
		
		ModelBuilder builder = new ModelBuilder();
		Model boxModel = builder.createBox(1, 1, 1, new Material(), VertexAttributes.Usage.Position);
		Geometry geom = new Geometry(){
			@Override
			public void update(){
				Quaternion rotation = new Quaternion();
				rotation.setFromAxis(1, 1, 0, 1);
				rotation = getLocalRotation().mul(rotation);
				setLocalRotation(rotation);
			}
		};
		geom.setModelInstance(new ModelInstance(boxModel));
		Renderer.getRoot().attach(geom);
		Renderer.getRoot().setLocalRotation(new Quaternion().setFromAxis(0, 1, 0, 45));
		Renderer.getRoot().setLocalTranslation(1, 0, 0);
		geom.setLocalRotation(new Quaternion().setFromAxis(1, 0, 0, 45));
		
		mouseDownEvt = InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_DOWN){
			@Override
			public boolean callback(){
				mouseDown = true;
				direction = (this.button == 1) ? 1 : -1;
				return false;
			}
		});
		
		mouseUpEvt = InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_UP){
			@Override
			public boolean callback(){
				mouseDown = false;
				return false;
			}
		});
		
		mouseScrollEvt = InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_SCROLL){
			@Override
			public boolean callback(){
				if(delta > 0){
					Renderer.camera().translate(Renderer.camera().direction.cpy().scl(-1.5f));
				}else{
					Renderer.camera().translate(Renderer.camera().direction.cpy().scl(1.5f));		
				}
				return false;
			}
		});
		
		spaceUpEvt = InputHandler.addListener(new KeyboardEvent(KeyboardEvent.KEY_UP, Input.Keys.SPACE){
			@Override
			public boolean callback(){
				done = true;
				return false;
			}
		});
	}

	@Override
	public void onExit() {
		System.out.println("Exit scene test");	
		Renderer.getRoot().clear();
		InputHandler.removeListener(mouseDownEvt);
		InputHandler.removeListener(mouseUpEvt);
		InputHandler.removeListener(mouseScrollEvt);
		InputHandler.removeListener(spaceUpEvt);
	}
	
	@Override
	public void update() {
		if(mouseDown){
			Renderer.camera().rotateAround(new Vector3(), Vector3.Y, direction * 1f);
		}
	}
}
