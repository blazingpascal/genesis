package model.person.idbased;

import model.Sex;
import model.genetics.GeneticsMap;
import model.person.IPerson;
import model.person.Role;
import model.personality.IPersonality;

public interface IIdBasedPerson extends IPerson {
	public static IIdBasedPerson create(String firstName, String lastName, 
			Sex sex, int age, int generation, int birthYear, int genesis_id, 
			GeneticsMap genes, Role r, IPersonality personality) {
		return new IdBasedPersonImpl(firstName, lastName, sex, age, generation, 
				birthYear, genesis_id, genes,r, personality);
	}
}
