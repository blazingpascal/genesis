package model.goals;

import java.util.List;
import java.util.Random;

public interface IGoal {
	double computeProgress(long year);
	
	String getTitle();
	String getDescription();
	double computeHypotheticalProgress(List<IAction> actions, long year, Random r);
	boolean updateCurrentMaxSuccess(long year);
	double getCurrentMaxSuccess();
}
