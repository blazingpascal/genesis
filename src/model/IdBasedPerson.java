package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IdBasedPerson extends APersonalInfoPerson {

	int genesisId;
	String motherId = null;
	String fatherId = null;
	String spouseId = null;
	String maternalGrandmother = null;
	String paternalGrandmother = null;
	String maternalGrandfather = null;
	String paternalGrandfather = null;
	HashMap<String, Integer> spouseIdAnniversaryHistory = new HashMap<String, Integer>();
	List<String> childrenIds = new ArrayList<String>();
	List<String> ancestorIds = new ArrayList<String>();
	List<String> relatedIds = new ArrayList<String>();

	protected IdBasedPerson(String firstName, String lastName, Sex sex, int age, int generation, int birthYear,
			int genesisId) {
		super(firstName, lastName, sex, age, generation, birthYear,
				IdGenesisMapCollection.getGenesisIdCount(genesisId));
		this.genesisId = genesisId;
	}

	@Override
	public boolean isSingle() {
		return spouseId == null;
	}

	@Override
	public boolean relationLevelMax(IPerson p2, int depth) {
		/*
		 * return depth > 0 && (this.equals(p2) ||
		 * this.childrenIds.contains(p2.getId()) ||
		 * this.getChildren().stream().anyMatch(c -> relationLevelMax(c, depth))
		 * || (this.motherId != null && this.getMother().relationLevelMax(p2,
		 * depth - 1)) || (this.fatherId != null &&
		 * this.getFather().relationLevelMax(p2, depth - 1)));
		 */

		// go up depth levels
		List<IPerson> ancestors = new ArrayList<IPerson>();

		if (relatedIds.contains(p2.getId())) {
			return true;
		}

		if (this.ancestorIds.size() > 0) {
			ancestors = this.getPersonsByIdFromHomeGenesis(ancestorIds.toArray(new String[] {}));
		} else {
			// get mother's ancestors
			List<IPerson> mAncestors = getAncestors(getMother(), depth);

			// get father's ancestors
			List<IPerson> fAncestors = getAncestors(getFather(), depth);

			ancestors.addAll(mAncestors);
			ancestors.addAll(fAncestors);
		}

		// Crawl from these ancestors to see if anyone is related.
		while (!ancestors.isEmpty()) {
			IPerson p = ancestors.remove(0);
			if (p.equals(p2)) {
				relatedIds.add(p2.getId());
				return true;
			}
			ancestors.addAll(p.getChildren());
		}
		return false;

	}

	private List<IPerson> getAncestors(IPerson person, int depth) {
		if (person == null) {
			return new ArrayList<IPerson>();
		}
		if (!ancestorIds.isEmpty()) {
			return getPersonsByIdFromHomeGenesis(ancestorIds.toArray(new String[] {}));
		}
		List<IPerson> ancestors = new ArrayList<IPerson>();
		List<IPerson> ancSubset = new ArrayList<IPerson>();
		IPerson mother = person.getMother();
		IPerson father = person.getFather();
		if (mother != null) {
			ancSubset.add(mother);
		}
		if (father != null) {
			ancSubset.add(father);
		}

		int level = depth;
		while (level > 0) {
			List<IPerson> ancSubset2 = new ArrayList<IPerson>();
			while (ancSubset.size() > 0) {
				IPerson p = ancSubset.remove(0);
				IPerson pMother = p.getMother();
				IPerson pFather = p.getFather();
				if (pMother != null) {
					ancSubset2.add(pMother);
				}
				if (pFather != null) {
					ancSubset2.add(pFather);
				}
				if (pMother == null || pFather == null) {
					ancestors.add(p);
				}
			}
			ancSubset.addAll(ancSubset2);
			level--;
		}
		ancestors.addAll(ancSubset);
		return ancestors;

	}

	@Override
	public List<IPerson> getChildren() {
		IIdBasedGenesis genesis = getHomeGenesis();
		List<IPerson> children = genesis.getByIds(childrenIds.toArray(new String[] {}));
		return children;
	}

	@Override
	public boolean isMarriedTo(IPerson p2) {
		return p2.getId().equals(spouseId);
	}

	@Override
	public void setSpouse(IPerson person) {
		if (person == null) {
			this.spouseId = null;
		} else {
			this.spouseId = person.getId();
		}
	}

	@Override
	public void addSpouse(IPerson person, int year) {
		this.spouseIdAnniversaryHistory.put(person.getId(), year);
	}

	@Override
	public IPerson getSpouse() {
		if (this.spouseId == null) {
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.spouseId).get(0);
	}

	@Override
	public void marry(IPerson fiance, int year) {
		String newLastName = GeneologyRules.getMarriedLastName(this, fiance);
		this.currentLastName = newLastName;
		fiance.setLastName(newLastName);
		fiance.setSpouse(this);
		this.addSpouse(fiance, year);
		fiance.addSpouse(this, year);
		this.spouseId = fiance.getId();
	}

	@Override
	public void addChild(IPerson child) {
		this.childrenIds.add(child.getId());
	}

	private IIdBasedGenesis getHomeGenesis() {
		return IdGenesisMapCollection.getIdGenesis(this.genesisId);
	}

	private List<IPerson> getPersonsByIdFromHomeGenesis(String... ids) {
		IIdBasedGenesis genesis = getHomeGenesis();
		return genesis.getByIds(ids);
	}

	@Override
	protected void setFather(IPerson parent) {
		if (parent == null) {
			this.fatherId = null;
		} else {
			this.fatherId = parent.getId();
			IPerson grandmother = parent.getMother();
			IPerson grandfather = parent.getFather();
			if(grandmother != null){
				this.paternalGrandmother = grandmother.getId();
			}
			if(grandfather != null){
				this.paternalGrandfather = grandfather.getId();
			}
		}

	}

	@Override
	protected void setMother(IPerson parent) {
		if (parent == null) {
			this.motherId = null;
		} else {
			this.motherId = parent.getId();
			IPerson grandmother = parent.getMother();
			IPerson grandfather = parent.getFather();
			if(grandmother != null){
				this.maternalGrandmother = grandmother.getId();
			}
			if(grandfather != null){
				this.maternalGrandfather = grandfather.getId();
			}
		}
	}

	@Override
	protected void makeSpouseWidow() {
		if (getSpouse() != null) {
			getSpouse().setSpouse(null);
		}
	}

	@Override
	public IPerson getMother() {
		if (this.motherId == null)
			return null;
		return this.getPersonsByIdFromHomeGenesis(this.motherId).get(0);
	}

	@Override
	public IPerson getFather() {
		if (this.fatherId == null)
			return null;
		return this.getPersonsByIdFromHomeGenesis(this.fatherId).get(0);
	}

	@Override
	protected APersonalInfoPerson createPerson(String firstName, String lastName, Sex sex, int age, int generation,
			int birthYear) {
		return new IdBasedPerson(firstName, lastName, sex, age, generation, birthYear, this.genesisId);
	}

	@Override
	public void kill(int deathYear) {
		super.kill(deathYear);
		notifyGenesisOfDeath();
	}

	private void notifyGenesisOfDeath() {
		getHomeGenesis().reactToDeath(this);
	}

	@Override
	public void incrementAge() {
		super.incrementAge();
		if (this.age == GeneologyRules.MIN_MARRIAGE_AGE) {
			notifyGenesisOfPairablePerson();
		}
		if (this.sex == Sex.MALE) {
			if (this.age == GeneologyRules.MALE_MIN_FERTILE_AGE) {
				notifyGenesisOfFertility();
			}
			if (this.age == GeneologyRules.MALE_MAX_FERTILE_AGE) {
				notifyGenesisOfInfertility();
			}
		} else {
			if (this.age == GeneologyRules.FEMALE_MIN_FERTILE_AGE) {
				notifyGenesisOfFertility();
			}
			if (this.age == GeneologyRules.FEMALE_MAX_FERTILE_AGE) {
				notifyGenesisOfInfertility();
			}
		}
	}

	private void notifyGenesisOfInfertility() {
		getHomeGenesis().reactToInfertility(this);

	}

	private void notifyGenesisOfFertility() {
		getHomeGenesis().reactToFertility(this);

	}

	private void notifyGenesisOfPairablePerson() {
		getHomeGenesis().reactToPairability(this);

	}

	@Override
	public Map<IPerson, Integer> getSpousalHistory() {
		HashMap<IPerson, Integer> map = new HashMap<IPerson, Integer>();
		for(Entry<String, Integer> entry : spouseIdAnniversaryHistory.entrySet()){
			map.put(getPersonsByIdFromHomeGenesis(entry.getKey()).get(0), entry.getValue());
		}
		return map;
	}

	@Override
	public IPerson getMaternalGrandmother() {
		if(this.maternalGrandmother == null){
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.maternalGrandmother).get(0);
	}

	@Override
	public IPerson getPaternalGrandmother() {
		if(this.paternalGrandmother == null){
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.paternalGrandmother).get(0);
	}

	@Override
	public IPerson getMaternalGrandfather() {
		if(this.maternalGrandfather == null){
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.maternalGrandfather).get(0);
	}

	@Override
	public IPerson getPaternalGrandfather() {
		if(this.paternalGrandfather == null){
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.paternalGrandfather).get(0);
	}
}
