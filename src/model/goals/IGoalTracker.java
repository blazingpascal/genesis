package model.goals;

import java.util.List;
import java.util.Random;

public interface IGoalTracker {
	List<IGoal> getGoals();
	double computeGoalProgress(int year);
	double computeHypotheticalProgress(List<IAction> actions, int year, Random r);
	void updateMaxSuccess(int year);
}
