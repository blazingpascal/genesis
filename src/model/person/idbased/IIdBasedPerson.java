package model.person.idbased;

import model.Sex;
import model.genetics.GeneticsMap;
import model.person.IPerson;
import model.person.Role;

public interface IIdBasedPerson extends IPerson {
	public static IIdBasedPerson create(String firstName, String lastName, Sex sex, int age, int generation,
			int birthYear, int genesis_id, GeneticsMap genes, Role r) {
		return new IdBasedPersonImpl(firstName, lastName, sex, age, generation, birthYear, genesis_id, genes,r );
	}
}
