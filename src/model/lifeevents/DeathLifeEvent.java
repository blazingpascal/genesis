package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.person.IPerson;

public class DeathLifeEvent implements ILifeEvent {

	private final String TITLE_FMT = "%s has passed away";
	private IPerson person;
	private int year;

	public DeathLifeEvent(IPerson person, int year) {
		this.person = person;
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Death";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format(TITLE_FMT, person.getFullName());
	}

	@Override
	public String getLifeEventDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("In %d, %s passed away at age %d.", this.year, this.person.getFullName(),
				this.person.getAge()));
		if (!this.person.isSingle()) {
			sb.append(String.format("%s's %s %s will miss %s dearly", this.person.getFirstName(), getWidowNoun(),
					this.person.getSpouse().getFullName(), getSexPronoun()));
		}
		List<IPerson> children = this.person.getChildren();
		// TODO
		return sb.toString();
	}

	private String getWidowNoun() {
		switch (this.person.getSpouse().getSex()) {
		case MALE:
			return "widower";
		default:
			return "widow";
		}
	}

	private String getSexPronoun() {
		switch (this.person.getSex()) {
		case FEMALE:
			return "her";
		case MALE:
			return "him";
		default:
			return "them";
		}
	}

	@Override
	public Date getLifeEventDate() {
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		try {
			return df.parse("12/31/" + this.year);
		} catch (ParseException e) {
			throw new IllegalStateException("????");
		}
	}

}
