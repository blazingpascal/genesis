package model.genetics.subtypes;

import java.util.Random;
import model.genetics.AScalingRecessiveGeneticTrait;

public class CleftChinTrait extends AScalingRecessiveGeneticTrait<CleftChinTrait> {

	public static final CleftChinTrait CLEFT = new CleftChinTrait("Cleft Chin", 0);
	public static final CleftChinTrait NO_CLEFT = new CleftChinTrait("No Cleft Chin", 1);

	public CleftChinTrait(CleftChinTrait maternalHistory, CleftChinTrait paternalHistory, Random r) {
		super(maternalHistory, paternalHistory, r);
	}

	protected CleftChinTrait(String name, int recessiveValue) {
		super(name, recessiveValue);
	}

	@Override
	protected void setHistoryToSelf() {
		this.maternalHistory = this;
		this.paternalHistory = this;
	}

	public static CleftChinTrait random(Random r){
		if(r.nextBoolean()) return CLEFT;
		return NO_CLEFT;
	}
	
	public static CleftChinTrait randomSkewRecess(Random r){
		for(int i = 0; i < 3; i++){
			if(r.nextBoolean()) return CLEFT;
		}
		return NO_CLEFT;
	}
}