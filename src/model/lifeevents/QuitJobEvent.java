package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.career.Job;
import model.person.APersonalInfoPerson;
import model.person.IPerson;

public class QuitJobEvent implements ILifeEvent {

	String name;
	private int level;
	private String jobTitle;
	private String occupation;
	private int year;
	
	public QuitJobEvent(IPerson person, Job job, int year) {
		this.name = person.getFullName();
		this.level = job.getLevel();
		this.jobTitle = job.getJobTypeTitle();
		this.occupation = job.getOccupation().getName();
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Quit Job";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s has quit their job as a level %d %s in the %s career", 
				this.name, this.level, this.jobTitle, this.occupation);
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
