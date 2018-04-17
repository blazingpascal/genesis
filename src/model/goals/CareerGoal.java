package model.goals;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import model.NotImplementedException;
import model.career.CareerManager;
import model.career.Job;
import model.person.CareerRole;
import model.person.CombinationRole;
import model.person.IPerson;

public class CareerGoal extends AGoal {

	private static final double MAX_CAREER_TRACKS = 5;
	private static final int MAX_LEVEL = 8;
	private int careerTracks;
	private int progressHope;

	public CareerGoal(IPerson p, Random r) {
		super("", "", p);
		CombinationRole comboR = (CombinationRole) p.getRole();
		CareerRole role = comboR.getCareer();
		double focus = role.getCareerFocus();
		double tenacity = role.getCareerTenacity();
		this.careerTracks = (int) Math.round((1 - focus) * MAX_CAREER_TRACKS + 1);
		this.progressHope = (int) (tenacity * MAX_LEVEL);
		this.title = String.format("Get to level %d in %d careers", progressHope, careerTracks);
	}

	@Override
	public double computeProgress(long year) {
		double progress = 0;
		CareerManager career = this.refPerson.getCareer();
		Job currentJob = career.currentJob();
		ArrayList<Job> previousJobs = career.getPreviousJobs();
		if(previousJobs == null){
			previousJobs = new ArrayList<Job>();
		}
		if (currentJob != null) {
			previousJobs.add(currentJob);
		}
		HashMap<String, Integer> careerTypes = new HashMap<String, Integer>();
		for (Job j : previousJobs) {
			int level = j.getRank();
			String name = j.getOccupation().getName();
			careerTypes.put(name, careerTypes.containsKey(name) ? Math.max(careerTypes.get(name), level) : level);
		}
		List<Entry<String, Integer>> lst = new ArrayList<Entry<String, Integer>>();
		lst.addAll(careerTypes.entrySet());
		Collections.sort(lst, (e1, e2) -> e1.getValue() - e2.getValue());
		for (int i = 0; i < this.careerTracks && i < lst.size(); i++) {
			Entry<String, Integer> entry = lst.get(i);
			progress += Math.min(1, ((double) entry.getValue()) / this.progressHope) * ((double) 1 / this.careerTracks);
		}
		return progress;
	}

	@Override
	public double computeHypotheticalProgress(List<IAction> actions, long year) {
		throw new IllegalStateException();
	}

}
