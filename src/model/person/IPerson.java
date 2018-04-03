package model.person;

import java.util.*;

import model.Sex;
import model.genetics.GeneticsMap;
import model.person.idbased.IIdBasedPerson;
import model.relationship.IRelationship;
import model.spousehistory.ISpouseHistory;

public interface IPerson {

	String getFullName();

	String basicPersonalInfo();

	Sex getSex();

	int getAge();

	boolean isSingle();

	boolean relationLevelMax(IPerson p2, int depth);

	String getCurrentLastName();

	List<IPerson> getChildren();

	boolean isMarriedTo(IPerson p2);

	int getYearsSinceLastChild();

	void setLastName(String newLastName);

	void setSpouse(IPerson person);

	void addSpouse(IPerson person, int anniversaryYear);

	String getId();
	
	IPerson getSpouse();
	
	void marry(IPerson fiance, int anniversaryYear);

	boolean isLiving();
	
	public void incrementAge();

	IPerson createChildWith(IPerson candidate2, int timeInYears, Random random);

	void resetYearsSinceLastChild();

	void addChild(IPerson child);

	int getGeneration();

	void kill(int deathYear);
	
	IPerson getMother();
	
	IPerson getFather();

	String getFirstName();

	String getBirthLastName();

	int getDeathYear();

	int getBirthYear();
	
	List<ISpouseHistory> getSpousalHistory();

	boolean atLeastCousins(IPerson p2);

	List<IPerson> getGrandparents();
	
	IPerson getMaternalGrandmother();
	IPerson getPaternalGrandmother();
	IPerson getMaternalGrandfather();
	IPerson getPaternalGrandfather();

	void makeWidow(int endYear);

	void startMourning();

	int getTimeMourningSpouse();

	boolean isMourningSpouse();

	void stopMourning();

	public static IPerson createBasicPerson(String firstName, String lastName, Sex sex, int age, int generation, int birthYear){
		// TODO fix this
		return new Person(firstName, lastName, sex, age, generation, birthYear);
	}
	
	public static IPerson createIdBasedPerson(String firstName, String lastName, Sex sex, int age, int generation, int birthYear, int genesis_id, GeneticsMap genes, Role r){
		return IIdBasedPerson.create(firstName, lastName, sex, age, generation, birthYear, genesis_id, genes, r);
	}

	String getFullBirthName();

	Collection<String> getFoundingLastNames();

	GeneticsMap getGenes();

	Role getRole();

  int getPreferredTrait(String s);

  int sumUpPreferences(IPerson other);

	Map<IPerson, IRelationship> getRelationships();

	void addRelationship(IPerson other, IRelationship relationship);

	void meet(IPerson other, int year);

	boolean knows(IPerson other);

	void setSignificantOther(IPerson other);

	boolean hasSignificantOther();

	IPerson getSignificantOther();
}
