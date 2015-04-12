package com.gearworks.kraken.scene;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


/*
 *	Represents a 3 dimmensional positioning for use in the SceneGraph
 */
public abstract class Spatial{	
	private Matrix4 localTransform; //Location relative to parent.
	private Matrix4 worldTransform; //Location relative to origin.
	private BoundingBox bounds;
	private Node parent;
	private String name;
	private boolean valid; //The update flags for when world location/rotation need to be updated.
	
	public Spatial(){
		parent = null;
		name = "default";
		valid = false;
		
		localTransform = new Matrix4();
		worldTransform = new Matrix4();
		bounds = new BoundingBox();
	}
	
	public Spatial(String name){
		this();
		this.name = name;
		localTransform = new Matrix4();
		worldTransform = new Matrix4();
	}
	
	public void setLocalTransform(Matrix4 transform){
		localTransform = transform;
		invalidate();
	}
	
	public Matrix4 getLocalTransform(){
		return localTransform;
	}
	
	public Matrix4 getWorldTransform(){
		validate();
		return worldTransform;
	}
	
	public Vector3 getLocalTranslation(){
		return getLocalTransform().getTranslation(new Vector3());
	}
	
	public Vector3 getWorldTranslation(){
		validate();
		return getWorldTransform().getTranslation(new Vector3());		
	}
	
	public Quaternion getLocalRotation(){
		return getLocalTransform().getRotation(new Quaternion());
	}
	
	public Quaternion getWorldRotation(){
		validate();
		return getWorldTransform().getRotation(new Quaternion());
	}
	
	public Vector3 getLocalScale(){
		validate();
		return getLocalTransform().getScale(new Vector3());
	}
	
	public Vector3 getWorldScale(){
		validate();
		return getWorldTransform().getScale(new Vector3());
	}
	
	public void setLocalTranslation(Vector3 translation){
		getLocalTransform().setTranslation(translation);
		invalidate();
	}
	
	public void setLocalTranslation(float x, float y, float z){
		setLocalTranslation(new Vector3(x, y, z));
	}
	
	public void setWorldTranslation(Vector3 worldTranslation){
		Vector3 myLocalTranslation = new Vector3();
		getWorldTransform().getTranslation(myLocalTranslation); //The world translation will be the same as the local translation if we have no parent  
		
		//If we are a child, calculate our new local translation based on our parents world translation, and our new world translation
		if(parent != null){
			Vector3 myWorldTranslation = new Vector3();
			Vector3 parentWorldTranslation = new Vector3();
			getParent().getWorldTransform().getTranslation(parentWorldTranslation);
			getWorldTransform().getTranslation(worldTranslation);
			myLocalTranslation = myWorldTranslation.sub(parentWorldTranslation);
			
		}
		getWorldTransform().setTranslation(worldTranslation);
		getLocalTransform().setTranslation(myLocalTranslation); //Don't use setLocalTranstion() here because we don't need to invalidate()
	}
	
	public void setWorldTranslation(float x, float y, float z){
		setWorldTranslation(new Vector3(x, y, z));
	}
	
	public Node getParent(){
		return parent;
	}	
	
	public void validate(){
		if(valid) return;
		
		//Update location
		if(parent == null){
			worldTransform = localTransform;
		}else{
			if(!getParent().isValid())
				getParent().validate();
			
			worldTransform = getParent().getWorldTransform().cpy().mul(localTransform);
		}
		
		//Center the bounds around the world transform.
		Vector3 worldTranslation = new Vector3();
		worldTransform.getTranslation(worldTranslation);		
		bounds.mul(worldTransform);
		valid = true;
	}
	
	public void invalidate(){
		valid = false;
		
		if(this instanceof Node){
			for(Spatial child : ((Node)this).getChildren()){
				child.invalidate();
			}
		}
	}
	
	public boolean isValid(){ return valid; }

	public void setParent(Node parent){
		this.parent = parent;
		invalidate();
	}
	
	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }

	public BoundingBox getBounds(){
		validate();
		return bounds;
	}

	public void setBounds(BoundingBox bounds) {
		this.bounds = bounds;
		invalidate();
	}
	
	public Vector3 getSize(){
		return new Vector3(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
	}
	
	public void setSize(Vector3 size){
		bounds.set(size.cpy().scl(-.5f), size.cpy().scl(.5f));
		invalidate();
	}
	
	public void destroy(){
		if(this instanceof Node){
			for(Spatial child : ((Node)this).getChildren()){
				child.destroy();
			}
		}
	}
	
	public void update(){}
}
