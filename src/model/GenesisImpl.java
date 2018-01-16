package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenesisImpl extends AGenesis {

	private List<IPerson> people = new ArrayList<IPerson>();
	public static long PEOPLE_COUNT = 0;

	@Override
	public void addSinglePerson(String firstName, String lastName, Sex sex, int age) {
		people.add(new Person(firstName, lastName, sex, age, 0, timeInYears - age));
		PEOPLE_COUNT++;
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
	protected void addPerson(IPerson person) {
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
