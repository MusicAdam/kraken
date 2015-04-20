package com.gearworks.kraken.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.scene.Geometry;
import com.gearworks.kraken.scene.Node;
import com.gearworks.kraken.scene.Spatial;

/*
public class OctNode {	
	public final int 			binSize; //Minimum # of points to constitute partitoning a node.
	public BoundingBox 			bounds;
	public OctNode 				parent; //Null for root
	public Spatial[]			spatials; //Spatials in this node, only set on leaves
	public OctNode[]    		children;
	public Color 				dbgColor = new Color();
	private int 				pruneDelay = 10;//Prune empty leaves every 10 frames
	private int 				pruneTimer = 0; 
	private boolean 			shouldPrune = false;
	private boolean 			hasChildren = false; //True when this node has children
	private boolean				isFull 		= false; //True when this node this node has the maximum number of spatials 
	private boolean 			isValid     = false; //When false, this node has changed, either due to insertions, deletions or spatial movement
	private Queue<Spatial>		insertQueue; 
	
	public OctNode(BoundingBox bounds){
		parent = null;
		binSize = 10;
		this.bounds = bounds;
		
		init();
	}
	
	public OctNode(BoundingBox bounds, int binSize){
		this.binSize = binSize;
		this.bounds = bounds;
		
		init();
	}
	
	private void init(){
		spatials = new Spatial[binSize];
		children = new OctNode[8];
		insertQueue = new ConcurrentLinkedQueue<Spatial>();
		dbgColor = Color.GRAY;
	}
	
	public void update(){
		//Process insertions
		while(!insertQueue.isEmpty() && !isFull)
		if(!isValid){
			
			//Partition if required
		}
	}
	
	//Splits this node 8 ways, moving spatials as required.
	public void partition(){
		float width = bounds.getWidth();
		float height = bounds.getHeight();
		float depth = bounds.getDepth();
		Vector3 cnt = bounds.getCenter(new Vector3());
		
		BoundingBox[] boxes = new BoundingBox[8];
		boxes[0] = new BoundingBox();
		boxes[0].set(cnt.cpy().add(new Vector3(-width/2, -height/2, -depth/2)), cnt.cpy());
		boxes[1] = new BoundingBox();
		boxes[1].set(cnt.cpy().add(new Vector3(-width/2, -height/2, 0)), cnt.cpy().add(new Vector3(0, 0, depth/2)));
		boxes[2] = new BoundingBox();
		boxes[2].set(cnt.cpy().add(new Vector3(0, -height/2, -depth/2)), cnt.cpy().add(new Vector3(width/2, 0, 0)));
		boxes[3] = new BoundingBox();
		boxes[3].set(cnt.cpy().add(new Vector3(0, -height/2, 0)), cnt.cpy().add(new Vector3(width/2, 0, depth/2)));
		boxes[4] = new BoundingBox();
		boxes[4].set(cnt.cpy().add(new Vector3(-width/2, 0, -depth/2)), cnt.cpy().add(new Vector3(0, height/2, 0)));
		boxes[5] = new BoundingBox();
		boxes[5].set(cnt.cpy().add(new Vector3(-width/2, 0, 0)), cnt.cpy().add(new Vector3(0, height/2, depth/2)));
		boxes[6] = new BoundingBox();
		boxes[6].set(cnt.cpy().add(new Vector3(0, 0, -depth/2)), cnt.cpy().add(new Vector3(width/2, height/2, 0)));
		boxes[7] = new BoundingBox();
		boxes[7].set(cnt.cpy(), cnt.cpy().add(new Vector3(width/2, height/2, depth/2)));
		
		for(BoundingBox box : boxes){
			Bin node = new Bin(this);
			node.bounds = box;
			
			if(spatials != null){
				Iterator<Spatial> it = spatials.iterator();
				while(it.hasNext()){
					if(node.insert(it.next()))
						it.remove();
				}
			}
			addChild(node);
		}
	}
	
	public boolean insert(Spatial spat){
		if(bounds.contains(spat.getBounds())){
			for(Bin child : children){
				if(child.insert(spat)){
					shouldPrune = false;
					return true;
				}
			}
			
			if(spatials == null)
				spatials = new ArrayList<Spatial>();
			boolean success = spatials.add(spat);
			if(spat instanceof Drawable){
				((Drawable)spat).setColor(dbgColor);
			}
			if(spatials.size() > binSize){
				partition();
			}
			shouldPrune = !success;
			return success;
		}
		
		return false;
	}
	
	public void addChild(Bin child){
		children.add(child);
	}		
	
	public void findNearPoint(Vector3 point, ArrayList<Spatial> found){
		if(bounds.contains(point)){
			for(Bin child : children){
				child.findNearPoint(point, found);
			}
			found.addAll(spatials);
		}
	}
	
	public void intersectsBounds(BoundingBox bounds, ArrayList<Spatial> found){
		if(this.bounds.intersects(bounds)){
			for(Bin child : children){
				child.intersectsBounds(bounds, found);
			}
			
			for(Spatial spat : spatials){
				if(spat.getBounds().intersects(bounds)){
					found.add(spat);
				}
			}
		}			
	}

	public void intersects(Ray ray, ArrayList<Spatial> intersects) {
		if(Intersector.intersectRayBounds(ray, bounds, null)){
			dbgColor = Color.WHITE;
			for(Bin child : children){
				child.intersects(ray, intersects);
			}
			
			for(Spatial spat : spatials){
				if(Intersector.intersectRayBounds(ray, spat.getBounds(), null))
					intersects.add(spat);
			}
		}else{
			dbgColor = Color.GRAY;
		}
	}
	
	public boolean shouldPrune(){ return shouldPrune; }
	
	public void setParent(OctNode parent){
		this.parent = parent;
	}
	
	public OctNode getParent(){ return parent; }
	
	public void dbg_resetColors() {			
		dbgColor = Color.GRAY;
		
		for(Spatial spat : spatials){
			if(spat instanceof Drawable){
				Drawable d = (Drawable)spat;
				d.setColor(Color.GRAY);
			}
		}
		
		for(Bin child : children){
			child.dbg_resetColors();
		}
	}
	
	//Returns true if this node has no children
	public boolean isLeaf(){
		return !hasChildren;
	}
	
	public boolean isFull(){
		return isFull;
	}
	
}
*/
