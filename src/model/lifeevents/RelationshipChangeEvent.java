package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.person.IPerson;
import model.relationship.RelationshipType;

public class RelationshipChangeEvent implements ILifeEvent {

	private String p1Name;
	private String p2Name;
	private RelationshipType type;
	private int year;

	public RelationshipChangeEvent(IPerson p1, IPerson p2, RelationshipType type, int year) {
		this.p1Name = p1.getFullName();
		this.p2Name = p2.getFullName();
		this.type = type;
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Relationship Change";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s and %s are now %s", p1Name, p2Name, type);
	}

	@Override
	public String getLifeEventDescription() {
		return "";
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
