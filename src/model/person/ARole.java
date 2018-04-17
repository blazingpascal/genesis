package model.person;

import java.util.Random;

public abstract class ARole implements Comparable<ARole> {
	protected String title;
	protected float strength;
	final static double MUTATION_CHANCE = .05f;

	public ARole(String title, float strength) {
		this.title = title;
		this.strength = strength;
	}

	@Override
	public int compareTo(ARole r) {
		return Float.compare(this.strength, r.strength);
	}

	public static ARole calculateRole(ARole r1, ARole r2, Random r) {
		if (r.nextDouble() < MUTATION_CHANCE) {
			return getRandomRole(new Random(r.nextInt()), true);
		}
		return r1.merge(r2);
	}

	abstract ARole merge(ARole r2);

	public static ARole getRandomRole(Random r, boolean canBeCelibate) {
		return new CombinationRole(RomanticRole.getRandomRole(r, canBeCelibate),
				PlatonicRole.getRandomRole(r), CareerRole.getRandomRole(r), 
				NarrativeRole.getRandomRole(r));
	}

	public double computeRomanticCompatibility(ARole r) {
		throw new IllegalStateException("This is not the job of this role");
	}

	public double computeRomanticCompatibilityRR(RomanticRole romanticRole) {
		throw new IllegalStateException("Only other Romantic Roles should be used to compute compatibility");
	}

    public double computeCareerProgressionModifier() {
        throw new IllegalStateException("This is not the job of this role");
    }

    public double getCareerFocus() {
        throw new IllegalStateException("This is not the job of this role");
    }

    public double getCareerTenacity() {
        throw new IllegalStateException("This is not the job of this role");
    }

	@Override
	public String toString() {
		return this.title;
	}

	protected ARole mergeRR(RomanticRole romanticRole) {
		throw new IllegalStateException("Can only merge romantic roles with other romantic roles");
	}

	protected ARole mergeCoR(CombinationRole cr) {
		throw new IllegalStateException("Can only merge combination roles with other combination roles");
	}

	protected ARole mergePR(PlatonicRole platonicRole) {
		throw new IllegalStateException("Can only merge platonic roles with other platonic roles");
	}

	ARole mergeCaR(CareerRole careerRole) {
		throw new IllegalStateException("Can only merge career roles with other career roles");
	}

	protected ARole mergeNR(NarrativeRole narrativeRole) {
		throw new IllegalStateException("Can only merge narrative roles with other narrative roles");
	}

	static final String adverbModifier(double value) {
		if(value < 0f){
			return "not";
		}
		if (value < 0.1f) {
			return "hardly";
		}
		if (value < 0.2f) {
			return "barely";
		}
		if (value < 0.3f) {
			return "only just";
		}
		if (value < 0.4f) {
			return "somewhat";
		}
		if (value < 0.5f) {
			return "kinda";
		}
		if (value < 0.6f) {
			return "pretty";
		}
		if (value < 0.7f) {
			return "moderately";
		}
		if (value < 0.8f) {
			return "considerably";
		}
		if (value < 0.9f) {
			return "very";
		}
		return "incredibly";
	}

}
