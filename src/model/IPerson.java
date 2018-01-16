package model;

import java.util.List;
import java.util.Random;

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

	void addSpouse(IPerson person);

	String getId();
	
	IPerson getSpouse();
	
	void marry(IPerson fiance);

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

}
