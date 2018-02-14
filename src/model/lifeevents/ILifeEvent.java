package model.lifeevents;

import java.util.Date;

public interface ILifeEvent {
	String getLifeEventType();
	String getLifeEventTitle();
	String getLifeEventDescription();
	Date getLifeEventDate();
}
