package model.goals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.person.CombinationRole;
import model.person.IPerson;
import model.person.PlatonicRole;
import model.relationship.IRelationship;

public class PlatonicGoal extends AGoal {

	private static final String TITLE = "Have %d %s friends and %d %s enemies.";

	private static final int MIN_RELATIONSHIPS = 2;
	private static final int MAX_RELATIONSHIPS = 20;

	private static final float MAX_SHALLOW_ENEMY_VALUE = -.3f;
	private static final float MIN_SHALLOW_ENEMY_VALUE = -.49f;
	private static final float MAX_DEEP_ENEMY_VALUE = -0.7f;
	private static final float MIN_DEEP_ENEMY_VALUE = -1f;

	private static final float MAX_SHALLOW_FRIEND_VALUE = .3f;
	private static final float MIN_SHALLOW_FRIEND_VALUE = .49f;
	private static final float MAX_DEEP_FRIEND_VALUE = 0.7f;
	private static final float MIN_DEEP_FRIEND_VALUE = 1f;

	private int numEnemies = 0;
	private int numFriends = 0;
	private float positiveRelationshipValue = 0;
	private float negativeRelationshipValue = 0;

	public PlatonicGoal(IPerson p, Random r) {
		super(null, "", p);
		CombinationRole comboR = (CombinationRole) p.getRole();
		PlatonicRole role = comboR.getPlatonic();
		float depth = role.getDepth();
		float friendliness = role.getFriendliness();
		int numRelationships = MIN_RELATIONSHIPS
				+ r.nextInt(Math.round(Math.max(1f, (MAX_RELATIONSHIPS * depth) - MIN_RELATIONSHIPS)));
		boolean isShallow = depth < 0.5f;
		if (isShallow) {
			positiveRelationshipValue = MIN_SHALLOW_FRIEND_VALUE
					+ r.nextFloat() * (MAX_SHALLOW_FRIEND_VALUE - MIN_SHALLOW_FRIEND_VALUE);
			negativeRelationshipValue = MIN_SHALLOW_ENEMY_VALUE
					+ r.nextFloat() * (MAX_SHALLOW_ENEMY_VALUE - MIN_SHALLOW_ENEMY_VALUE);
		} else {
			positiveRelationshipValue = MIN_DEEP_FRIEND_VALUE
					+ r.nextFloat() * (MAX_DEEP_FRIEND_VALUE - MIN_DEEP_FRIEND_VALUE);
			negativeRelationshipValue = MIN_DEEP_ENEMY_VALUE
					+ r.nextFloat() * (MAX_DEEP_ENEMY_VALUE - MIN_DEEP_ENEMY_VALUE);
		}
		for (int i = 0; i < numRelationships; i++) {
			boolean isPositive = r.nextDouble() < friendliness;
			if (isPositive) {
				numFriends++;
			} else {
				numEnemies++;
			}
		}
		String adjective = isShallow ? "shallow" : "deep";
		this.title = String.format(TITLE, numFriends, adjective, numEnemies, adjective);
	}

	@Override
	public double computeProgress(long year) {
		double rValue = 0;
		List<IRelationship> positives = new ArrayList<IRelationship>();
		List<IRelationship> negatives = new ArrayList<IRelationship>();
		float friendContributor =  (1f/numFriends) * ((float) numFriends / (numFriends + numEnemies));
		float enemyContributor = (1f/numEnemies) * ((float) numEnemies / (numFriends + numEnemies));
		for (IRelationship r : refPerson.getRelationships().values()) {
			if (r.regard() > 0) {
				positives.add(r);
			} else if (r.regard() < 0) {
				negatives.add(r);
			}
		}
		positives.sort((r1, r2) -> Double.compare(r2.regard(), r1.regard()));
		negatives.sort((r1, r2) -> Double.compare(r2.regard(), r1.regard()));
		for (int i = 0; i < numFriends; i++) {
			if (positives.size() - 1 < i)
				break;
			IRelationship r = positives.get(i);
			rValue += (Math.min(1, r.regard() / positiveRelationshipValue) * friendContributor);
		}
		for (int i = 0; i < numEnemies; i++) {
			if (negatives.size() - 1 < i)
				break;
			IRelationship r = negatives.get(i);
			rValue += (Math.min(1, r.regard() / negativeRelationshipValue) * enemyContributor);
		}
		return rValue;
	}

	@Override
	public double computeHypotheticalProgress(List<IAction> actions, long year) {
		throw new IllegalStateException("Not implemented yet sorry");
	}
}
