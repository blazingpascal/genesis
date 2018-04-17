package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Finance extends AOccupation {
    public Finance() {
        super("Finance", 0.5);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValue(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValue(personality, PersonalityTrait.EXTRAVERSION);

        return ranking;
    }
}
