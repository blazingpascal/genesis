package model.spousehistory;

import model.person.IPerson;

public interface ISpouseHistory {

	IPerson getSpouse();

	int getAnniversaryYear();
	
	int getEndingYear();
	
	void setEndingYear(int endYear);

	public static ISpouseHistory createSpouseHistory(IPerson spouse, int anniversaryYear){
		return new SpouseHistoryImpl(spouse, anniversaryYear);
	}
}
