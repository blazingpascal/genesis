package main;

import java.util.Random;

import model.genetics.GeneticsMap;
import model.genetics.SingleTrait;
import model.genetics.subtypes.HairColorTrait;

public class GeneticsDriver {
	public static void main(String [] args){
		GeneticsMap map1 = new GeneticsMap(HairColorTrait.ORANGE_RED, new SingleTrait(0));
		GeneticsMap map2 = new GeneticsMap(HairColorTrait.BROWN, new SingleTrait(5));
		GeneticsMap child = map1.combine(map2, new Random());
		System.out.println(child.getHairColorName());
	}
}
