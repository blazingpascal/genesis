package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Engineering extends AOccupation {
    public Engineering() {
        super("Engineering", 0.5);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValue(personality, PersonalityTrait.CONSCIENTIOUSNESS);
        ranking += addValueAbsence(personality, PersonalityTrait.EXTRAVERSION);

        return ranking;
    }
}
