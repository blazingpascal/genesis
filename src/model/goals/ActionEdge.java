package model.goals;

import org.jgrapht.graph.DefaultWeightedEdge;

import model.person.RelationshipGoalProgressState;

public class ActionEdge extends DefaultWeightedEdge{

	private RelationshipGoalProgressState source;
	private RelationshipGoalProgressState target;

	@Override
	protected Object getSource() {
		return source;
	}

	@Override
	protected Object getTarget() {
		return target;
	}

	private final IAction action;
	private int weight;

	public ActionEdge(RelationshipGoalProgressState source, RelationshipGoalProgressState target, IAction action, int actionPoints){
		this.action = action;
		this.weight = actionPoints;
		this.source = source;
		this.target = target;
	}
	
	@Override
	protected double getWeight() {
		return this.weight;
	}

	public IAction getAction() {
		return action;
	}
}
