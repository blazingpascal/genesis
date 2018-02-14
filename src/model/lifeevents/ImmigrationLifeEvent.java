package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	public Date getLifeEventDate() {
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		try {
			return df.parse("01/01/" + this.immigrationYear);
		} catch (ParseException e) {
			throw new IllegalStateException("????");
		}
	}

}
