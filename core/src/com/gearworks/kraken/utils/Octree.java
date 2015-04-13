package com.gearworks.kraken.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.scene.Drawable;
import com.gearworks.kraken.scene.Node;
import com.gearworks.kraken.scene.Spatial;

/** Octree implementation for efficient picking. */
public class Octree {
	public class Bin{
		public BoundingBox bounds;
		public Bin parent; //Null for root
		public ArrayList<Spatial> spatials; //Spatials in this node, only set on leaves
		public ArrayList<Bin>    children;
		public Color dbgColor = new Color();
		private int pruneDelay = 10;//Prune empty leaves every 10 frames
		private int pruneTimer = 0; 
		private boolean shouldPrune = false;
		
		public Bin(Bin parent){
			this.parent = parent;
			spatials = new ArrayList<Spatial>();
			children = new ArrayList<Bin>();
			Random rand = new Random();		
			//dbgColor = new Color(rand.nextFloat() + .5f, rand.nextFloat() + .5f, rand.nextFloat() + .5f, .5f);
			dbgColor = Color.GRAY;
			if(parent != null)
				parent.addChild(this);
		}
		
		public void update(){
			if(!children.isEmpty()){
				boolean doPrune = true;
				for(Bin bin : children){
					bin.update();
					doPrune = doPrune && bin.shouldPrune();
				}
				if(doPrune)
					children.clear();
			}else{
				if(spatials.isEmpty()){
					pruneTimer++;
					if(pruneTimer >= pruneDelay)
						shouldPrune = true;
				}
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
	}
	
	private Bin root;
	public final int binSize; //Minimum # of points to constitute partitoning a node.
	public final Vector3 worldSize;
	
	public Octree(Vector3 size){
		binSize = 10;
		this.worldSize = size;
		
		init();
	}
	
	public Octree(Vector3 size, int binSize){
		this.binSize = binSize;
		this.worldSize = size;
		
		init();
	}
	
	private void init(){
		root = new Bin(null);
		root.bounds = new BoundingBox(new Vector3(-worldSize.x/2, -worldSize.y/2, -worldSize.z/2), new Vector3(worldSize.x/2, worldSize.y/2, worldSize.z/2)); //Set bounds centered on origin
	}
	
	public void contrsuctFromSpatial(Spatial spat){		
		root.insert(spat);
		
		Node node = null;
		if(spat instanceof Node)
			node = (Node)spat;
		if(node != null && node.hasChildren()){
			for(Spatial child : node.getChildren()){
				contrsuctFromSpatial(child);
			}
		}
	}
	
	public void insert(Spatial spat){
		root.insert(spat);
	}
	
	public void findNearPoint(Vector3 point, ArrayList<Spatial> found){
		root.findNearPoint(point, found);
	}
	
	public void intersectsBounds(BoundingBox bounds, ArrayList<Spatial> found){
		root.intersectsBounds(bounds, found);
	}
	
	public Bin getRoot(){ return root; }

	public void intersects(Ray ray, ArrayList<Spatial> intersects) {
		root.intersects(ray, intersects);
	}
	
	public void update(){
		root.update();
	}
	
	public void dbg_resetColors() {
		root.dbg_resetColors();
	}
}
