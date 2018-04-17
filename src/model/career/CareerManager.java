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

public class CareerManager {
	private APersonalInfoPerson person;
	private Optional<Job> currentJob;
	private ArrayList<Job> previousJobs;

	public ArrayList<Job> getPreviousJobs() {
		return previousJobs;
	}

	private AOccupation occupation;
	private boolean retired;
	private Random r;
	private int jobAttempts;
	private int maxAttempts;
	private AOccupation[] careerRanking;

	public CareerManager(APersonalInfoPerson person) {
		this.person = person;
		this.currentJob = Optional.empty();
		this.previousJobs = new ArrayList<>();
		this.retired = false;
		this.r = new Random();
		this.maxAttempts = calculateMaxAttempts();
		this.careerRanking = rankCareers();
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
		if (!retired) {
			if (occupation == null || jobAttempts >= maxAttempts) {
				occupation = chooseCareer();
				currentJob = attemptFindJob();
				if (currentJob.isPresent()) {
					this.person.addLifeEvent(new JoinedJobEvent(this.person, currentJob.get(), year));
				}
			} else if (!currentJob.isPresent()) {
				currentJob = attemptFindJob();
				if (currentJob.isPresent()) {
					this.person.addLifeEvent(new JoinedJobEvent(this.person, currentJob.get(), year));
				}
			} else {
				jobAttempts = 0;
				if (attemptQuit(year))
					return;
				attemptTakeLeave(year);

				currentJob.get().work(this);
				if (person.getAge() > 65){
					retired = attemptRetire();
					if(retired && currentJob.isPresent()){
						this.person.addLifeEvent(new RetirementEvent(this.person, currentJob.get(), year));
					}
				}
			}
		}
	}

	public APersonalInfoPerson getPerson() {
		return person;
	}

	public AOccupation chooseCareer() {
		jobAttempts = 0;
		if (occupation != null) {
			for (int i = 0; i < careerRanking.length - 1; i++) {
				if (occupation.getName() == careerRanking[i].getName()) {
					careerRanking[i] = careerRanking[i + 1];
					careerRanking[i + 1] = occupation;
				}
			}
		}

		for (AOccupation oc : careerRanking) {
			if (occupation == null || occupation.getName() != oc.getName()) {
				double roll = r.nextDouble();
				if (roll < 0.7)
					return oc;
			}
		}

		for (AOccupation oc : careerRanking) {
			if (occupation == null || occupation.getName() != oc.getName()) {
				return oc;
			}
		}

		return careerRanking[0];
	}

	public Optional<Job> attemptFindJob() {
		if (occupation.interview(person)) {
			int level = 0;
			if (!previousJobs.isEmpty()) {
				for (int i = previousJobs.size() - 1; i >= 0; i--) {
					if (previousJobs.get(i).getOccupation().getName() == occupation.getName()) {
						double roll = r.nextDouble();
						int levelMod = roll < 0.4 ? roll < 0.15 ? -1 : 1 : 0;
						level = previousJobs.get(previousJobs.size() - 1).getLevel() + levelMod;
						level = Math.max(0, Math.min(level, 9));
						break;
					}
				}
			}
			return Optional.of(new Job(occupation, level));
		}
		jobAttempts++;
		return Optional.empty();
	}

	public boolean attemptRetire() {
		double tenacity = person.getRole().getCareerTenacity();
		double chance = (1 - tenacity) / 2 + 0.1;
		return r.nextDouble() < chance;
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

	public void fired(Job j) {
		previousJobs.add(j);
		currentJob = Optional.empty();
	}

	public void quitJob() {
		currentJob.get().sendResignation();
		previousJobs.add(currentJob.get());
		currentJob = Optional.empty();
	}

	public Job currentJob() {
		if (currentJob.isPresent()) {
			return currentJob.get();
		}
		return null;
	}
}
