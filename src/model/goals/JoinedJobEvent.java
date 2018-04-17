package model.goals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import model.career.Job;
import model.lifeevents.ILifeEvent;
import model.person.APersonalInfoPerson;
import model.person.IPerson;

public class JoinedJobEvent implements ILifeEvent {

	private String personName;
	private int level;
	private String title;
	private String occupation;
	private int year;

	public JoinedJobEvent(IPerson person, Job job, int year) {
		this.personName = person.getFullName();
		this.level = job.getRank();
		this.title = job.getJobTypeTitle();
		this.occupation = job.getOccupation().getName();
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Joined Job";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s started as a Level %d %s in the %s career", 
				personName, level, title, occupation);
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
