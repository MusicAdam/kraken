package com.gearworks.kraken.scene;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.gearworks.kraken.Kraken;

public class Renderer {
	public static boolean DEBUG = true;
	private static Environment environment;
	private static Node root;
	private static ShapeRenderer shapeRenderer;
	private static ModelBatch modelBatch;
	private static Stack<Matrix4> matrixStack;
	private static PerspectiveCamera camera;
	private static ModelInstance debug_testInstance;
	
	public static void init(){
		shapeRenderer = new ShapeRenderer();
		modelBatch = new ModelBatch();
		matrixStack = new Stack<Matrix4>();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}
	
	public static Node getRoot(){
		if(root == null)
			root = new Node("root");
		return root; 
	}
	
	public static void update(){
		for(Spatial child : getRoot().getChildren()){
				child.update();
		}
	}
	
	private static void render(Node node, long tpf) throws Exception{
		if(node == null)
			return;
		pushMatrix(node.getLocalTransform());		
		for(Spatial child : node.getChildren()){
			if(child instanceof Node){
				render((Node)child, tpf);
			}else if(child instanceof Geometry){
				pushMatrix(child.getLocalTransform());
					modelBatch.begin(camera);
					modelBatch.render(((Geometry)child), environment);
					modelBatch.end();
				popMatrix();
				
				debug_drawBoundingBox(((Geometry) child).getBounds());
			}else{				
				throw new Exception("Spatial " + child.getName() + " is not drawable.");
			}		
		}
		popMatrix();
	}
	
	public static void all(long tpf){
		try {
			//shapeRenderer.identity();
			render(getRoot(), tpf);
			
			if(DEBUG){
				debug_drawTestInstance();
				debug_drawAxes();
			}
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
	
	public static void resize(int w, int h){
        float aspectRatio = (float) w / (float) h;
        camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);		
        camera.position.z += 10f;
        camera.update();
	}
	
	public static PerspectiveCamera camera(){
		return camera;
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
	
	public static void debug_drawAxes(){
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rotate(getRoot().getWorldRotation().x, getRoot().getWorldRotation().y, getRoot().getWorldRotation().z, getRoot().getWorldRotation().getAngle());
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.line(0, 0, 0, camera.far, 0, 0);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(0, 0, 0, 0, camera.far, 0);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.line(0, 0, 0, 0, 0, camera.far);
		shapeRenderer.identity();
		shapeRenderer.end();
	}

	public static void debug_setTestModelInstance(ModelInstance testInstance) {
		debug_testInstance = testInstance;
	}
	
	public static float deg = 0;
	public static void debug_drawTestInstance(){
		if(debug_testInstance == null || !DEBUG) return;
		
		Quaternion rotation = new Quaternion();
		deg++;
		rotation.setFromAxis(1, 1, 0, deg);
		debug_testInstance.getNode("node1").rotation.set(rotation);
		debug_testInstance.calculateTransforms();
		
		modelBatch.begin(camera);
		modelBatch.render(debug_testInstance, environment);
		modelBatch.end();	
		
		BoundingBox bb = debug_testInstance.calculateBoundingBox(new BoundingBox());
		//bb.mul(debug_testInstance.transform);

		debug_drawBoundingBox(bb);
	}
	
	public static void debug_drawBoundingBox(BoundingBox bb){
		if(!DEBUG) return;

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.box(bb.min.x, bb.min.y, bb.min.z + bb.getDepth(), bb.getWidth(), bb.getHeight(), bb.getDepth());
		shapeRenderer.end();
	}
	
	/*public static ArrayList<Spatial> findSpatials(Ray pickRay){
		ArrayList<Spatial> found = new ArrayList<Spatial>();
		
	}*/
}
