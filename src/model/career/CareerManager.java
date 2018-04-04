package model.career;

import model.career.occupations.AOccupation;
import model.career.occupations.Journalism;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class CareerManager {

    private Optional<Job> currentJob;
    private ArrayList<Job> previousJobs;
    private AOccupation occupation;
    private Random r;

    public CareerManager() {
        this.currentJob = Optional.empty();
        this.previousJobs = new ArrayList<>();
        this.r = new Random();
    }

    public void manageCareer() {
        if(occupation == null) {
            chooseCareer();
            findJob();
            //find new AOccupation
            //find new job
        } else if(!currentJob.isPresent()) {
            //find new job
        } else {
            //do job
            //external events: had baby, fired
            //random events: quitJob, switchCareer, takeLeave
            //check if want to retire
        }
    }

    public AOccupation chooseCareer() {
        return AOccupation.JOURNALISM;
    }

    public Optional<Job> findJob() {
        if(r.nextDouble() < 0.5) {
            return Optional.of(new Job(occupation));
        }
        return Optional.empty();
    }

    public void retire() {

    }

    public void quitJob() {

    }

    public AOccupation switchCareer() {
        return null;
    }

    public void takeLeave() {

    }
}
