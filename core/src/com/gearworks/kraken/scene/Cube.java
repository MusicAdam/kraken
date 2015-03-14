package com.gearworks.kraken.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

//This class defines a square.
public class Cube extends Drawable{	
	public Cube(){
	}
	
	@Override
	public void drawShapeRenderer(ShapeRenderer shapeRenderer) {
		shapeRenderer.set(ShapeType.Line);
		shapeRenderer.setColor(color);
		shapeRenderer.box(-getSize().x/2, -getSize().y/2, getSize().z/2, getSize().x, getSize().y, getSize().z);
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
