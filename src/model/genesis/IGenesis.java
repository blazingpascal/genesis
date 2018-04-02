package model.genesis;

import java.util.List;
import java.util.Random;

import model.Sex;
import model.genetics.GeneticsMap;
import model.person.IPerson;
import model.person.Role;
import model.personality.IPersonality;

public interface IGenesis {
	IPerson addSinglePerson(String firstName, String lastName, Sex sex, int age,
			GeneticsMap genes, Role r, IPersonality p);
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
	int maxGeneration();
}
