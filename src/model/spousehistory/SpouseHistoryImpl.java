package model.spousehistory;

import model.person.IPerson;

class SpouseHistoryImpl implements ISpouseHistory {
	
	private final int anniversaryYear;
	private final IPerson spouse;
	private int endYear = -1;
	
	SpouseHistoryImpl(IPerson spouse, int anniversaryYear){
		this.spouse = spouse;
		this.anniversaryYear = anniversaryYear;
	}
	@Override
	public IPerson getSpouse() {
		return this.spouse;
	}

	@Override
	public int getAnniversaryYear() {
		return anniversaryYear;
	}

	@Override
	public int getEndingYear() {
		return this.endYear;
	}
	@Override
	public void setEndingYear(int endYear) {
		this.endYear = endYear;
	}

}
