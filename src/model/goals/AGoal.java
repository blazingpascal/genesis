package model.goals;

import java.util.List;

import model.person.IPerson;

public abstract class AGoal implements IGoal{

	protected String title;
	protected String description;
	protected IPerson refPerson;
	
	public AGoal(String title, String description, IPerson p){
		this.title = title;
		this.description = description;
		refPerson = p;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
