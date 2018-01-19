package model.lifeevents;

import model.person.IPerson;

public class MarriageLifeEvent implements ILifeEvent {

	private String TITLE_FMT = "%s and %s get married!";
	private String DESC_FMT = "%s and %s became %s and %s in a heart-warming ceremony in %d";

	private IPerson s1;
	private IPerson s2;
	private int anniversaryYear;

	public MarriageLifeEvent(IPerson s1, IPerson s2, int anniversaryYear) {
		this.s1 = s1;
		this.s2 = s2;
		this.anniversaryYear = anniversaryYear;
	}

	@Override
	public String getLifeEventType() {
		return "Marriage";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format(TITLE_FMT, s1.getFullBirthName(), s2.getFullBirthName());
	}

	@Override
	public String getLifeEventDescription() {
		return String.format(DESC_FMT, s1.getFullBirthName(), s2.getFullBirthName(), s1.getFullName(),
				s2.getFullName(), anniversaryYear);
	}

	@Override
	public String getLifeEventDate() {
		return String.format("1/1/%d", this.anniversaryYear);
	}

}
