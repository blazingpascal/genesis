package model.person;

import java.util.Random;

public class RomanticRole extends ARole {

	public static final RomanticRole SoulmateSeeker = new RomanticRole("Soulmate Seeker", 1f, 0.5f, 1);
	public static final RomanticRole Casanova = new RomanticRole("Casanova", 1f, 0.0f, 1);
	public static final RomanticRole CasualDater = new RomanticRole("Casual Dater", 0.5f, 0.5f, 1);
	public static final RomanticRole Celibate = new RomanticRole("Celibate", 0.0f, 0.0f, 1f);

	private static final RomanticRole[] opts = new RomanticRole[4];

	static {
		opts[0] = SoulmateSeeker;
		opts[1] = Casanova;
		opts[2] = CasualDater;
		opts[3] = Celibate;
	}

	private final float flirtation;
	private final float monogamy;

	private RomanticRole(String title, float flirtation, float monogamy, float strength) {
		super(title, strength);
		this.monogamy = monogamy;
		this.flirtation = flirtation;
	}

	public static RomanticRole getRandomRole(Random r, boolean canBeCelibate) {
		int rIndex = r.nextInt(opts.length + (!canBeCelibate ? -1 : 0));
		return opts[rIndex];
	}

	public double computeRomanticCompatibility(ARole r) {
		return r.computeRomanticCompatibilityRR(this);
	}

	@Override
	public double computeRomanticCompatibilityRR(RomanticRole romanticRole) {
		if (this.flirtation > 0) {
			return (2.0 * Math.abs(romanticRole.monogamy - this.monogamy)
					+ Math.abs(romanticRole.flirtation - this.flirtation)) / 3.0;
		}
		return 0;
	}

	@Override
	ARole merge(ARole r) {
		return r.mergeRR(this);
	}

	public ARole mergeRR(RomanticRole r) {
		float monogamy = (this.strength * this.monogamy + r.strength * r.monogamy) / 2f;
		float flirtation = (this.strength * this.flirtation + r.strength * r.flirtation) / 2f;
		return new RomanticRole(calculateTitle(monogamy, flirtation), flirtation, monogamy,
				(this.strength + r.strength) / 2);
	}

	final static String calculateTitle(float monogamy, float flirtation) {
		if (flirtation > 0) {
			return getFlirtationAdjective(flirtation) + " " + getMonogamyNoun(monogamy);
		} else {
			return "Celibate";
		}
	}

	private static String getMonogamyNoun(float monogamy) {
		if(monogamy <= 0){
			return "Polyamorist";
		}
		if(monogamy <= .3f){
			return "Commitment Fearer";
		}
		if(monogamy <= .5f){
			return "Casual Dater";
		}
		if(monogamy <= .7f){
			return "Relationship Seeker";
		}
		return "Soulmate Seeker";
	}

	private static String getFlirtationAdjective(float flirtation) {
		if (flirtation <= 0) {
			return "Celibate";
		}
		if (flirtation <= .1f) {
			return "Uninterested";
		}
		if (flirtation <= .2f) {
			return "Unflirty";
		}
		if (flirtation <= .3f) {
			return "Unprovocative";
		}
		if (flirtation <= .4f) {
			return "Modest";
		}
		if (flirtation <= .5f) {
			return "Casual";
		}
		if (flirtation <= .6f) {
			return "Coy";
		}
		if (flirtation <= .7f) {
			return "Flirty";
		}
		if (flirtation <= .8f) {
			return "Charming";
		}
		if(flirtation <= .9f){
			return "Romantic";
		}
		return "Amorous";
	}
}