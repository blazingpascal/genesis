package model.person;

import java.util.Random;

public class RomanticRole extends Role {

	public static final RomanticRole SoulmateSeeker = 
			new RomanticRole("Soulmate Seeker", 1f, 0.5f, 1);
	public static final RomanticRole Casanova = 
			new RomanticRole("Casanova", 1f, 0.0f, 1);
	public static final RomanticRole CasualDater = 
			new RomanticRole("Casual Dater", 0.5f, 0.5f, 1);
	public static final RomanticRole Celibate = 
			new RomanticRole("Celibate", 0.0f, 0.0f, 1f);
	
	private static final RomanticRole [] opts = new RomanticRole[4];
	
	static{
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
	
	public static Role getRandomRole(Random r){
		int rIndex = r.nextInt(opts.length);
		return opts[rIndex];
	}
}