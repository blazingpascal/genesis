package model.personality;

import java.util.Random;

public class PersonalityImpl implements IPersonality {

	private final double openness;
	private final double conscientiousness;
	private final double extraversion;
	private final double agreeableness;
	private final double neuroticism;

	public PersonalityImpl(Random r) {
		this(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
	}

	PersonalityImpl(double openness, double conscientiousness, double extraversion, double agreeableness,
			double neuroticism) {
		this.openness = openness;
		this.conscientiousness = conscientiousness;
		this.extraversion = extraversion;
		this.agreeableness = agreeableness;
		this.neuroticism = neuroticism;
	}

	public double getTraitValue(PersonalityTrait trait) {
		switch (trait) {
		case AGREEABLENESS:
			return this.agreeableness;
		case CONSCIENTIOUSNESS:
			return this.conscientiousness;
		case EXTRAVERSION:
			return this.extraversion;
		case NEUROTICISM:
			return this.neuroticism;
		case OPENNESS:
			return this.openness;
		default:
			throw new IllegalArgumentException(String.format("No such personality trait %s", trait));
		}
	}

	@Override
	public IPersonality mergeWith(IPersonality other) {
		double openness = (this.openness + 
				other.getTraitValue(PersonalityTrait.OPENNESS)) / 2;
		double conscientiousness = (this.conscientiousness + 
				other.getTraitValue(PersonalityTrait.CONSCIENTIOUSNESS)) / 2;
		double extraversion = (this.extraversion + 
				other.getTraitValue(PersonalityTrait.EXTRAVERSION)) / 2;
		double agreeableness = (this.agreeableness + 
				other.getTraitValue(PersonalityTrait.AGREEABLENESS)) / 2;
		double neuroticism = (this.neuroticism + 
				other.getTraitValue(PersonalityTrait.NEUROTICISM)) / 2;
		return new PersonalityImpl(openness, conscientiousness, extraversion, 
				agreeableness, neuroticism);
	}

	@Override
	public IPersonality generatePersonality(Random r) {
		return new PersonalityImpl(r);
	}

}
