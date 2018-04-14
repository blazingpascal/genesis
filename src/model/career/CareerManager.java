package model.career;

import model.career.occupations.AOccupation;
import model.person.APersonalInfoPerson;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class CareerManager {

    private APersonalInfoPerson person;
    private Optional<Job> currentJob;
    private ArrayList<Job> previousJobs;
    private AOccupation occupation;
    private boolean retired;
    private Random r;
    private int jobAttempts;
    private int maxAttempts;

    public CareerManager(APersonalInfoPerson person) {
        this.person = person;
        this.currentJob = Optional.empty();
        this.previousJobs = new ArrayList<>();
        this.retired = false;
        this.r = new Random();
        this.maxAttempts = calculateMaxAttempts();
    }

    private int calculateMaxAttempts() {
        double focus = Math.pow(person.getRole().getCareerFocus(), 1.5);
        return (int) Math.round(focus * 6) + 1;
    }

    public void manageCareer() {
        if(!retired) {
            if(occupation == null || jobAttempts >= maxAttempts) {
                occupation = chooseCareer();
                currentJob = attemptFindJob();
            } else if(!currentJob.isPresent()) {
                currentJob = attemptFindJob();
            } else {
                jobAttempts = 0;
                if(attemptQuit()) return;
                attemptTakeLeave();

                currentJob.get().work(this);
                if(person.getAge() > 65) retired = attemptRetire();
            }
        }
    }

    public APersonalInfoPerson getPerson() {
        return person;
    }

    public AOccupation chooseCareer() {
        //temp. chooses at random
        jobAttempts = 0;

        int i = r.nextInt(4);
        switch(i) {
            case 0: return AOccupation.JOURNALISM;
            case 1: return AOccupation.MEDICAL;
            case 2: return AOccupation.VISUAL_ART;
            case 3: return AOccupation.TEACHING;
            default: return AOccupation.LAW_ENFORCEMENT;
        }
    }

    public Optional<Job> attemptFindJob() {
        Optional<Job> j = occupation.interview(person) ? Optional.of(new Job(occupation)) : Optional.empty();
        if(!j.isPresent()) jobAttempts++;
        return j;
    }

    public boolean attemptRetire() {
        double tenacity = person.getRole().getCareerTenacity();
        double chance = (1 - tenacity) / 2 + 0.1;
        return r.nextDouble() < chance;
    }

    public boolean attemptQuit() {
        double focus = person.getRole().getCareerFocus();
        double chance = Math.pow((1 - focus) / 2, 2) + 0.05;
        boolean b = r.nextDouble() < chance;
        if(b) quitJob();
        return b;
    }

    public void attemptTakeLeave() {
        if(r.nextDouble() < 0.1) currentJob.get().takeLeave();
    }

    public void fired(Job j) {
        previousJobs.add(j);
        currentJob = Optional.empty();
    }

    public void quitJob() {
        currentJob.get().sendResignation();
        previousJobs.add(currentJob.get());
        currentJob = Optional.empty();
    }
}
