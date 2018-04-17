package model.lifeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import model.career.Job;
import model.person.APersonalInfoPerson;

public class RetirementEvent implements ILifeEvent {

	private String name;
	private int level;
	private String jobTitle;
	private String occupation;
	private int year;

	
	public RetirementEvent(APersonalInfoPerson person, Job job, int year) {
		this.name = person.getFullName();
		this.level = job.getRank();
		this.jobTitle = job.getJobTypeTitle();
		this.occupation = job.getOccupation().getName();
		this.year = year;
	}

	@Override
	public String getLifeEventType() {
		return "Retirement";
	}

	@Override
	public String getLifeEventTitle() {
		return String.format("%s has retried from their job as a level %d %s in the %s career", 
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
