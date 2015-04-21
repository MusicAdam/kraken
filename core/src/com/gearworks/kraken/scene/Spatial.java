package com.gearworks.kraken.scene;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


/** A leaf node which updates local and world transformations. 
 * 
 * @author MusicAdam
 *
 */
public class Spatial{	
	public static final byte UPDATE_ALL				=   (byte) 0xFF;
	public static final byte UPDATE_TRANSFORM_BYTE	=	0x1;
	public static final byte UPDATE_BOUNDS_BYTE		=	0X2;
	protected Matrix4 localTransform; //Location relative to parent.
	protected Matrix4 worldTransform; //Location relative to origin.
	protected Node parent;
	protected String name;
	protected byte flags; //The update flags for when world location/rotation need to be updated.
						//When UPDATE_TRANSFORM_BYTE is set, the transform is updated.
						//UPDATE_BOUNDS_BYTE is set when the bounds change, and sets it in the parent so the parent can update its bounds.
						
	
	public Spatial(){
		parent = null;
		name = "default";
		flags = UPDATE_ALL;
		
		localTransform = new Matrix4();
		worldTransform = new Matrix4();
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
		validateTransform();
		return worldTransform;
	}
	
	public Vector3 getLocalTranslation(){
		return getLocalTransform().getTranslation(new Vector3());
	}
	
	public Vector3 getWorldTranslation(){
		validateTransform();
		return getWorldTransform().getTranslation(new Vector3());		
	}
	
	public Quaternion getLocalRotation(){
		return getLocalTransform().getRotation(new Quaternion());
	}
	
	public Quaternion getWorldRotation(){
		validateTransform();
		return getWorldTransform().getRotation(new Quaternion());
	}
	
	public Vector3 getLocalScale(){
		validateTransform();
		return getLocalTransform().getScale(new Vector3());
	}
	
	public Vector3 getWorldScale(){
		validateTransform();
		return getWorldTransform().getScale(new Vector3());
	}
	
	public void setLocalTranslation(Vector3 translation){
		getLocalTransform().setTranslation(translation);
		invalidate();
	}
	
	public void setLocalTranslation(float x, float y, float z){
		setLocalTranslation(new Vector3(x, y, z));
	}
	
	public void setLocalRotation(Quaternion rotation){
		rotation.nor();
		localTransform.set(getLocalTranslation(), rotation);
		invalidateTransform();
	}
	
	/** Sets the local translation to reflect the world translation provided and sets the worldTranform 
	 * the the given worldTranslation. This will update the local and world transforms and doesn't not 
	 * invalidate the spatial
	 * 
	 * @param translation to use*/
	public void setWorldTranslation(Vector3 worldTranslation){
		if(parent == null){
			localTransform.setTranslation(worldTranslation);
			worldTransform.setTranslation(worldTranslation);
		}else{
			Vector3 myLocalTransform = new Vector3();
			myLocalTransform = worldTranslation.sub(parent.getWorldTranslation());
			localTransform.setTranslation(myLocalTransform);
			worldTransform.setTranslation(worldTranslation);
		}
	}
	
	public void setWorldTranslation(float x, float y, float z){
		setWorldTranslation(new Vector3(x, y, z));
	}
	
	public Node getParent(){
		return parent;
	}	
	
	public void validate(){
		validateTransform();
	}
	
	public void validateTransform(){
		if(!isTransformValid()){		
			//Update location
			if(parent == null){
				worldTransform = localTransform;
			}else{
				if(!getParent().isTransformValid())
					getParent().validateTransform();
				
				worldTransform = getParent().getWorldTransform().cpy().mul(localTransform);
			}
			
			flags &= ~UPDATE_TRANSFORM_BYTE;
		}
	}
	
	public void invalidateTransform(){
		flags |= UPDATE_TRANSFORM_BYTE;
	}
	
	public void invalidate(){
		invalidateTransform();
	}
	
	public boolean isValid(){ return isTransformValid(); }
	public boolean isTransformValid(){ return (flags & UPDATE_TRANSFORM_BYTE) == 0;}

	public void setParent(Node parent){
		this.parent = parent;
		invalidate();
	}
	
	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }
	
	public void destroy(){
		if(this instanceof Node){
			for(Spatial child : ((Node)this).getChildren()){
				child.destroy();
			}
		}
	}
	
	public void update(){}
}
