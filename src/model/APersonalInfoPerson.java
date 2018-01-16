package model;

import java.util.List;
import java.util.Random;

abstract class APersonalInfoPerson implements IPerson {

	protected String firstName;
	protected final String birthLastName;
	protected final Sex sex;
	protected int age;
	protected boolean living = true;
	protected long person_id;
	protected final int generation;
	protected final int birthYear;
	protected int deathYear;
	protected int yearsSinceLastChild = 0;
	protected String currentLastName;

	protected APersonalInfoPerson(String firstName, String lastName, Sex sex, int age, int generation, int birthYear, long person_id){
		this.firstName = firstName;
		this.currentLastName = lastName;
		this.birthLastName = lastName;
		this.sex = sex;
		this.age = age;
		this.generation = generation;
		this.birthYear = birthYear;
		this.person_id = person_id;
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
		return Long.toString(this.person_id);
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
	
	public void resetYearsSinceLastChild(){
		this.yearsSinceLastChild = 0;
	}

	public IPerson createChildWith(IPerson parent, int year, Random r) {
		if (this.sex == parent.getSex()) {
			throw new IllegalArgumentException("Parent sexes must be different");
		}
		Sex sex = r.nextDouble() < 0.5 ? Sex.FEMALE : Sex.MALE;
		String firstName = GeneologyRules.getRandomFirstName(sex, new Random(r.nextInt()));
	
		APersonalInfoPerson child = createPerson(firstName, this.currentLastName, sex, 0, Math.max(this.generation, parent.getGeneration()) + 1, year);
	
		if (this.sex == Sex.FEMALE) {
			child.setMother(this);
			child.setFather(parent);
		} else {
			child.setFather(this);
			child.setMother(parent);
		}
		this.addChild(child);
		parent.addChild(child);
		this.yearsSinceLastChild = 0;
		parent.resetYearsSinceLastChild();
		return child;
	}
	
	protected abstract APersonalInfoPerson createPerson(String firstName2, String currentLastName2, Sex sex2, int age, int generation,
			int birthYear);

	protected abstract void setFather(IPerson parent);
	
	protected abstract void setMother(IPerson parent);

	public void kill(int deathYear) {
		this.living = false;
		makeSpouseWidow();
		this.deathYear = deathYear;
	}

	protected abstract void makeSpouseWidow();

}
