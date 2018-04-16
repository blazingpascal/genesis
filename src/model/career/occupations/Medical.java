package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Medical extends AOccupation {

    public Medical() {
        super("Medical", 0.25);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValueAbsence(personality, PersonalityTrait.OPENNESS);
        ranking += addValueAdditional(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValue(personality, PersonalityTrait.AGREEABLENESS);

        return ranking;
    }
}
