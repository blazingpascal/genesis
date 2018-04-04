package model.genesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.Sex;
import model.genetics.GeneticsMap;
import model.person.IPerson;
import model.person.ARole;
import model.personality.IPersonality;

public class GenesisImpl extends AGenesis {

	private List<IPerson> people = new ArrayList<IPerson>();
	public static long PEOPLE_COUNT = 0;

	@Override
	public IPerson addSinglePerson(String firstName, String lastName, Sex sex, 
			int age, GeneticsMap genes, ARole r, IPersonality personality) {
		IPerson p = IPerson.createBasicPerson(firstName, lastName, sex, age, 0, timeInYears - age);
		people.add(p);
		PEOPLE_COUNT++;
		return p;
	}

	public int getTimeInYears() {
		return timeInYears;
	}

	@Override
	public long getYear() {
		return timeInYears;
	}

	@Override
	public List<IPerson> historicalPopulation() {
		return this.people;
	}

	@Override
	public void incrementIdCount() {
		PEOPLE_COUNT++;

	}

	@Override
	public long getIdCount() {
		return PEOPLE_COUNT;
	}

	@Override
	protected void addChild(IPerson person) {
		this.people.add(person);
	}

	@Override
	public List<IPerson> getByIds(String... ids) {
		List<IPerson> people = new ArrayList<IPerson>();
		for (IPerson p : this.people) {
			for (int i = 0; i < ids.length; i++) {
				if(p.getId().equals(ids[i])){
				people.add(p);
				break;
				}
			}
		}
		return people;
	}

}
