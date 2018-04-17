package model.goals;

import java.util.List;

public interface IGoal {
	double computeProgress(long year);
	
	String getTitle();
	String getDescription();
	double computeHypotheticalProgress(List<IAction> actions, long year);
}
