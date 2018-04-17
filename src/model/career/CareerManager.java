package model.career;

import model.career.occupations.AOccupation;
import model.goals.JoinedJobEvent;
import model.lifeevents.QuitJobEvent;
import model.lifeevents.RetirementEvent;
import model.lifeevents.TakeLeaveEvent;
import model.person.APersonalInfoPerson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

class JobGroup {
    private AOccupation occupation;
    private ArrayList<Job> jobs;

    public JobGroup(AOccupation o, Job j) {
        this.occupation = o;
        this.jobs = new ArrayList<>();
        this.jobs.add(j);
    }

    public void addJob(Job j) {
        jobs.add(j);
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public AOccupation getOccupation() {
        return occupation;
    }

    public boolean sameOccupation(AOccupation o) {
        return o.getName() == occupation.getName();
    }

    public Job getMostRecentJob() {
        return jobs.get(jobs.size() - 1);
    }
}

public class CareerManager {
    private APersonalInfoPerson person;
    private Optional<Job> currentJob;
    private ArrayList<JobGroup> previousJobGroups;
    private AOccupation occupation;
    private boolean retired;
    private Random r;
    private int jobAttempts;
    private int maxAttempts;
    private AOccupation[] careerRanking;

    public CareerManager(APersonalInfoPerson person) {
        this.person = person;
        this.currentJob = Optional.empty();
        this.previousJobGroups = new ArrayList<>();
        this.retired = false;
        this.r = new Random();
        this.maxAttempts = calculateMaxAttempts();
        this.careerRanking = rankCareers();
    }
  
  	public ArrayList<Job> getPreviousJobs() {
        ArrayList<Job> result = new ArrayList<>();
        for(JobGroup g : previousJobGroups) {
            for(Job j : g.getJobs()) {
                result.add(j);
            }
        }
        return result;
    }


    private int calculateMaxAttempts() {
        double focus = Math.pow(person.getRole().getCareerFocus(), 1.5);
        return (int) Math.round(focus * 6) + 1;
    }

    private AOccupation[] rankCareers() {
        AOccupation[] sorted = AOccupation.opts.clone();
        Arrays.sort(sorted, (a1, a2) -> {
            double d = a1.evaluateFit(person) - a2.evaluateFit(person);
            return d < 0 ? 1 : d > 0 ? -1 : 0;
        });
        return sorted;
    }

    public void manageCareer(int year) {
        if(!retired) {
            if(occupation == null || jobAttempts >= maxAttempts) {
                occupation = chooseCareer();
                currentJob = attemptFindJob(year);
            } else if(!currentJob.isPresent()) {
                currentJob = attemptFindJob(year);
            } else {
                jobAttempts = 0;
                if(attemptQuit(year)) return;
                attemptTakeLeave(year);

                currentJob.get().work(this);
                if(person.getAge() > 65) retired = attemptRetire(year);
            }
        }
    }

    public APersonalInfoPerson getPerson() {
        return person;
    }

    public AOccupation chooseCareer() {
        jobAttempts = 0;
        if(occupation != null) {
            for(int i = 0; i < careerRanking.length - 1; i++) {
                if(occupation.getName() == careerRanking[i].getName()) {
                    careerRanking[i] = careerRanking[i + 1];
                    careerRanking[i + 1] = occupation;
                }
            }
        }

        for(AOccupation oc : careerRanking) {
            if(occupation == null || occupation.getName() != oc.getName()) {
                double roll = r.nextDouble();
                if(roll < 0.7) return oc;
            }
        }

        for(AOccupation oc : careerRanking) {
            if(occupation == null || occupation.getName() != oc.getName()) {
                return oc;
            }
        }

        return careerRanking[0];
    }

    public Optional<Job> attemptFindJob(int year) {
        if(occupation.interview(person)) {
            int level = 0;

            if(!previousJobGroups.isEmpty()) {
                for(int i = previousJobGroups.size() - 1; i >= 0; i--) {
                    if(previousJobGroups.get(i).sameOccupation(occupation)) {
                        double roll = r.nextDouble();
                        level = roll < 0.4 ? roll < 0.15 ? -1 : 1 : 0;
                        level += previousJobGroups.get(i).getMostRecentJob().getRank();
                        level = Math.max(0, Math.min(level, 9));
                        break;
                    }
                }
            }
            Job j = new Job(occupation, level);
  					person.addLifeEvent(new JoinedJobEvent(person, j, year));
            return Optional.of(j);
        }
        jobAttempts++;
        return Optional.empty();
    }
  
  	public boolean attemptQuit(int year) {
      double focus = person.getRole().getCareerFocus();
      double chance = Math.pow((1 - focus) / 2, 2) + 0.05;
      boolean b = r.nextDouble() < chance;
      if (b) {
        this.person.addLifeEvent(new QuitJobEvent(this.person, currentJob.get(), year));
        quitJob();
      }
      return b;
    }

    public void attemptTakeLeave(int year) {
      if (r.nextDouble() < 0.1) {
        currentJob.get().takeLeave();
        this.person.addLifeEvent(new TakeLeaveEvent(this.person, this.currentJob.get(), year));
      }
    }

    public boolean attemptRetire(int year) {
        double tenacity = person.getRole().getCareerTenacity();
        double chance = (1 - tenacity) / 2 + 0.1;
        boolean b = r.nextDouble() < chance;
        if(b) {
            Job j = currentJob.orElse(null);
            if(j == null) j = lastJobGroup().getMostRecentJob();
            person.addLifeEvent(new RetirementEvent(person, j, year));
        }
        return b;
    }

    public void fired() {
        addToPreviousJobs(currentJob.get());
        currentJob = Optional.empty();
    }

    public void quitJob() {
        currentJob.get().sendResignation();
        addToPreviousJobs(currentJob.get());
        currentJob = Optional.empty();
    }

    void addToPreviousJobs(Job j) {
        if(previousJobGroups.size() > 0) {
            JobGroup last = lastJobGroup();
            if(last.sameOccupation(j.getOccupation())) {
                last.addJob(j);
                return;
            }
        }
        previousJobGroups.add(new JobGroup(occupation, j));

    }
  
  	public Job currentJob() {
      return currentJob.orElse(null);
    }

    public boolean isRetired() {
        return retired;
    }

    JobGroup lastJobGroup() {
        return previousJobGroups.get(previousJobGroups.size() - 1);
    }
}
