package com.gearworks.kraken.scene;

import java.util.ArrayList;

public class Node extends Spatial {
	private ArrayList<Spatial> children;
	
	public Node(){
		super();
		children = new ArrayList<Spatial>();
		System.out.println("NODE CONSTRUCTOR1");
	}
	
	public Node(String name){
		super(name);
		children = new ArrayList<Spatial>();
		System.out.println("NODE CONSTRUCTOR2");
	}
	
	public void attach(Spatial child){
		children.add(child);
		child.setParent(this);
		child.invalidate();
	}
	
	public ArrayList<Spatial> getChildren(){ return children; }
	
	public boolean hasChild(String name){
		if(children.size() == 0)
			return false;
		
		for(Spatial child : children){
			if(child.getName() == name){
				return true;
			}else{
				if(child instanceof Node){
					((Node)child).hasChild(name);
				}
			}
		}
		
		return false;
	}
	
	public boolean hasChildren(){
		return !children.isEmpty();
	}
	
	public Spatial getChild(String name){
		if(children.size() == 0)
			return null;
		
		for(Spatial child : children){
			if(child.getName().equals(name)){
				return child;
			}else{
				if(child instanceof Node){
					return ((Node)child).getChild(name);
				}
			}
		}
		
		return null;
	}
	
	public void detach(Spatial child){
		boolean success = false;
		for(Spatial c : getChildren()){
			if(child == c){
				c.setParent(null);
				success = true;
				
				if(child instanceof Node){
					((Node)child).clear();
				}
			}
		}
		
		if(success)
			children.remove(child);
	}

	public void clear() {
		for(Spatial c : getChildren()){
			if(c instanceof Node)
				((Node)c).clear();
		}
		
		children.clear();
	}
	
}
