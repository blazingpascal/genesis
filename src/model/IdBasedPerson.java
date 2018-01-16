package model;

import java.util.ArrayList;
import java.util.List;

public class IdBasedPerson extends APersonalInfoPerson {

	int genesisId;
	String motherId = null;
	String fatherId = null;
	String spouseId = null;
	List<String> spouseIdHistory = new ArrayList<String>();
	List<String> childrenIds = new ArrayList<String>();

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
		ArrayList<IPerson> ancestors = new ArrayList<IPerson>();

		// get mother's ancestors
		List<IPerson> mAncestors = getAncestors(getMother(), depth);

		// get father's ancestors
		List<IPerson> fAncestors = getAncestors(getFather(), depth);

		ancestors.addAll(mAncestors);
		ancestors.addAll(fAncestors);

		// Crawl from these ancestors to see if anyone is related.
		while (!ancestors.isEmpty()) {
			IPerson p = ancestors.remove(0);
			if (p.equals(p2)) {
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
	public void addSpouse(IPerson person) {
		this.spouseIdHistory.add(person.getId());
	}

	@Override
	public IPerson getSpouse() {
		if (this.spouseId == null) {
			return null;
		}
		return getPersonsByIdFromHomeGenesis(this.spouseId).get(0);
	}

	@Override
	public void marry(IPerson fiance) {
		String newLastName = GeneologyRules.getMarriedLastName(this, fiance);
		this.currentLastName = newLastName;
		fiance.setLastName(newLastName);
		fiance.setSpouse(this);
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
		}

	}

	@Override
	protected void setMother(IPerson parent) {
		if (parent == null) {
			this.motherId = null;
		} else {
			this.motherId = parent.getId();
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

}
