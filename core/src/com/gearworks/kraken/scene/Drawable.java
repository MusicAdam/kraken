package com.gearworks.kraken.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

//This is the base abstract shape class containing the data for everything needed to draw
// shapes
public abstract class Drawable extends Spatial{	
	//TODO: DEPRECATED? protected 	int 	drawMode = GL_TRIANGLES;
	protected		Color color    = new Color();
	//TODO: DEPRECATED? private Vector2 location; //the location of the center of the shape x, y
	
	public Drawable(){
		super("default-drawable");
		init();
	}
	
	public Drawable(int drawMode){
		super("default-drawable");
		//this.drawMode = drawMode;
		init();
	}
	
	public Drawable(String name){
		super(name);
		init();
	}
	
	public Drawable(String name, int drawMode){
		super(name);
		//this.drawMode = drawMode;
		init();
	}
	
	protected void init(){
		//location = new Vector2();
	}	
	
	//public void setMode(int newMode){ drawMode = newMode; }
	//public int  getMode(){ return drawMode; }
	
	public Color getColor(){ return color; }	
	public void setColor(Color newColor){ color = newColor; }
	public void setColor(float r, float g, float b, float a){
		color = new Color(r, g, b, a);
	}
	
	public abstract void drawShapeRenderer(ShapeRenderer renderer);
	public abstract void drawSpriteBatch(SpriteBatch renderer);
	public abstract void drawModelBatch(ModelBatch renderer);
	
	
}
