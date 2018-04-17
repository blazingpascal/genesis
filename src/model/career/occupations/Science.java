package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

public class Science extends AOccupation{
    public Science() {
        super("Science", 0.5);
    }

    @Override
    public double evaluateFit(APersonalInfoPerson person) {
        IPersonality personality = person.getPersonality();
        double ranking = 0;

        ranking += addValueAdditional(personality, PersonalityTrait.CONSCIENTIOUSNESS);

        return ranking;
    }
}
