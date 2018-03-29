package model.genetics;

import java.util.HashMap;
import java.util.Random;

public class GeneticsMap {
	private static float MUTATION_CHANCE = 0.05f;
    private HashMap<String, SingleTrait> traits;


    public GeneticsMap(HashMap<String, SingleTrait> traits) {
        this.traits = traits;
    }
	
	public GeneticsMap combine(GeneticsMap map, Random r){
        HashMap<String, SingleTrait> traits = new HashMap<>();

        for(String key : JSONTraits.getTraits().keySet()) {
            SingleTrait t;
            if (r.nextDouble() < MUTATION_CHANCE) {
                t = new SingleTrait(JSONTraits.getRandomSkewRecessValue(key, r));
            } else {
                int maternal = getTrait(key).getRandomHistorical(r);
                int paternal = map.getTrait(key).getRandomHistorical(r);

                t = new SingleTrait(Math.max(maternal, paternal), maternal, paternal);
            }
            traits.put(key, t);
        }

        return new GeneticsMap(traits);
	}

    public SingleTrait getTrait(String s) {
        return traits.get(s);
    }

    public String getTraitName(String s) {
        return JSONTraits.getName(s, getTrait(s).get());
    }

	public static GeneticsMap randomGenes(Random r) {
        HashMap<String, SingleTrait> traits = new HashMap<>();

        for(String key : JSONTraits.getTraits().keySet()) {
            traits.put(key, new SingleTrait(JSONTraits.getRandomValue(key, r)));
        }

        return new GeneticsMap(traits);
	}

	public static GeneticsMap randomSkewRecessGenes(Random r) {
        HashMap<String, SingleTrait> traits = new HashMap<>();

        for(String key : JSONTraits.getTraits().keySet()) {
            traits.put(key, new SingleTrait(JSONTraits.getRandomSkewRecessValue(key, r)));
        }

        return new GeneticsMap(traits);
	}
}
