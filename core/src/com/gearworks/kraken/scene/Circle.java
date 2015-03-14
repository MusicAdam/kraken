package com.gearworks.kraken.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle extends Drawable{

	private int resolution; //This is the number of vertices in the circle.
	private float step;     //This is the number of degrees in radians between each vertex in the circle
	
	public Circle(){
		super();
		setResolution(12);
	}
	
	public Circle(int res){
		super();
		setResolution(res);
	}
	
	public void setResolution(int res){
		resolution = res;
		step = (float)Math.PI/(res/2);
	}
	
	@Override
	public void drawShapeRenderer(ShapeRenderer shapeRenderer) {
		shapeRenderer.setTransformMatrix(getLocalTransform());
		shapeRenderer.setColor(getColor());
		shapeRenderer.circle(0, 0, getSize().x);
	}

	@Override
	public void drawSpriteBatch(SpriteBatch renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawModelBatch(ModelBatch renderer) {
		// TODO Auto-generated method stub
		
	}
}
