package model.goals;

import java.util.List;

public interface IGoalTracker {
	List<IGoal> getGoals();
	double computeGoalProgress(int year);
}
