package model;

import java.util.List;

public interface IIdBasedGenesis extends IGenesis {

	void reactToDeath(IPerson person);

	void reactToInfertility(IPerson person);

	void reactToFertility(IPerson person);

	void reactToPairability(IPerson person);
}
