package model.person;

import java.util.Random;

class PlatonicRole extends ARole {

	//Friendly Neighbor, Close Circle Keeper, Friendly Enough, 
	// Frenemy Seeker, Common Enemy, Actually Hates Everyone
	public static final PlatonicRole FriendlyNeighbor = new PlatonicRole("Friendly Neighbor", 1, 0.5f, 1);
	public static final PlatonicRole CloseCircleKeeper = new PlatonicRole("Close Circle Keeper", 1, 1, 1);
	public static final PlatonicRole FriendlyEnough = new PlatonicRole("Friendly Enough", 1f, 0f, 0.5f);
	public static final PlatonicRole FrenemySeeker = new PlatonicRole("Frenemy Seeker", 1f, 0.5f, 0.5f);
	public static final PlatonicRole CommonEnemy = new PlatonicRole("Common Enemy", 1f, 0.5f, 1);
	public static final PlatonicRole ActuallyHatesEveryone = new PlatonicRole("Actually Hates Everyone", 1f, 0f, 0f);
	
	private static final PlatonicRole[] opts = new PlatonicRole[6];
	
	static{
		opts[0] = FriendlyNeighbor;
		opts[1] = CloseCircleKeeper;
		opts[2] = FriendlyEnough;
		opts[3] = FrenemySeeker;
		opts[4] = CommonEnemy;
		opts[5] = ActuallyHatesEveryone;
	}
	
	private final float depth;
	private final float friendliness;
	
	PlatonicRole(String title, float strength, float depth, float friendliness) {
		super(title, strength);
		this.depth = depth;
		this.friendliness = friendliness;
	}
	
	@Override
	ARole merge(ARole r2) {
		return r2.mergePR(this);
	}

	@Override
	protected ARole mergePR(PlatonicRole pr) {
		float depth = (this.depth + pr.depth)/2;
		float friendliness = (this.friendliness + pr.friendliness)/2;
		float strength = (this.strength + pr.strength)/2;
		String title = calculateTitle(depth, friendliness);
		return new PlatonicRole(title, strength, depth, friendliness);
	}

	private String calculateTitle(float depth, float friendliness) {
		//TODO make this better
		return String.format("%f/1 invested and %f/1", 
				depth, friendliness);
	}

	public static PlatonicRole getRandomRole(Random r) {
		return opts[r.nextInt(opts.length)];
	}

}
