package model.person;

import java.util.*;

import model.GeneologyRules;
import model.Sex;
import model.genesis.GenesisImpl;
import model.genetics.GeneticsMap;
import model.personality.IPersonality;
import model.relationship.IRelationship;
import model.relationship.RelationshipImpl;
import model.spousehistory.ISpouseHistory;

class Person extends APersonalInfoPerson{
	// Family details
	IPerson spouse = null;
	IPerson mother = null;
	IPerson father = null;
	List<IPerson> spouseHistory = new ArrayList<IPerson>();
	private List<IPerson> children = new ArrayList<IPerson>();
	
	Person(String firstName, String lastName, Sex sex, int age, int generation, int birthYear) {
		super(firstName, lastName, sex, age, generation, birthYear, 
				firstName + lastName + GenesisImpl.PEOPLE_COUNT, null, null, 
				null);
	}
	
	public String basicFamilyInfo() {
		return getMotherString() + "\n" + getFatherString() + "\n" + getSpouseString() + "\n" + getChildrenString();
	}

	private String getChildrenString() {
		StringBuilder sb = new StringBuilder("Children:");
		if (children.isEmpty()) {
			sb.append("None");
			return sb.toString();
		}
		for (IPerson child : children) {
			sb.append("\n");
			sb.append(child.basicPersonalInfo());
		}
		return sb.toString();
	}

	private String getSpouseString() {
		StringBuilder sb = new StringBuilder("Spouse:\n");
		if (spouse == null) {
			sb.append("None");
			return sb.toString();
		}
		sb.append(spouse.basicPersonalInfo());
		return sb.toString();
	}

	private String getFatherString() {
		StringBuilder sb = new StringBuilder("Father:\n");
		if (father == null) {
			sb.append("None");
			return sb.toString();
		}
		sb.append(father.basicPersonalInfo());
		return sb.toString();
	}

	private String getMotherString() {
		StringBuilder sb = new StringBuilder("Mother:\n");
		if (mother == null) {
			sb.append("None");
			return sb.toString();
		}
		sb.append(mother.basicPersonalInfo());
		return sb.toString();
	}

	public boolean tryForBaby(APersonalInfoPerson parent, Random r) {
		if (parent.getSex() == this.sex) {
			throw new IllegalArgumentException("Parent sexes must be different");
		}
		double fertilityChance = GeneologyRules.fertilityChanceCouple(this, parent);
		double roll = r.nextDouble();
		return roll < fertilityChance;
	}

	protected void setFather(IPerson parent) {
		this.father = parent;
	}

	protected void setMother(IPerson person) {
		this.mother = person;
	}

	public void addChild(IPerson child) {
		this.children.add(child);
	}

	public IPerson getSpouse() {
		return spouse;
	}

	public IPerson getMother() {
		return mother;
	}

	public IPerson getFather() {
		return father;
	}

	public List<IPerson> getChildren() {
		return children;
	}

	@Override
	public boolean relationLevelMax(IPerson p2, int depth) {
		return depth > 0 && (this.equals(p2) || this.children.contains(p2)
				|| (this.mother != null && this.mother.relationLevelMax(p2, depth - 1))
				|| (this.father != null && this.father.relationLevelMax(p2, depth - 1)));
	}

	@Override
	public boolean isMarriedTo(IPerson p2) {
		return this.spouse != null && this.spouse.equals(p2);
	}

	public boolean isFounder() {
		return mother == null && father == null;
	}

	public boolean isSingle() {
		return spouse == null;
	}

	public void marry(IPerson fiance, int year) {
		this.spouse = fiance;
		String newLastName = GeneologyRules.getMarriedLastName(this, fiance);
		this.currentLastName = newLastName;
		fiance.setLastName(newLastName);
		fiance.setSpouse(this);
		this.addSpouse(this, year);
		spouse.addSpouse(this, year);
	}

	@Override
	public void setSpouse(IPerson person) {
		this.spouse = person;
	}

	@Override
	public void addSpouse(IPerson person, int year) {
		this.spouseHistory.add(person);
	}

	@Override
	protected void makeSpouseWidow(int deathYear) {
		if(spouse != null){
			this.spouse.setSpouse(null);
		}
	}

	@Override
	protected APersonalInfoPerson createPerson(String firstName, String lastName, Sex sex, int age,
			int generation, int birthYear, GeneticsMap genes, Role role, 
			IPersonality personality) {
		return new Person(firstName, lastName, sex, age, generation, birthYear);
	}

	@Override
	public List<ISpouseHistory> getSpousalHistory() {
		throw new IllegalStateException("Not implemented yet");
	}

	@Override
	public IPerson getMaternalGrandmother() {
		if(this.mother != null){
			return this.mother.getMother();
		}
		return null;
	}

	@Override
	public IPerson getPaternalGrandmother() {
		if(this.father != null){
			return this.father.getMother();
		}
		return null;
	}

	@Override
	public IPerson getMaternalGrandfather() {
		if(this.mother != null){
			return this.mother.getFather();
		}
		return null;
	}

	@Override
	public IPerson getPaternalGrandfather() {
		if(this.father != null){
			return this.father.getFather();
		}
		return null;
	}

	@Override
	public void makeWidow(int endYear) {
		this.spouse = null;
	}

}
