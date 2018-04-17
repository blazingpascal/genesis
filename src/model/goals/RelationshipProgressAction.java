package model.goals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.person.IPerson;
import model.relationship.IRelationship;
import model.relationship.RelationshipType;
import model.spousehistory.ISpouseHistory;

public class RelationshipProgressAction implements IAction{
	private final IPerson p1;
	private final IPerson p2;
	private final int actionPointValue = 1;
	// Save old info
	boolean occurred = false;
	private double p1ToP2regard = -1;
	private double p2ToP1regard = -1;
	private double p1ToP2desire = -1;
	private double p2ToP1desire = -1;
	private  List<ISpouseHistory> oldp1sh;
	private  List<ISpouseHistory> oldp2sh;
	private RelationshipType oldType;
	
	public RelationshipProgressAction(IPerson p1, IPerson p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public void enact(int year, Random r) {
		IRelationship p1toP2 = this.p1.getRelationships().get(p2);
		IRelationship p2toP1 = this.p2.getRelationships().get(p1);
		p1ToP2regard = p1toP2.regard();
		p2ToP1regard = p2toP1.regard();
		p1ToP2desire = p1toP2.romanticDesire();
		p2ToP1desire = p2toP1.romanticDesire();
		oldType = p1toP2.getType();
		oldp1sh = new ArrayList<ISpouseHistory>(p1.getSpousalHistory());
		oldp2sh = new ArrayList<ISpouseHistory>(p2.getSpousalHistory());
		p1toP2.progressRelationship(year, r);
		p2toP1.progressRelationship(year, r);
		occurred = true;

	}
	@Override
	public boolean reverse() {
		if(occurred){
			IRelationship p1toP2 = p1.getRelationships().get(p2);
			IRelationship p2toP1 = p2.getRelationships().get(p1);
			p1toP2.setRegard(p1ToP2regard);
			p2toP1.setRegard(p2ToP1regard);
			p1toP2.setDesire(p1ToP2desire);
			p2toP1.setDesire(p2ToP1desire);
			p1toP2.setType(oldType);
			p2toP1.setType(oldType);
			p1.setSpouseHistory(oldp1sh);
			p2.setSpouseHistory(oldp2sh);
			occurred = false;
			return true;
		}
		return false;
	}
	@Override
	public String title() {
		return String.format("%s flirts with %s", p1.getId(), p2.getId());
	}
	@Override
	public String description() {
		return "Some flavor text";
	}
	@Override
	public int actionPointValue() {
		return actionPointValue;
	}
}
