package model.person;

import java.util.Random;

public abstract class Role implements Comparable<Role> {
	protected final String title;
	protected final float strength;
	final static double MUTATION_CHANCE = .05f;

	public Role(String title, float strength) {
		this.title = title;
		this.strength = strength;
	}

	@Override
	public int compareTo(Role r) {
		return Float.compare(this.strength, r.strength);
	}

	public static Role calculateRole(Role r1, Role r2, Random r) {
		if (r.nextDouble() < MUTATION_CHANCE) {
			return getRandomRole(new Random(r.nextInt()), true);
		}
		return r1.merge(r2);
	}

	abstract Role merge(Role r2);

	public static Role getRandomRole(Random random, boolean canBeCelibate) {
		// TODO
		return RomanticRole.getRandomRole(random, canBeCelibate);
	}

	public abstract double computeRomanticCompatibility(Role r);

	public double computeRomanticCompatibilityRR(RomanticRole romanticRole) {
		throw new IllegalStateException("Only other Romantic Roles should be used to compute compatibility");
	}

	@Override
	public String toString() {
		return this.title;
	}

	public Role mergeRR(RomanticRole romanticRole) {
		throw new IllegalStateException("Can only merge romantic roles with other romantic roles");
	}

}
