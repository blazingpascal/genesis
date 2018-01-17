package model;

public interface ISpouseHistory {

	IPerson getSpouse();

	int getAnniversaryYear();
	
	int getEndingYear();
	
	void setEndingYear(int endYear);

}
