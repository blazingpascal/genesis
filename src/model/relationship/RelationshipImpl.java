package model.relationship;

import model.GeneologyRules;
import model.person.IPerson;

import java.util.Random;

public class RelationshipImpl implements IRelationship {
	private double regard;
	private double desire;
	private IPerson p1;
	private IPerson p2;
	private int anniversaryYear;
	private RelationshipType type;
	private final double REGARD_DESIRE_INCREMENT = 0.2;

	public RelationshipImpl(IPerson p1, IPerson p2, int anniversaryYear) {
		this(p1, p2, 0, 0, anniversaryYear, null);
	}

	public RelationshipImpl(IPerson p1, IPerson p2, double regard, double desire, int anniversaryYear,
			RelationshipType type) {
		this.regard = regard;
		this.desire = desire;
		this.p1 = p1;
		this.p2 = p2;
		this.anniversaryYear = anniversaryYear;
		this.type = (type == null) ? RelationshipType.platonicType(regard) : type;
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
	public void growRegard() {
		this.regard = Math.min(1, this.regard + REGARD_DESIRE_INCREMENT * GeneologyRules.computeRegard(p1, p2));
		this.updateType();
	}

	@Override
	public void lessenRegard() {
		this.regard = Math.max(-1,
				this.regard - (REGARD_DESIRE_INCREMENT * (1 - GeneologyRules.computeRegard(p1, p2))));
		this.updateType();
	}

	@Override
	public double romanticDesire() {
		return this.desire;
	}

	@Override
	public void growDesire() {
		this.desire = Math.min(1, this.desire + REGARD_DESIRE_INCREMENT * GeneologyRules.computeDesire(p1, p2));
	}

	@Override
	public void lessenDesire() {
		this.desire = Math.max(-1, this.desire - REGARD_DESIRE_INCREMENT * (1 - GeneologyRules.computeDesire(p1, p2)));
	}

	@Override
	public RelationshipType getType() {
		return this.type;
	}

	public void updateType() {
		if (!this.isRomantic()) {
			this.type = RelationshipType.platonicType(regard);
		}
	}

	@Override
	public boolean isRomantic() {
		return this.type.isRomantic();
	}

	private boolean rollFor(Random r, double chance) {
		return r.nextDouble() < chance;
	}

	@Override
	public void progressRelationship(int year, Random r) {
		progressRegard(r);
		if ((this.isRomantic() || (p1.getAge() >= GeneologyRules.MIN_MARRIAGE_AGE
				&& p2.getAge() >= GeneologyRules.MIN_MARRIAGE_AGE && compatibleSexuality(r)))) {
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
				// No breaking up until we can maintain a sustainable population
				/*
				if (this.isRomantic()) {
					if (rollFor(r, breakUpChance())) {
						breakUp(year);
					}
				}*/
			}
		}
	}

	private void progressRegard(Random r) {
		if (rollFor(r, progressRegardChance(r))) {
			growRegard();
		} else {
			lessenRegard();
		}
	}

	private double progressRegardChance(Random r) {
		double computeRegard = GeneologyRules.computeRegard(p1, p2);
		return computeRegard;
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
		double computeDesire = GeneologyRules.computeDesire(p1, p2);
		return computeDesire;
	}

	private boolean compatibleSexuality(Random r) {
		return this.p1.getSex() == this.p2.getSex()
				? r.nextDouble() < this.p1.kinseyScaleValue() && r.nextDouble() < this.p2.kinseyScaleValue()
				: r.nextDouble() > this.p1.kinseyScaleValue() && r.nextDouble() > this.p2.kinseyScaleValue();
	}

	private void progressRomanticType(int year) {
//		System.out.printf("Regard: %f (%s)\n", regard, this.getType());
		
		switch (this.getType()) {
		case FRIEND:
			this.type = RelationshipType.PARTNER;
			this.p1.setSignificantOther(p2);
			p2.setSignificantOther(p1);
			break;
		case PARTNER:
			this.type = RelationshipType.FIANCE;
			break;
		case FIANCE:
			this.type = RelationshipType.SPOUSE;
			p1.marry(p2, anniversaryYear);
			break;
		default:
			break;
		}
		if(this.getType().isRomantic()){
			System.out.printf("%s: %s - %s\n", this.getType(), p1.getId(), p2.getId());
		}
	}

	private double progressRomanticTypeChance() {
		double upperThreshold = (this.isRomantic()) ? this.getType().getUpperThreshold()
				: RelationshipType.PARTNER.getLowerThreshold();
		if (this.desire <= upperThreshold) {
			return 0;
		} else {
			double d = 0.5 + Math.min(this.desire - upperThreshold, 0.5);
			return d;
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
		this.p1.divorce(year);
		this.type = RelationshipType.platonicType(this.regard);
	}

	@Override
	public int getAnniversaryYear() {
		return this.anniversaryYear;
	}

	@Override
	public void setRegard(double regard) {
		this.regard = regard;

	}

	@Override
	public void setDesire(double desire) {
		this.desire = desire;
	}

	@Override
	public void setType(RelationshipType oldType) {
		this.type = oldType;
	}
}
