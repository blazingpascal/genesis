package main;

import java.util.HashMap;
import java.util.Random;

import model.genetics.GeneticsMap;
import model.genetics.SingleTrait;

public class GeneticsDriver {
	public static void main(String [] args){
        HashMap<String, SingleTrait> t1 = new HashMap<>();
        t1.put("hair color", new SingleTrait(0));
        GeneticsMap map1 = new GeneticsMap(t1);
        HashMap<String, SingleTrait> t2 = new HashMap<>();
        t2.put("hair color", new SingleTrait(5));
		GeneticsMap map2 = new GeneticsMap(t2);
		GeneticsMap child = map1.combine(map2, new Random());
		System.out.println(child.getTraitName("hair color"));
	}
}
