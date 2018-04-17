package model.goals;

import java.util.Random;

import model.person.IPerson;

public class RelationshipStartAction implements IAction {

	private final IPerson p1;
	private final IPerson p2;
	private final int actionPointValue = 2;
	private boolean occurred;
	
	public RelationshipStartAction(IPerson p1, IPerson p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public boolean reverse() {
		if(occurred){
			p1.getRelationships().remove(p2);
			p2.getRelationships().remove(p1);
			return true;
		}
		return false;
	}

	@Override
	public String title() {
		return String.format("%s meet %s", p1.getId(), p2.getId());
	}

	@Override
	public String description() {
		return String.format("Some flavor text");
	}

	@Override
	public int actionPointValue() {
		return this.actionPointValue;
	}

	@Override
	public void enact(int year, Random r) {
		p1.meet(p2, year);
		this.occurred = true;
	}

}
