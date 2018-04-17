package model.goals;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.person.IPerson;

public class GoalTrackerImpl implements IGoalTracker{

	private final RomanticGoal romanticGoal;
	private final PlatonicGoal platonicGoal;
	private final CareerGoal careerGoal;
	// Narrative goals may have happened in a better run.
	//private final NarrativeGoal narrativeGoal;
	
	public GoalTrackerImpl(IPerson p, Random r){
		romanticGoal = new RomanticGoal(p, r);
		platonicGoal = new PlatonicGoal(p, r);
		careerGoal = new CareerGoal(p, r);
	}
	
	@Override
	public List<IGoal> getGoals() {
		return Arrays.asList(romanticGoal, platonicGoal, careerGoal);
	}

	@Override
	public double computeGoalProgress(int year) {
		return (romanticGoal.computeProgress(year) + platonicGoal.computeProgress(year))/2;
	}
	
}
