package model.lifeevents;

import model.person.IPerson;

public class ImmigrationLifeEvent implements ILifeEvent {

	private IPerson p;
	private long immigrationYear;

	public ImmigrationLifeEvent(IPerson immigrant, long immigrationYear) {
		p = immigrant;
		this.immigrationYear = immigrationYear;
	}

	@Override
	public String getLifeEventType() {
		return "Immigration";
	}

	@Override
	public String getLifeEventTitle() {
		return p.getFullBirthName() + " arrives.";
	}

	@Override
	public String getLifeEventDescription() {
		return String.format("Bright-eyed and ready for adventure %s arrived in %d", p.getFullBirthName(),
				immigrationYear);
	}

	@Override
	public String getLifeEventDate() {
		return "01/01/" + this.immigrationYear;
	}

}
