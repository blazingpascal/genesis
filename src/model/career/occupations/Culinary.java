package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Culinary extends AOccupation{

    public Culinary() {
        super("Culinary", 0.5);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValue(personality, PersonalityTrait.OPENNESS);
        ranking += addValue(personality, PersonalityTrait.CONSCIENTIOUSNESS);

        return ranking;
    }
}
