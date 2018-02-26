package model.genetics;

import java.util.Random;

public abstract class ACombinatoryGeneticTrait<SELF extends ICombinatoryGeneticTrait> extends AGeneticTrait<SELF>
		implements ICombinatoryGeneticTrait {

	protected ACombinatoryGeneticTrait(SELF maternalHistory, SELF paternalHistory, Random r) {
		super(maternalHistory, paternalHistory, r);
	}

	protected ACombinatoryGeneticTrait(String name, double value) {
		super(name, value);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	protected void computeNameAndValue(Random r){
		double maternal = this.maternalHistory.getRandomHistoricalTrait(r).getValue();
		double paternal = this.paternalHistory.getRandomHistoricalTrait(r).getValue();
		this.value = (maternal + paternal)/2;
		this.name = computeName(value);
	}

	abstract protected String computeName(Double value);
}
