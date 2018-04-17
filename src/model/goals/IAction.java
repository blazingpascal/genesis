package model.goals;

import java.util.Random;

public interface IAction {
	boolean reverse();
	String title();
	String description();
	int actionPointValue();
	void enact(int year, Random r);
}
