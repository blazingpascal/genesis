package model.genesis.idbased;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Sex;
import model.genesis.AGenesis;
import model.lifeevents.BirthLifeEvent;
import model.lifeevents.DeathLifeEvent;
import model.lifeevents.ILifeEvent;
import model.lifeevents.ImmigrationLifeEvent;
import model.lifeevents.MarriageLifeEvent;
import model.person.IPerson;

public class LifeEventEnabledGenesisImpl extends AGenesis implements IIdBasedGenesis, ILifeEventEnabledGenesis {
	private final IdBasedGenesisImpl subGenesis;
	private List<ILifeEvent> lifeEvents = new ArrayList<ILifeEvent>();
	
	public LifeEventEnabledGenesisImpl(){
		this.subGenesis = new IdBasedGenesisImpl();
		// TODO Make more flexible
		IdGenesisMapCollection.addGenesis(this, "SUPER_"+0);
	}
	
	@Override
	public IPerson addSinglePerson(String firstName, String lastName, Sex sex, int age) {
		IPerson p = this.subGenesis.addSinglePerson(firstName, lastName, sex, age);
		this.lifeEvents.add(new ImmigrationLifeEvent(p, getYear()));
		return p;
	}

	@Override
	public long getYear() {
		return subGenesis.getYear();
	}

	@Override
	public List<IPerson> historicalPopulation() {
		return subGenesis.historicalPopulation();
	}

	@Override
	public void incrementIdCount() {
		subGenesis.incrementIdCount();
	}

	@Override
	public long getIdCount() {
		return subGenesis.getIdCount();
	}

	@Override
	public List<IPerson> getByIds(String... ids) {
		return subGenesis.getByIds(ids);
	}

	@Override
	public void reactToDeath(IPerson person, int year) {
		subGenesis.reactToDeath(person, year);
		this.lifeEvents.add(new DeathLifeEvent(person, year));
	}

	@Override
	public void reactToInfertility(IPerson person, int year) {
		subGenesis.reactToInfertility(person, year);
	}

	@Override
	public void reactToFertility(IPerson person, int year) {
		subGenesis.reactToFertility(person, year);
	}

	@Override
	public void reactToPairability(IPerson person, int year) {
		subGenesis.reactToPairability(person, year);
	}

	@Override
	protected void addChild(IPerson child) {
		subGenesis.addChild(child);
		this.lifeEvents.add(new BirthLifeEvent(child));
	}

	@Override
	public void reactToMarriage(IPerson s1, IPerson s2, int anniversaryYear) {
		subGenesis.reactToMarriage(s1, s2, anniversaryYear);
		this.lifeEvents.add(new MarriageLifeEvent(s1,s2, anniversaryYear));
	}

	@Override
	public void reactToWidowhood(IPerson p, int widowYear) {
		subGenesis.reactToWidowhood(p, widowYear);
	}

	@Override
	public List<ILifeEvent> lifeEvents() {
		return this.lifeEvents;
	}

	@Override
	public void incrementTime(Random r) {
		this.subGenesis.incrementTime(r);
	}
	
	@Override
	public int maxGeneration(){
		return this.subGenesis.maxGeneration();
	}
}
