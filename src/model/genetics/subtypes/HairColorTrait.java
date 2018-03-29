package model.genetics.subtypes;

import java.util.Random;

import model.genetics.AScalingRecessiveGeneticTrait;

public class HairColorTrait extends AScalingRecessiveGeneticTrait<HairColorTrait>{

	private static final double MUTATION_CHANCE = 0.05;
	
	public static final HairColorTrait ORANGE_RED = new HairColorTrait("orange red", -2);
	public static final HairColorTrait DARK_RED = new HairColorTrait("dark red", -1);
	public static final HairColorTrait LIGHT_BLONDE = new HairColorTrait("light blonde", 0);
	public static final HairColorTrait BLONDE = new HairColorTrait("blonde", 1);
	public static final HairColorTrait LIGHT_BROWN = new HairColorTrait("light brown", 2);
	public static final HairColorTrait BROWN = new HairColorTrait ("brown", 3);
	public static final HairColorTrait DARK_BROWN = new HairColorTrait("dark brown", 4);
	public static final HairColorTrait BLACK = new HairColorTrait("black", 5);
	
	private static final HairColorTrait [] defaults;
	static{
		defaults = new HairColorTrait[8];
		defaults[0] = ORANGE_RED;
		defaults[1] = DARK_RED;
		defaults[2] = LIGHT_BLONDE;
		defaults[3] = BLONDE;
		defaults[4] = LIGHT_BROWN;
		defaults[5] = BROWN;
		defaults[6] = DARK_BROWN;
		defaults[7] = BLACK;
	}
	
	public HairColorTrait(HairColorTrait maternalHistory, HairColorTrait paternalHistory, Random r) {
		super(maternalHistory, paternalHistory, r);
	}
	
	protected HairColorTrait(String name, int value){
		super(name, value);
	}

	@Override
	protected void setHistoryToSelf() {
		this.maternalHistory = this;
		this.paternalHistory = this;
	}

	public static HairColorTrait random(Random r) {
		return defaults[r.nextInt(defaults.length)];
	}

    public static HairColorTrait randomSkewRecess (Random r) {
        for(int i = 0; i < 3; i++){
            if(r.nextBoolean()) return defaults[r.nextInt(defaults.length/2)];
        }
        return defaults[r.nextInt(defaults.length/2) + defaults.length/2];
    }

}
