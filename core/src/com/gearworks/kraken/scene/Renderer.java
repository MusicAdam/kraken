package com.gearworks.kraken.scene;

import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.Kraken;
import com.gearworks.kraken.utils.Octree;

public class Renderer {
	private static Node root;
	private static ShapeRenderer shapeRenderer;
	private static Stack<Matrix4> matrixStack;
	private static PerspectiveCamera perspectiveCam;
	
	public static void init(){
		shapeRenderer = new ShapeRenderer();
		matrixStack = new Stack<Matrix4>();
	}
	
	public static Node getRoot(){
		if(root == null)
			root = new Node("root");
		return root; 
	}
	
	public static void update(){
		for(Spatial child : root.getChildren()){
				child.update();
		}
	}
	
	private static void render(Node node, long tpf) throws Exception{
		if(node == null)
			return;
		pushMatrix(node.getLocalTransform());

		for(Spatial child : node.getChildren()){
			//System.out.println(shapeRenderer.getTransformMatrix().getTranslation(new Vector3()));
			System.out.println(child.getWorldTransform());
			if(child instanceof Node){
				render((Node)child, tpf);
			}else if(child instanceof Drawable){
				pushMatrix(child.getLocalTransform());
				((Drawable)child).drawShapeRenderer(shapeRenderer);
				popMatrix();
			}else{				
				throw new Exception("Spatial " + child.getName() + " is not drawable.");
			}		
		}
		popMatrix();
	}
	
	public static void all(long tpf){
		try {
			//shapeRenderer.identity();
			shapeRenderer.setProjectionMatrix(perspectiveCam.combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setAutoShapeType(true);
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.set(ShapeType.Point);
			shapeRenderer.point(0, 0, 0);
			shapeRenderer.point(1, 0, 0);
			shapeRenderer.set(ShapeType.Line);
			render(getRoot(), tpf);
			shapeRenderer.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pushMatrix(Matrix4 matrix){
		matrixStack.push(matrix);
		shapeRenderer.getTransformMatrix().mul(matrix);
		shapeRenderer.updateMatrices();
	}
	
	public static void popMatrix(){
		Matrix4 matrix = matrixStack.pop();
		shapeRenderer.getTransformMatrix().mul(matrix.cpy().inv());
		shapeRenderer.updateMatrices();
	}
	
	public static void debugRenderBin(Octree.Bin bin){
		if(bin.children.isEmpty()){
			shapeRenderer.setProjectionMatrix(perspectiveCam.combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(bin.dbgColor);
			shapeRenderer.box(bin.bounds.min.x, bin.bounds.min.y, -bin.bounds.min.z, bin.bounds.getWidth(),bin.bounds.getHeight(), bin.bounds.getDepth());
			shapeRenderer.end();
		}else{
			for(Octree.Bin child : bin.children){
				debugRenderBin(child);
			}
		}
	}	
	
	public static void resize(int w, int h){
        float aspectRatio = (float) w / (float) h;
        perspectiveCam = new PerspectiveCamera(67, 2f * aspectRatio, 2f);		
        perspectiveCam.position.z += 10f;
        perspectiveCam.update();
	}
	
	public static PerspectiveCamera camera(){
		return perspectiveCam;
	}

	public static void drawRay(Ray ray) {
		Vector3 endPoint = new Vector3();
		ray.getEndPoint(endPoint, 100);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();
		shapeRenderer.translate(endPoint.x, endPoint.y, endPoint.z);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(0, 0, 1);
		shapeRenderer.identity();
		shapeRenderer.set(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.line(	ray.origin.x, ray.origin.y, ray.origin.z, 
							endPoint.x, endPoint.y, endPoint.z);
		shapeRenderer.end();
	}
}
