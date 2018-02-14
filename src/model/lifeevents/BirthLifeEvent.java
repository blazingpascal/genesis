package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Sex;
import model.person.IPerson;

public class BirthLifeEvent implements ILifeEvent {

	private IPerson person;
	private final String TITLE_FMT = "%s is born!";
	private final String DESC_FMT = "%s and %s had a baby %s in %d.";
	private final String ORPHAN_FMT = "%s arrived as a newborn baby at the Genesis orphanage in %d";

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
		IPerson mother = person.getMother();
		IPerson father = person.getFather();
		if (mother != null && father != null) {
			return String.format(DESC_FMT, mother.getFullName(), father.getFullName(), getSexNoun(person.getSex()), person.getBirthYear());
		}
		
		return String.format(ORPHAN_FMT, person.getFullBirthName(),
				person.getBirthYear());

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
	public Date getLifeEventDate() {
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		try {
			return df.parse("01/01/" + this.person.getBirthYear());
		} catch (ParseException e) {
			throw new IllegalStateException("????");
		}
	}

}
