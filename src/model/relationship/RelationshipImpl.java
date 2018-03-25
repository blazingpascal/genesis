package model.relationship;

import model.GeneologyRules;
import model.person.IPerson;

import java.util.Optional;
import java.util.Random;

/**
 * Created by alice on 3/21/2018.
 */
public class RelationshipImpl implements IRelationship {
  private double regard;
  private double desire;
  private IPerson you;
  private IPerson other;
  private int anniversaryYear;
  private RelationshipType type;

  public RelationshipImpl(IPerson you, IPerson other, int anniversaryYear) {
    this(you, other, GeneologyRules.computeRegard(you, other), anniversaryYear);
  }

  public RelationshipImpl(IPerson you, IPerson other, double regard, int anniversaryYear) {
    this(you, other, regard, GeneologyRules.computeDesire(you, other), anniversaryYear);
  }

  public RelationshipImpl(IPerson you, IPerson other, double regard, double desire, int anniversaryYear) {
    this(you, other, regard, desire, anniversaryYear, null);
  }

  public RelationshipImpl(IPerson you, IPerson other, double regard, double desire, int anniversaryYear, RelationshipType type) {
    this.regard = regard;
    this.desire = desire;
    this.you = you;
    this.other = other;
    this.anniversaryYear = anniversaryYear;
    this.type = type;
  }


  @Override
  public IPerson withWhom() {
    return this.other;
  }

  @Override
  public double regard() {
    return this.regard;
  }

  @Override
  public void growRegard() {
    this.regard += 0.1;
  }

  @Override
  public void lessenRegard() {
    this.regard -= 0.1;
  }

  @Override
  public double romanticDesire() {
    return this.desire;
  }

  @Override
  public void growDesire() {
    this.desire += 0.1;
  }

  @Override
  public void lessenDesire() {
    this.desire -= 0.1;
  }

  @Override
  public RelationshipType getType() {
    if (this.type == null) {
      if (this.regard > 0) {
        return RelationshipType.FRIEND;
      } else if (this.regard == 0) {
        return RelationshipType.UNKNOWN;
      } else {
        return RelationshipType.ENEMY;
      }
    } else {
      return this.type;
    }
  }

  @Override
  public void progressRelationship(Random r) {
    progressPlatonic(r);
    if (significantOtherType(this.getType())) {
      double chance = progressRomanticChance();
      double roll = r.nextDouble();
      if (roll < chance) {
        progressRomantic();
      }
    }
  }

  private void progressPlatonic(Random r) {
    //
  }

  private double datingChance() {
    if (this.you.hasSignificantOther() || this.other.hasSignificantOther()) {
      return 0;
    }
    double otherDesire = other.getRelationships().get(you).romanticDesire();
    if (otherDesire < RelationshipType.PARTNER.getThreshold() || this.desire < RelationshipType.PARTNER.getThreshold()) {
      return 0;
    } else {
      return otherDesire + this.desire / 2; //TODO: will probably change this to include some other factors. maybe.
    }
  }

  private RelationshipType progressRomantic() {
    switch (this.getType()) {
      case FRIEND:
        this.type = RelationshipType.PARTNER;
        break;
      case PARTNER:
        this.type = RelationshipType.FIANCE;
        break;
      case FIANCE:
        this.type = RelationshipType.SPOUSE;
        break;
      default:
        break;
    }
    return this.type;
  }

  private double progressRomanticChance() {
    return 0; //TODO
  }

  private double breakUpChance() { // should this also include a random factor?
    if (significantOtherType(this.getType())) {
      double reasonsToStay  = this.desire + this.regard/2; // TODO: add how long they've been together
      double total = 1.5;
      return total - reasonsToStay;
    } else {
      return  0;
    }

  }

  private boolean significantOtherType(RelationshipType type) {
    return type == RelationshipType.PARTNER || type == RelationshipType.FIANCE || type == RelationshipType.SPOUSE;
  }

  private void breakUp() {
    this.you.setSignificantOther(null);
    this.other.setSignificantOther(null);
    // TODO: isn't there a list of past spouses/significant others? if so, this add to that
  }

  @Override
  public int getAnniversaryYear() {
    return this.anniversaryYear;
  }
}
