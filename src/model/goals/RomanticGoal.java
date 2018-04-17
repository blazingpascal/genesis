package model.goals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.person.CombinationRole;
import model.person.IPerson;
import model.person.RomanticRole;
import model.relationship.IRelationship;

public class RomanticGoal extends AGoal {

	private static final String TITLE = "Find %d romantic partners " + "and stay in love for %d years each";

	private static final int MIN_RELATIONSHIPS = 1;
	private static final int MAX_RELATIONSHIPS = 5;
	private static final int MAX_DURATION = 40;
	private static final int MIN_DURATION = 5;

	private final int desiredNumberOfRomanticRelationships;
	private final int desiredDurationPerRelationship;
	private final boolean isMonogamous;

	public RomanticGoal(IPerson p, Random r) {
		super(null, "", p);
		CombinationRole comboR = (CombinationRole) p.getRole();
		RomanticRole role = comboR.getRomantic();
		double flirtation = role.getFlirtation();
		double monogamy = role.getMonogamy();
		if (monogamy == 0 && flirtation == 0) {
			this.desiredDurationPerRelationship = 0;
			this.desiredNumberOfRomanticRelationships = 0;
			this.isMonogamous = false;
			this.title = ("Celibate and not interested in romance");
		} else {
			isMonogamous = r.nextDouble() > monogamy;
			this.desiredNumberOfRomanticRelationships = isMonogamous ? 1
					: MIN_RELATIONSHIPS + r.nextInt(MAX_RELATIONSHIPS);
			this.desiredDurationPerRelationship = MIN_DURATION + r.nextInt(MAX_DURATION - MIN_DURATION);
			this.title = String.format(TITLE, desiredNumberOfRomanticRelationships, desiredDurationPerRelationship);
		}
	}

	@Override
	public double computeProgress(long year) {
		double rValue = 0;
		Map<IPerson, IRelationship> relationships = refPerson.getRelationships();
		List<IRelationship> romanticRelationships = new ArrayList<IRelationship>();
		for (IRelationship r : relationships.values()) {
			if (r.isRomantic()) {
				romanticRelationships.add(r);
			}
		}
		romanticRelationships.sort((r1, r2) -> 
		Long.compare((year - r2.getAnniversaryYear()), ((year - r1.getAnniversaryYear()))));
		for(int i = 0; i < desiredNumberOfRomanticRelationships; i++){
			if(i >= romanticRelationships.size()){
				break;
			}
			long duration = year - romanticRelationships.get(i).getAnniversaryYear();
			double success = Math.min(1, (double)duration / desiredDurationPerRelationship);
			rValue += success * (1.0 / desiredNumberOfRomanticRelationships);
		}
		return rValue;
	}

	@Override
	public double computeHypotheticalProgress(List<IAction> actions, long year) {
		throw new IllegalStateException("Not implemented yet sorry");
	}

}
