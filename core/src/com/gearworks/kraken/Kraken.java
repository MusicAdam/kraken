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
import com.gearworks.kraken.tests.SceneTest;
import com.gearworks.kraken.tests.TestStateManager;


public class Kraken extends ApplicationAdapter{
	public static final Vector3 WORLD_SIZE = new Vector3(10, 5, 5);
	Ray ray;
	
	@Override
	public void create(){
		StateManager.init();
		Renderer.init();
		
		TestStateManager.runTests();
		
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
	}
	
	@Override
	public void render(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		Gdx.gl.glClearColor(0,  0,  .2f, 1);
		
		StateManager.update();
		Renderer.update();
		Renderer.all(0);
		
		Renderer.camera().update();
	}
	
	@Override
	public void resize(int w, int h){
		Renderer.resize(w, h);
	}
}
