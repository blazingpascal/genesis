package model.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import model.GeneologyRules;
import model.Sex;
import model.genetics.GeneticsMap;

public abstract class APersonalInfoPerson implements IPerson {

	// Genetics!
	protected GeneticsMap genes;
	
	// Personal Info
	protected String firstName;
	protected final String birthLastName;
	protected final Sex sex;
	protected int age;
	protected boolean living = true;
	protected String person_id;
	protected final int generation;
	protected final int birthYear;
	protected int deathYear;
	protected int yearsSinceLastChild = 0;
	protected String currentLastName;
	protected boolean isMourningSpouse = false;
	protected int timeMourningSpouse = 0;
	protected HashSet<String> foundingLastNames = new HashSet<String>();
	protected Role role;

	protected APersonalInfoPerson(String firstName, String lastName, Sex sex, int age, int generation, int birthYear,
			String person_id, GeneticsMap genes, Role role) {
		this.firstName = firstName;
		this.currentLastName = lastName;
		this.birthLastName = lastName;
		this.sex = sex;
		this.age = age;
		this.generation = generation;
		this.birthYear = birthYear;
		this.person_id = person_id;
		this.genes = genes;
		this.role = role;
	}

	@Override
	public int getGeneration() {
		return generation;
	}

	public String basicPersonalInfo() {
		return "Name: " + getNameString() + "\nSex: " + this.sex + "\nAge: " + this.age + " " + livingStateString()
				+ "\nMarital Status: " + getMaritalStatusString();
	}

	public String getFullName() {
		return this.firstName + " " + this.currentLastName;
	}

	public void setLastName(String newLastName) {
		this.currentLastName = newLastName;
	}

	public void incrementAge() {
		if (living) {
			this.age++;
		}
		if (isMourningSpouse) {
			this.timeMourningSpouse++;
		}
		this.yearsSinceLastChild++;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getCurrentLastName() {
		return currentLastName;
	}

	public String getBirthLastName() {
		return birthLastName;
	}

	public Sex getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public boolean isLiving() {
		return this.living;
	}

	public int getBirthYear() {
		return this.birthYear;
	}

	public int getDeathYear() {
		return this.deathYear;
	}

	public int getYearsSinceLastChild() {
		return this.yearsSinceLastChild;
	}

	@Override
	public String getId() {
		return this.person_id;
	}

	String getMaritalStatusString() {
		if (isSingle())
			return "Single";
		return "Married to " + getSpouse().getFullName();
	}

	String livingStateString() {
		return !living ? "(Deceased)" : "";
	}

	String getNameString() {
		if (currentLastName.equals(birthLastName)) {
			return firstName + " " + currentLastName;
		} else {
			return firstName + " " + currentLastName + " nee " + birthLastName;
		}
	}

	public void resetYearsSinceLastChild() {
		this.yearsSinceLastChild = 0;
	}

	public IPerson createChildWith(IPerson parent, int year, Random r) {
		if (this.sex == parent.getSex()) {
			throw new IllegalArgumentException("Parent sexes must be different");
		}
		Sex sex = r.nextDouble() < 0.5 ? Sex.FEMALE : Sex.MALE;
		List<IPerson> myChildren = this.getChildren();
		List<IPerson> theirChildren = this.getChildren();
		String firstName = GeneologyRules.getRandomFirstName(sex, new Random(r.nextInt()));
		if (sex == this.sex) {
			if (!myChildren.stream().anyMatch(c -> c.getFirstName().startsWith(this.firstName))) {
				firstName = this.firstName;
			}
		} else {
			// Check if other parent has an heir yet.
			if (!theirChildren.stream().anyMatch(c -> c.getFirstName().startsWith(parent.getFirstName()))) {
				firstName = parent.getFirstName();
			}
		}

		GeneticsMap childGenes;
		if(this.sex == Sex.FEMALE){
			childGenes = this.genes.combine(parent.getGenes(), r);
		} else{
			childGenes = parent.getGenes().combine(this.genes, r);
		}
		
		APersonalInfoPerson child = createPerson(firstName, this.currentLastName, sex, 0,
				Math.max(this.generation, parent.getGeneration()) + 1, year, childGenes, Role.calculateRole(this.role, parent.getRole()));

		if (this.sex == Sex.FEMALE) {
			child.setMother(this);
			child.setFather(parent);
		} else {
			child.setFather(this);
			child.setMother(parent);
		}
		if (this.foundingLastNames.isEmpty()) {
			child.foundingLastNames.add(this.birthLastName);
		} else {
			child.foundingLastNames.addAll(this.foundingLastNames);
		}
		if (parent.getFoundingLastNames().isEmpty()) {
			child.foundingLastNames.add(parent.getBirthLastName());
		} else {
			child.foundingLastNames.addAll(parent.getFoundingLastNames());
		}
		this.addChild(child);
		parent.addChild(child);
		this.resetYearsSinceLastChild();
		parent.resetYearsSinceLastChild();

		return child;
	}

	protected abstract APersonalInfoPerson createPerson(String firstName2, String currentLastName2, Sex sex2, int age,
			int generation, int birthYear, GeneticsMap genes, Role role);

	protected abstract void setFather(IPerson parent);

	protected abstract void setMother(IPerson parent);

	public void kill(int deathYear) {
		this.living = false;
		makeSpouseWidow(deathYear);
		this.deathYear = deathYear;
	}

	protected abstract void makeSpouseWidow(int deathYear);

	@Override
	public boolean atLeastCousins(IPerson p) {
		List<IPerson> myGrandparents = this.getGrandparents();
		List<IPerson> theirGrandparents = p.getGrandparents();
		IPerson myMother = this.getMother();
		IPerson myFather = this.getFather();
		IPerson theirFather = p.getFather();
		IPerson theirMother = p.getMother();
		if (p.equals(myMother) || p.equals(myFather) || this.getChildren().contains(p)
				|| (theirMother != null && theirMother.equals(myMother))
				|| (theirFather != null && theirFather.equals(myFather)) || myGrandparents.contains(theirMother)
				|| myGrandparents.contains(theirFather) || theirGrandparents.contains(myMother)
				|| theirGrandparents.contains(myFather) 
				|| theirGrandparents.contains(this) || myGrandparents.contains(p)) {
			return true;
		}
		for (IPerson gp : myGrandparents) {
			if (theirGrandparents.contains(gp)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IPerson> getGrandparents() {
		List<IPerson> list = new ArrayList<IPerson>();
		list.add(getMaternalGrandmother());
		list.add(getMaternalGrandfather());
		list.add(getPaternalGrandfather());
		list.add(getPaternalGrandmother());
		list.removeIf(p -> p == null);
		assert !list.contains(null);
		return list;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof APersonalInfoPerson) {
			return this.person_id.equals(((APersonalInfoPerson) o).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return person_id.hashCode();
	}

	@Override
	public void startMourning() {
		this.isMourningSpouse = true;
		this.timeMourningSpouse = 0;
	}

	@Override
	public int getTimeMourningSpouse() {
		return this.timeMourningSpouse;
	}

	public boolean isMourningSpouse() {
		return this.isMourningSpouse;
	}

	public void stopMourning() {
		this.isMourningSpouse = false;
	}

	public String getFullBirthName() {
		return this.getFirstName() + " " + this.getBirthLastName();
	}

	@Override
	public Collection<String> getFoundingLastNames() {
		return this.foundingLastNames;
	}
	
	@Override
	public GeneticsMap getGenes(){
		return this.genes;
	}
	
	@Override
	public Role getRole(){
		return this.role;
	}
}
