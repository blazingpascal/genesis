package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Teaching extends AOccupation {

    public Teaching() {
        super("Teaching", 0.1);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValue(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValue(personality, PersonalityTrait.EXTRAVERSION);
        ranking += addValueAdditional(personality, PersonalityTrait.AGREEABLENESS);
        ranking += addValueAbsence(personality, PersonalityTrait.NEUROTICISM);

        return ranking;
    }
}
