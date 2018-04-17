package model.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.goals.IAction;

public class RelationshipGoalProgressState {
	final double progress;
	int actionPointsLeft;
	final List<IAction> actions;

	public List<IAction> getActions() {
		return actions;
	}

	RelationshipGoalProgressState(double progress, int actionPointsLeft, List<IAction> actions) {
		this.progress = progress;
		this.actionPointsLeft = actionPointsLeft;
		this.actions = actions;
	}

	public int samenessNumber() {
		return 197 * (int) Math.round(progress / 0.05) + 199 * (actionPointsLeft / 5);
	}
}
