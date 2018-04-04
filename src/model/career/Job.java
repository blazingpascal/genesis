package model.career;

import model.career.occupations.AOccupation;

public class Job {

    private AOccupation occupation;
    private int level;
    private double performance;
    private int status; //active, fired, quit, onLeave

    public Job(AOccupation occupation) {
        this.occupation = occupation;
        this.level = 0;
        this.performance = 0.5;
        this.status = 0;
    }

    public void promote() {
        level++;
    }

    public void fire() {
        this.status = 1;
    }

}
