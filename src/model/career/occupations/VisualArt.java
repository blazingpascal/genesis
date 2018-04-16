package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class VisualArt extends AOccupation {

    public VisualArt() {
        super("Visual Art", 0.25);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValueAdditional(personality, PersonalityTrait.OPENNESS);
        ranking += addValueAbsence(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValueAbsence(personality, PersonalityTrait.EXTRAVERSION);

        return ranking;
    }
}
