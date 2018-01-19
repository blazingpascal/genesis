package model.genesis.idbased;

import java.util.List;

import model.genesis.IGenesis;
import model.person.IPerson;

public interface IIdBasedGenesis extends IGenesis {

	void reactToDeath(IPerson person, int deathYear);

	void reactToInfertility(IPerson person, int infertileYear);

	void reactToFertility(IPerson person, int fertileYear);

	void reactToPairability(IPerson person, int comingOfAgeYear);

	void reactToWidowhood(IPerson p, int widowYear);

	void reactToMarriage(IPerson s1, IPerson s2, int anniversaryYear);
}
