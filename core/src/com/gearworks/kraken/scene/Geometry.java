package com.gearworks.kraken.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/** A Geometry node contains information on a renderable object in the world. This class encapsulates a single ModelInstance to
 *  Manage Shader, meshes, and animations. Additionally it defines the volume of the spatial through a {@link BoundingBox}, caching
 *  the bounds on a need basis
 * @author MusicAdam 
 */
public class Geometry extends Spatial implements RenderableProvider{	
	private ModelInstance modelInstance;
	protected BoundingBox bounds;
	
	public Geometry(){
		super("default-drawable");
		bounds = new BoundingBox();
	}
	
	public Geometry(String name){
		super(name);
		bounds = new BoundingBox();
	}
	
	/** Sets the ModelInstance to be rendered at this geometry 
	 * @param newInstance the instance to be managed
	 * @return the new instance for chaining */
	public ModelInstance setModelInstance(ModelInstance newInstance){
		modelInstance = newInstance;
		invalidate();
		return modelInstance;
	}
	
	/** 
	 * @return the current ModelInstance managed by this gemoetry */
	public ModelInstance getModelInstance(){ 
		validateTransform();
		return modelInstance; 
	} 
	
	/** 
	 * @return renderables in the ModelInstance
	 */
	public Array<Renderable> getRenderables(){
		validateTransform();
		Array<Renderable> renderables = new Array<Renderable>();
		if(modelInstance != null)
			modelInstance.getRenderables(renderables, null);
		return renderables;
	}
	
	@Override
	public void validateTransform(){
		super.validateTransform();
		if(modelInstance != null){
			modelInstance.transform = worldTransform;
			modelInstance.calculateTransforms();
		}
	}
	
	public void validateBounds(){
		if(!isBoundsValid()){
			if(!isTransformValid())
				validateTransform();
			modelInstance.calculateBoundingBox(bounds);
			bounds.mul(localTransform);
			flags &= ~UPDATE_BOUNDS_BYTE;
		}
	}
	
	@Override
	public void validate(){
		validateBounds(); //Calls validateTransform if needed		
	}
	
	@Override
	public void invalidate(){
		invalidateTransform(); //Calls invalidate bounds
	}
	
	@Override
	public void invalidateTransform(){
		super.invalidateTransform();
		invalidateBounds();
	}
	
	public void invalidateBounds(){
		flags |= UPDATE_BOUNDS_BYTE;
	}

	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		if(modelInstance != null)
			modelInstance.getRenderables(renderables, pool);
	}
	
	@Override
	public void setWorldTranslation(Vector3 worldTranslation){
		super.setWorldTranslation(worldTranslation);
		invalidateBounds();
	}
	
	public BoundingBox getBounds(){
		validateBounds();
		return bounds;
	}
	
	public Vector3 getSize(){
		return new Vector3(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
	}
}
