package model.personality;

import java.util.Random;

public interface IPersonality {
	double getTraitValue(PersonalityTrait trait);
	IPersonality mergeWith(IPersonality other);
	IPersonality generatePersonality(Random r);
}
