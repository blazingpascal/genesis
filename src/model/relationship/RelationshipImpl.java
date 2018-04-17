package model.relationship;

import model.GeneologyRules;
import model.lifeevents.RelationshipChangeEvent;
import model.person.IPerson;

import java.util.Random;

public class RelationshipImpl implements IRelationship {
	private double regard;
	private double desire;
	private IPerson p1;
	private IPerson p2;
	private int anniversaryYear;
	private RelationshipType type;
	private final double REGARD_DESIRE_INCREMENT = 0.1;

	public RelationshipImpl(IPerson p1, IPerson p2, int anniversaryYear) {
		this(p1, p2, GeneologyRules.computeRegard(p1, p2), GeneologyRules.computeDesire(p1, p2), anniversaryYear, null);
	}

	public RelationshipImpl(IPerson p1, IPerson p2, double regard, double desire, int anniversaryYear,
			RelationshipType type) {
		this.regard = regard;
		this.desire = desire;
		this.p1 = p1;
		this.p2 = p2;
		this.anniversaryYear = anniversaryYear;
		this.setType((type == null) ? RelationshipType.platonicType(regard) : type, anniversaryYear);
	}

	@Override
	public IPerson p1() {
		return this.p1;
	}

	@Override
	public IPerson p2() {
		return this.p2;
	}

	@Override
	public double regard() {
		return this.regard;
	}

	@Override
	public void growRegard(int year) {
		this.regard = Math.min(1, this.regard + REGARD_DESIRE_INCREMENT);
		this.updateType(year);
	}

	@Override
	public void lessenRegard(int year) {
		this.regard = Math.max(-1, this.regard - REGARD_DESIRE_INCREMENT);
		this.updateType(year);
	}

	@Override
	public double romanticDesire() {
		return this.desire;
	}

	@Override
	public void growDesire() {
		this.desire = Math.min(1, this.desire + REGARD_DESIRE_INCREMENT);
	}

	@Override
	public void lessenDesire() {
		this.desire = Math.max(-1, this.desire - REGARD_DESIRE_INCREMENT);
	}

	@Override
	public RelationshipType getType() {
		return this.type;
	}

	public void updateType(int year) {
		if (!this.isRomantic()) {
			RelationshipType platonicType = RelationshipType.platonicType(regard);
			if (platonicType != this.type) {
				p1.addLifeEvent(new RelationshipChangeEvent(p1, p2, platonicType, year));
			}
			this.setType(platonicType, year);
		}
	}

	@Override
	public boolean isRomantic() {
		return this.getType().isRomantic();
	}

	private boolean rollFor(Random r, double chance) {
		return r.nextDouble() < chance;
	}

	@Override
	public void progressRelationship(Random r, int year) {
		progressRegard(r, year);
		if (rollFor(r, progressDesireChance(r))) {
			growDesire();
			if (this.isRomantic()) {
				if (rollFor(r, progressRomanticTypeChance())) {
					progressRomanticType(year);
				}
			} else {
				if (rollFor(r, startDatingChance())) {
					progressRomanticType(year);
				}
			}
		} else {
			lessenDesire();
			if (this.isRomantic()) {
				if (rollFor(r, breakUpChance())) {
					breakUp(year);
				}
			}
		}
	}

	private void progressRegard(Random r, int year) {
		if (rollFor(r, progressRegardChance(r))) {
			growRegard(year);
		} else {
			lessenRegard(year);
		}
	}

	private double progressRegardChance(Random r) {
		return r.nextDouble(); // TODO: add some actual logic
	}

	private double startDatingChance() {
		if (this.p1.hasSignificantOther() || this.p2.hasSignificantOther() || this.p1.isMourningSpouse()
				|| this.p2.isMourningSpouse()) {
			return 0;
		} else {
			return progressRomanticTypeChance();
		}
	}

	private double progressDesireChance(Random r) {
		return r.nextDouble(); // TODO: add some actual logic
	}

	private void progressRomanticType(int year) {
		switch (this.getType()) {
		case FRIEND:
			this.setType(RelationshipType.PARTNER, year);
			this.p1.setSignificantOther(p2);
			p2.setSignificantOther(p1);
			break;
		case PARTNER:
			this.setType(RelationshipType.FIANCE, year);
			break;
		case FIANCE:
			this.setType(RelationshipType.SPOUSE, year);
			break;
		default:
			break;
		}
	}

	private void setType(RelationshipType type, int year) {
		this.type = type;
		this.p1.addLifeEvent(new RelationshipChangeEvent(p1, p2, type, year));
	}

	private double progressRomanticTypeChance() {
		double upperThreshold = (this.isRomantic()) ? this.getType().getUpperThreshold()
				: RelationshipType.PARTNER.getLowerThreshold();
		if (this.desire <= upperThreshold) {
			return 0;
		} else {
			return this.desire; // TODO: add some actual logic
		}
	}

	private double breakUpChance() { // should this also include a random
										// factor?
		if (this.isRomantic()) {
			double reasonsToStay = this.desire + this.regard / 2; // TODO: add
																	// how long
																	// they've
																	// been
																	// together
			double total = 1.5;
			return total - reasonsToStay;
		} else {
			return 0;
		}

	}

	private void breakUp(int year) {
		this.p1.setSignificantOther(null);
		this.p2.setSignificantOther(null);
		this.setType(RelationshipType.platonicType(this.regard), year);
	}

	@Override
	public int getAnniversaryYear() {
		return this.anniversaryYear;
	}
}
