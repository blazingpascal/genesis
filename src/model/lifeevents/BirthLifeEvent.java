package model.lifeevents;

import model.Sex;
import model.person.IPerson;

public class BirthLifeEvent implements ILifeEvent {

	private IPerson person;
	private final String TITLE_FMT = "%s is born!";
	private final String DESC_FMT = "%s and %s had a baby %s in %d.";

	public BirthLifeEvent(IPerson child) {
		this.person = child;
	}

	@Override
	public String getLifeEventType() {
		return "Birth";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format(TITLE_FMT, person.getFullBirthName());
	}

	@Override
	public String getLifeEventDescription() {
		return String.format(DESC_FMT, person.getMother().getFullName(), person.getFather().getFullName(),
				person.getBirthYear(), getSexNoun(person.getSex()));
	}

	private String getSexNoun(Sex sex) {
		switch (sex) {
		case FEMALE:
			return "girl";
		case MALE:
			return "boy";
		default:
			return "person";
		}
	}

	@Override
	public String getLifeEventDate() {
		return "1/1/" + this.person.getBirthYear();
	}

}
