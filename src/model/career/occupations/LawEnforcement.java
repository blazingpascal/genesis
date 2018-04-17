package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class LawEnforcement extends AOccupation {

    public LawEnforcement() {
        super("Law Enforcement", 0.33);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValueAbsence(personality, PersonalityTrait.OPENNESS);
        ranking += addValue(personality, PersonalityTrait.EXTRAVERSION);
        ranking += addValueAbsence(personality, PersonalityTrait.NEUROTICISM);

        return ranking;
    }
}
