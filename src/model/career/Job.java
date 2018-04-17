package model.career;

import model.career.occupations.AOccupation;

import java.util.Random;

public class Job {
    private AOccupation occupation;
    private int level;
    private double performance;
    private int status; //active, fired, quit, on leave
    private Random r;

    public Job(AOccupation occupation, int level) {
        this.occupation = occupation;
        this.level = level;
        this.performance = 0;
        this.status = 0;
        this.r = new Random();
    }

    public void work(CareerManager manager) {
        if(status == 0) {
            performance += progressJob();
            performance += manager.getPerson().getRole().computeCareerProgressionModifier();
            if (performance >= 1) {
                if (level < 9) {
                    level++;
                    performance = 0;
                    //System.out.println(manager.getPerson().getFullName() + " promoted to " + level);
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
  
    private double progressJob() {
      double p = (double)(r.nextInt(8) - 2) / 10;
      double roll = r.nextDouble();
      if(roll < 0.3) {
          p += roll < 0.1 ? 0.8 : -0.8;
      }
      return p;
    }

    public int getLevel() {
        return level;
    }
  
  	public Integer getRank() {
      return level;
    }

    public Double getPerformance() {
      return performance;
    }

    public AOccupation getOccupation() {
        return occupation;
    }
  
  	public String getJobTypeTitle() {
      return occupation.getName();
    }
  
  	public String getStatus() {
      switch (status) {
      case 0:
        return "Active";
      case 1:
        return "Fired";
      case 2:
        return "Quit";
      case 3:
        return "On Leave";
      default:
        throw new IllegalStateException("Invalid state: " + status);
      }
    }
}
