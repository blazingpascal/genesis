package model.person;

import java.util.Random;

public class NarrativeRole extends ARole {

	final static int DEFAULT_ACTION_POINTS = 12;
	private final float importance;
	private final float goodness;
	//Hero, Villain, Hero's Sidekick, Villain's Mook, 
	// Distressed (Like Damsel in Distress but gender neutral),
	// Background Character
	
	public static final NarrativeRole Hero =
			new NarrativeRole("Hero", 1f, 1f, 1f, DEFAULT_ACTION_POINTS);
	public static final NarrativeRole Villain =
			new NarrativeRole("Villain", 1f, 1f, 0f, DEFAULT_ACTION_POINTS);
	public static final NarrativeRole Sidekick =
			new NarrativeRole("Sidekick", 1f, 0.5f, 1f, DEFAULT_ACTION_POINTS * 2/3);
	public static final NarrativeRole Mook =
			new NarrativeRole("Mook", 1f, 0.5f, 0f, DEFAULT_ACTION_POINTS * 2/3);
	public static final NarrativeRole Distressed =
			new NarrativeRole("Distressed", 1f, 1f, 0.5f, DEFAULT_ACTION_POINTS * 1/2);
	public static final NarrativeRole BackgroundCharacter =
			new NarrativeRole("Background Character", 1f, 0f, 0.5f, DEFAULT_ACTION_POINTS * 1/2);
	
	private static final NarrativeRole[] opts = new NarrativeRole[6];
	
	static{
		opts[0] = Hero;
		opts[1] = Villain;
		opts[2] = Sidekick;
		opts[3] = Mook;
		opts[4] = Distressed;
		opts[5] = BackgroundCharacter;
	}
	
	public NarrativeRole(String title, float strength, 
			float importance, float goodness, int actionPoints) {
		super(title, strength, actionPoints);
		this.importance = importance;
		this.goodness = goodness;
	}
	
	@Override
	ARole merge(ARole r2) {
		return r2.mergeNR(this);
	}

	public static NarrativeRole getRandomRole(Random r) {
		return opts[r.nextInt(opts.length)];
	}
	
	protected NarrativeRole mergeNR(NarrativeRole nr){
		float importance = (this.importance + nr.importance)/2;
		float goodness = (this.goodness + nr.goodness)/2;
		float strength = (this.strength + nr.strength)/2;
		int actionPoints = (this.actionPoints + nr.actionPoints)/2;
		String title = calculateTitle(importance, goodness);
		return new NarrativeRole(title, strength, importance, goodness, actionPoints);
	}

	private String calculateTitle(float importance, float goodness) {
		//TODO Make this better
		return String.format("%f/1 important and %f/1 good", 
				importance, goodness);
	}

}
