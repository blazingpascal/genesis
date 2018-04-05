package model.career.occupations;

import model.person.APersonalInfoPerson;

import java.util.Random;

public abstract class AOccupation {

    public static final Journalism JOURNALISM = new Journalism();

    private Random r;

    public AOccupation() {
        r = new Random();
    }

    public boolean interview(APersonalInfoPerson person) {
        return r.nextDouble() < evaluateHire(person);
    }

    public double progressJob(APersonalInfoPerson person) {
        double p = (double)(r.nextInt(4) - 2) / 10;
        double d = r.nextBoolean() ? 0.5 : -0.5;
        //return p + evaluateProgress(person);
        return d;
    }

    abstract protected double evaluateHire(APersonalInfoPerson person);
    abstract protected double evaluateProgress(APersonalInfoPerson person);

}
