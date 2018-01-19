package model.genesis.idbased;

import java.util.List;

import model.genesis.IGenesis;
import model.lifeevents.ILifeEvent;

public interface ILifeEventEnabledGenesis extends IGenesis{
	public List<ILifeEvent> lifeEvents();
}
