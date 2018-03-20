package model.genetics;

import java.util.Random;

public abstract class AGeneticTrait<SELF extends IGeneticTrait> implements IGeneticTrait {
	protected SELF maternalHistory;
	protected SELF paternalHistory;
	protected String name;
	protected Double value = null;

	protected AGeneticTrait(SELF maternalHistory, SELF paternalHistory, Random r) {
		this.maternalHistory = maternalHistory;
		this.paternalHistory = paternalHistory;
		computeNameAndValue(r);
		if (name == null || value == null) {
			throw new IllegalStateException("Name and value not set!");
		}
	}

	abstract protected void computeNameAndValue(Random r);

	protected AGeneticTrait(String name, double value) {
		this.name = name;
		this.value = value;
		setHistoryToSelf();
		if (maternalHistory == null || paternalHistory == null) {
			throw new IllegalStateException("Name and value not set!");
		}
	}

	abstract protected void setHistoryToSelf();

	public String getName() {
		return this.name;
	}

	public IGeneticTrait getRandomHistoricalTrait(Random r) {
		if (r.nextBoolean()) {
			return maternalHistory;
		}
		return paternalHistory;
	}

	public double getValue() {
		return this.value;
	}

}
