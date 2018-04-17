package model.goals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import model.career.Job;
import model.lifeevents.ILifeEvent;
import model.person.APersonalInfoPerson;

public class TakeLeaveEvent implements ILifeEvent {

	private String name;
	private int level;
	private String jobTitle;
	private String occupation;
	private int year;

	public TakeLeaveEvent(APersonalInfoPerson person,  Job job, int year) {
		this.name = person.getFullName();
		this.level = job.getLevel();
		this.jobTitle = job.getJobTypeTitle();
		this.occupation = job.getOccupation().getName();
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Take Leave";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s has taken leave from their job as a level %d %s in the %s career", 
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
