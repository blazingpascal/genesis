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
  private RelationshipType romanticType;

  public RelationshipImpl(IPerson p1, IPerson p2, int anniversaryYear) {
    this(p1, p2, GeneologyRules.computeRegard(p1, p2), GeneologyRules.computeDesire(p1, p2), anniversaryYear, null);
  }

  public RelationshipImpl(IPerson p1, IPerson p2, double regard, double desire, int anniversaryYear, RelationshipType romanticType) {
    this.regard = regard;
    this.desire = desire;
    this.p1 = p1;
    this.p2 = p2;
    this.anniversaryYear = anniversaryYear;
    this.romanticType = romanticType;
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
    this.regard = Math.min(1, this.regard + 0.1);
  }

  @Override
  public void lessenRegard() {
    this.regard = Math.max(-1, this.regard - 0.1);
  }

  @Override
  public double romanticDesire() {
    return this.desire;
  }

  @Override
  public void growDesire() {
    this.desire = Math.min(1, this.desire + 0.1);
  }

  @Override
  public void lessenDesire() {
    this.desire = Math.max(-1, this.desire - 0.1);
  }

  @Override
  public RelationshipType getType() {
    if (this.romanticType == null) {
      return RelationshipType.platonicType(regard);
    } else {
      return this.romanticType;
    }
  }

  @Override
  public boolean isRomantic() {
    return romanticType != null;
  }

  private boolean rollFor(Random r, double chance) {
    return r.nextDouble() < chance;
  }

  @Override
  public void progressRelationship(Random r) {
    progressRegard(r);
    if (rollFor(r, progressDesireChance(r))) {
      growDesire();
      if (this.isRomantic()) {
        if (rollFor(r, progressRomanticTypeChance())) {
          progressRomanticType();
        }
      } else {
        if (rollFor(r, startDatingChance())) {
          progressRomanticType();
        }
      }
    } else {
      lessenDesire();
      if (this.isRomantic()) {
        if (rollFor(r, breakUpChance())) {
          breakUp();
        }
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
    return r.nextDouble(); // TODO: add some actual logic
  }

  private double startDatingChance() {
    if (this.p1.hasSignificantOther() || this.p2.hasSignificantOther() ||
        this.p1.isMourningSpouse() || this.p2.isMourningSpouse()) {
      return 0;
    } else {
      return progressRomanticTypeChance();
    }
  }

  private double progressDesireChance(Random r) {
    return r.nextDouble(); // TODO: add some actual logic
  }

  private void progressRomanticType() {
    switch (this.getType()) {
      case FRIEND:
        this.romanticType = RelationshipType.PARTNER;
        this.p1.setSignificantOther(p2);
        p2.setSignificantOther(p1);
        break;
      case PARTNER:
        this.romanticType = RelationshipType.FIANCE;
        break;
      case FIANCE:
        this.romanticType = RelationshipType.SPOUSE;
        break;
      default:
        break;
    }
  }

  private double progressRomanticTypeChance() {
    double upperThreshold = (this.isRomantic()) ? this.getType().getUpperThreshold() : RelationshipType.PARTNER.getLowerThreshold();
    if (this.desire <= upperThreshold) {
      return 0;
    } else {
      return this.desire; //TODO: add some actual logic
    }
  }

  private double breakUpChance() { // should this also include a random factor?
    if (this.isRomantic()) {
      double reasonsToStay  = this.desire + this.regard /2; // TODO: add how long they've been together
      double total = 1.5;
      return total - reasonsToStay;
    } else {
      return  0;
    }

  }

  private void romanticToPlatonic() {
    this.romanticType = null;
  }

  private void breakUp() {
    this.p1.setSignificantOther(null);
    this.p2.setSignificantOther(null);
    this.romanticToPlatonic();
    // TODO: isn't there a list of past spouses/significant others? if so, this add to that
  }

  @Override
  public int getAnniversaryYear() {
    return this.anniversaryYear;
  }
}
