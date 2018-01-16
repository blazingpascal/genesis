package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class IdBasedGenesisImpl extends AGenesis implements IIdBasedGenesis {

	private static int GENESIS_COUNT = 0;
	private int genesis_id;
	// private int timeInYears = 1900;
	private HashMap<String, IPerson> map = new HashMap<>();
	private HashMap<String, IPerson> livingMap = new HashMap<>();
	private HashMap<String, IPerson> fertileMap = new HashMap<>();
	private HashMap<String, IPerson> pairableMap = new HashMap<>();
	private HashMap<String, IPerson> femaleMap = new HashMap<>();
	private HashMap<String, IPerson> maleMap = new HashMap<>();
	private long idCount = 0;

	public IdBasedGenesisImpl() {
		genesis_id = GENESIS_COUNT;
		GENESIS_COUNT++;
		IdGenesisMapCollection.addGenesis(this, genesis_id);
	}

	@Override
	public void addSinglePerson(String firstName, String lastName, Sex sex, int age) {
		IdBasedPerson person = new IdBasedPerson(firstName, lastName, sex, age, 0, timeInYears - age, genesis_id);
		String id = person.getId();
		map.put(id, person);
		livingMap.put(id, person);
		if(sex == Sex.MALE){
			maleMap.put(person.getId(), person);
		} else{
			femaleMap.put(person.getId(), person);
		}
		incrementIdCount();
	}

	@Override
	public long getYear() {
		return this.timeInYears;
	}

	@Override
	public void incrementIdCount() {
		idCount++;
	}

	@Override
	public long getIdCount() {
		return this.idCount;
	}

	@Override
	protected void addPerson(IPerson child) {
		this.map.put(child.getId(), child);
		if (child.isLiving()) {
			this.livingMap.put(child.getId(), child);
		}
		if (child.getAge() >= GeneologyRules.MIN_MARRIAGE_AGE) {
			this.pairableMap.put(child.getId(), child);
		}
		if (child.getSex() == Sex.MALE) {
			if (child.getAge() <= GeneologyRules.MALE_MAX_FERTILE_AGE
					&& child.getAge() >= GeneologyRules.MALE_MIN_FERTILE_AGE) {
				this.fertileMap.put(child.getId(), child);
			}
		} else {
			if (child.getAge() <= GeneologyRules.FEMALE_MAX_FERTILE_AGE
					&& child.getAge() >= GeneologyRules.FEMALE_MIN_FERTILE_AGE) {
				fertileMap.put(child.getId(), child);
			}
		}

		incrementIdCount();
	}

	@Override
	public List<IPerson> historicalPopulation() {
		List<IPerson> people = new ArrayList<IPerson>();
		people.addAll(map.values());
		return people;
	}

	@Override
	public List<IPerson> getByIds(String... ids) {
		List<IPerson> people = new ArrayList<IPerson>();
		for (int i = 0; i < ids.length; i++) {
			if (map.get(ids[i]) != null) {
				people.add(map.get(ids[i]));
			} else {
				throw new IllegalArgumentException("No person with id " + ids[i]);
			}
		}
		return people;
	}

	@Override
	public List<IPerson> livingPopulation() {
		List<IPerson> people = new ArrayList<IPerson>();
		people.addAll(livingMap.values());
		return people;
	}

	@Override
	public void reactToDeath(IPerson person) {
		livingMap.remove(person.getId());
	}

	@Override
	public void reactToInfertility(IPerson person) {
		if(fertileMap.containsKey(person.getId())){
			fertileMap.remove(person.getId());
		}
	}

	@Override
	public void reactToFertility(IPerson person) {
		fertileMap.put(person.getId(), person);
		
	}

	@Override
	public void reactToPairability(IPerson person) {
		pairableMap.put(person.getId(), person);
	}
}
