package model;

import java.util.List;
import java.util.Random;

public interface IGenesis {
	void addSinglePerson(String firstName, String lastName, Sex sex, int age);
	void incrementTime(Random r);
	void incrementTime(int yearsPast, Random r0);
	long livingPopulationCount();
	long deadPopulationCount();
	long historicalPopulationCount();
	long getYear();
	String livingPersonalPopulationSummary();
	List<IPerson> historicalPopulation();
	List<IPerson> livingPopulation();
	void incrementIdCount();
	long getIdCount();
	List<IPerson> deadPopulation();
	List<IPerson> getByIds(String ... ids);
}
