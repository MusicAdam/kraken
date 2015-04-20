package com.gearworks.kraken;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.input.InputHandler;
import com.gearworks.kraken.input.MouseEvent;
import com.gearworks.kraken.scene.Geometry;
import com.gearworks.kraken.scene.Node;
import com.gearworks.kraken.scene.Renderer;
import com.gearworks.kraken.scene.Spatial;


public class Kraken extends ApplicationAdapter{
	public static final Vector3 WORLD_SIZE = new Vector3(10, 5, 5);
	private boolean mouseDown;
	private int direction = 1;
	Ray ray;
	
	@Override
	public void create(){
		Renderer.init();
		
		ModelBuilder builder = new ModelBuilder();
		Model boxModel = builder.createBox(1, 1, 1, new Material(), VertexAttributes.Usage.Position);
		Geometry test = new Geometry(){
			@Override
			public void update(){
				Quaternion rotation = new Quaternion();
				rotation.setFromAxis(1, 1, 0, 1);
				rotation = getLocalRotation().mul(rotation);
				setLocalRotation(rotation);
			}
		};
		test.setModelInstance(new ModelInstance(boxModel));
		Renderer.getRoot().attach(test);
		Renderer.getRoot().setLocalRotation(new Quaternion().setFromAxis(0, 1, 0, 45));
		Renderer.getRoot().setLocalTranslation(1, 0, 0);
		test.setLocalRotation(new Quaternion().setFromAxis(1, 0, 0, 45));
		
		/*
		Node test = new Node("test root");
		test.setLocalTranslation(0, 0, 0);
		
		Random rand = new Random();
		for(int i = 0; i < 10; i++){
			float x = (rand.nextFloat() * WORLD_SIZE.x) - WORLD_SIZE.x/2;
			float y = (rand.nextFloat() * WORLD_SIZE.y) - WORLD_SIZE.y/2;
			float z = (rand.nextFloat() * WORLD_SIZE.z) - WORLD_SIZE.z/2;
			Cube square = new Cube();
			square.setLocalTranslation(x, y, z);
			square.setSize(new Vector3(.5f, .5f, .5f));
			Renderer.getRoot().attach(square);
		}
		Cube square = new Cube(){
			@Override
			public void update(){
				System.out.println("HJERE");
				setLocalTranslation(getLocalTranslation().add(.1f, 0, 0));
			}
		};
		square.setSize(new Vector3(.5f, .5f, .5f));
		test.attach(square);
		Renderer.getRoot().attach(test);		
		*/
		
		Gdx.input.setInputProcessor(InputHandler.get());
		
		InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_DOWN){
			@Override
			public boolean callback(){
				mouseDown = true;
				direction = (this.button == 1) ? 1 : -1;
				return false;
			}
		});
		
		InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_UP){
			@Override
			public boolean callback(){
				mouseDown = false;
				return false;
			}
		});
		
		InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_SCROLL){
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
	}
	
	@Override
	public void render(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		Gdx.gl.glClearColor(0,  0,  .2f, 1);
		
		if(mouseDown){
			Renderer.camera().rotateAround(new Vector3(), Vector3.Y, direction * 1f);
		}
		Renderer.update();
		Renderer.all(0);
		
		Renderer.camera().update();
	}
	
	@Override
	public void resize(int w, int h){
		Renderer.resize(w, h);
	}
}
