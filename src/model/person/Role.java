package model.person;

import java.util.Random;

public abstract class Role implements Comparable<Role>{

	private final String title;
	private final float strength;
	
	public Role(String title, float strength) {
		this.title = title;
		this.strength = strength;
	}

	@Override
	public int compareTo(Role r) {
		return Float.compare(this.strength, r.strength);
	}

	public static Role calculateRole(Role r1, Role r2) {
		// This may be become similar to the incomplete dominance model later
		int comparison = r1.compareTo(r2);
		if(comparison > 0){
			return r2;
		} else if(comparison < 0){
			return r1;
		} else{
			return Math.random() < 0.5 ? r1 : r2;
		}
	}

	public static Role getRandomRole(Random random) {
		// TODO
		return RomanticRole.getRandomRole(random);
	}

}
