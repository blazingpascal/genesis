package model.genetics;

import java.util.Random;

public interface IGeneticTrait {
	String getName();
	IGeneticTrait getRandomHistoricalTrait(Random r);
	double getValue();
}
