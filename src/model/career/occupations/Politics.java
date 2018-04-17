package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Politics extends AOccupation {
    public Politics() {
        super("Politics", 0.2);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValueAdditional(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValue(personality, PersonalityTrait.EXTRAVERSION);
        ranking += addValueAbsenceAdditional(personality, PersonalityTrait.NEUROTICISM);

        return ranking;
    }
}
