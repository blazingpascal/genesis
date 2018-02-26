package main;

import java.util.Random;

import model.genetics.GeneticsMap;
import model.genetics.subtypes.HairColorTrait;

public class GeneticsDriver {
	public static void main(String [] args){
		GeneticsMap map1 = new GeneticsMap(HairColorTrait.ORANGE_RED);
		GeneticsMap map2 = new GeneticsMap(HairColorTrait.BROWN);
		GeneticsMap child = map1.combine(map2, new Random());
		System.out.println(child.getHairColor().getName());
	}
}
