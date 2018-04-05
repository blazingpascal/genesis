package model.career.occupations;

import model.person.APersonalInfoPerson;

import java.util.Random;

public class Journalism extends AOccupation {

    @Override
    protected double evaluateHire(APersonalInfoPerson person) {
        return 0.5;
    }

    @Override
    protected double evaluateProgress(APersonalInfoPerson person) {
        return 0;
    }
}
