package com.gearworks.kraken;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.input.InputHandler;
import com.gearworks.kraken.input.MouseEvent;
import com.gearworks.kraken.scene.Circle;
import com.gearworks.kraken.scene.Drawable;
import com.gearworks.kraken.scene.Node;
import com.gearworks.kraken.scene.Renderer;
import com.gearworks.kraken.scene.Spatial;
import com.gearworks.kraken.scene.Cube;
import com.gearworks.kraken.utils.Octree;


public class Kraken extends ApplicationAdapter{
	public static final Vector3 WORLD_SIZE = new Vector3(10, 5, 5);
	private Octree octree;
	private boolean mouseDown;
	private int direction = 1;
	Ray ray;
	
	@Override
	public void create(){
		Renderer.init();
		
		Node test = new Node("test root");
		test.setLocalTranslation(1, 0, 0);
		
		Random rand = new Random();
		for(int i = 0; i < 21; i++){
			float x = (rand.nextFloat() * WORLD_SIZE.x) - WORLD_SIZE.x/2;
			float y = (rand.nextFloat() * WORLD_SIZE.y) - WORLD_SIZE.y/2;
			float z = (rand.nextFloat() * WORLD_SIZE.z) - WORLD_SIZE.z/2;
			Cube square = new Cube();
			square.setLocalTranslation(x, y, z);
			square.setSize(new Vector3(.5f, .5f, .5f));
			test.attach(square);
		}
		Cube square = new Cube();
		square.setSize(new Vector3(.5f, .5f, .5f));
		//test.attach(square);
		Renderer.getRoot().attach(test);
		
		octree = new Octree(WORLD_SIZE, 10);
		octree.contrsuctFromSpatial(Renderer.getRoot());
		
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
		
		InputHandler.addListener(new MouseEvent(MouseEvent.MOUSE_MOVE){
			@Override
			public boolean callback(){
				octree.dbg_resetColors();
				Renderer.camera().update();
				ray = Renderer.camera().getPickRay(mouseScreenPosition.x, mouseScreenPosition.y);
				ArrayList<Spatial> intersects = new ArrayList<Spatial>();
				octree.intersects(ray, intersects);
				for(Spatial spat : intersects){
					if(spat instanceof Drawable){
						Drawable d = (Drawable)spat;
						d.setColor(Color.WHITE);
					}
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
		Renderer.debugRenderBin(octree.getRoot());
		
		Renderer.camera().update();
	}
	
	@Override
	public void resize(int w, int h){
		Renderer.resize(w, h);
	}
}
