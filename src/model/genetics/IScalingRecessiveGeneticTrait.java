package model.genetics;

import java.util.Random;

public interface IScalingRecessiveGeneticTrait extends IGeneticTrait {
	double recessiveValue();
	IScalingRecessiveGeneticTrait getRandomScalingRecessiveHistoricalTrait(Random r);
}
