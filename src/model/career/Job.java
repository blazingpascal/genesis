package model.career;

import model.career.occupations.AOccupation;

public class Job {

    private AOccupation occupation;
    private int level;
    private double performance;
    private int status; //active, fired, quit, on leave

    public Job(AOccupation occupation) {
        this.occupation = occupation;
        this.level = 0;
        this.performance = 0;
        this.status = 0;
    }

    public void work(CareerManager manager) {
        if(status == 0) {
            performance += occupation.progressJob(manager.getPerson());
            System.out.println(performance);
            if (performance >= 1) {
                if (level < 9) {
                    level++;
                    performance = 0;
                }
            } else if (performance <= -1) {
                this.status = 1;
                manager.fired(this);
            }
        } else if(status == 3) {
            status = 0;
        }
    }

    public void sendResignation() {
        status = 2;
    }

    public void takeLeave() {
        status = 3;
    }
}
