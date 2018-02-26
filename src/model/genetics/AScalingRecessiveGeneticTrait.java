package model.genetics;

import java.util.Random;

public abstract class AScalingRecessiveGeneticTrait<SELF extends IScalingRecessiveGeneticTrait>
		extends AGeneticTrait<SELF> implements IScalingRecessiveGeneticTrait {

	protected AScalingRecessiveGeneticTrait(SELF maternalHistory, SELF paternalHistory, Random r) {
		super(maternalHistory, paternalHistory, r);
	}

	protected AScalingRecessiveGeneticTrait(String name, double recessiveValue) {
		super(name, recessiveValue);
	}

	public double recessiveValue() {
		return this.value;
	}

	@Override
	protected void computeNameAndValue(Random r) {
		IScalingRecessiveGeneticTrait maternal = maternalHistory.getRandomScalingRecessiveHistoricalTrait(r);
		IScalingRecessiveGeneticTrait paternal = paternalHistory.getRandomScalingRecessiveHistoricalTrait(r);
		boolean maternalMoreRecessive = maternal.recessiveValue() < paternal.recessiveValue();
		this.name = maternalMoreRecessive ? paternal.getName() : maternal.getName();
		this.value = maternalMoreRecessive ? paternal.recessiveValue() : maternal.recessiveValue();
	}

	public IScalingRecessiveGeneticTrait getRandomScalingRecessiveHistoricalTrait(Random r) {
		if (r.nextBoolean()) {
			return maternalHistory;
		} else {
			return paternalHistory;
		}
	}
}
