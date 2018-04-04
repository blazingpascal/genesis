package model.personality;

import java.util.Random;

public interface IPersonality {
	double getTraitValue(PersonalityTrait trait);

	IPersonality mergeWith(IPersonality other);

	static IPersonality calculatePersonality(IPersonality personality1, IPersonality personality2) {
		return personality1.mergeWith(personality2);
	}

	static IPersonality randomPersonality(Random random) {
		return new PersonalityImpl(random);
	}
}
