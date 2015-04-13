package com.gearworks.kraken.tests;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.gearworks.kraken.utils.*;
import com.gearworks.kraken.scene.Cube;
import com.gearworks.kraken.scene.Spatial;

public class UtilsTest {
	/*
	 * Ensure that if a spatial lies between two octree buckets, it exists in both of them
	 */
	public static boolean octreeSupportsOverlappingSpatials(){
		System.out.println("octreeSupportsOverlappingSpatials ... ");
		int binSize = 2;
		Octree tree = new Octree(new Vector3(1, 1, 1), binSize);
		
		//Create 4 spatials all in multiple bins
		Vector3 size = new Vector3(.25f, .25f, .25f);
		for(int i = 0; i < 4; i++){
			Spatial spat = new Spatial();
			spat.setSize(size);
			spat.setLocalTranslation(-.375f + i * .25f, 0, 0);
			tree.insert(spat);		
		}
		
		//Make sure there are the expected number of bins
		if(tree.getRoot().children.size() != 4){
			System.out.println("\tFailed: There are not 4 bins! Bin count: " + tree.getRoot().children.size());
			return false;
		}
		
		System.out.println("\tsucceeded");
		return true;
		
	}
}
