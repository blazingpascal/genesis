package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.person.IPerson;

public class MeetingEvent implements ILifeEvent {

	String p1Name;
	String p2Name;
	int year;
	
	public MeetingEvent(IPerson p1, IPerson p2, int year) {
		this.p1Name = p1.getFullName();
		this.p2Name = p2.getFullName();
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Meeting";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s meets %s", p1Name, p2Name);
	}

	@Override
	public String getLifeEventDescription() {
		return String.format("%s met %s in %d", p1Name, p2Name, this.year);
	}

	@Override
	public Date getLifeEventDate() {
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		try {
			return df.parse("01/01/" + this.year);
		} catch (ParseException e) {
			throw new IllegalStateException("????");
		}
	}

}
