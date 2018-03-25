package model.relationship;


import model.person.IPerson;

/**
 * Created by alice on 3/21/2018.
 */
public class SignificantOther extends RelationshipImpl {


  private RelationshipType type;

  public SignificantOther(IPerson you, IRelationship other) {
    super(you, other.withWhom(), other.regard(), other.romanticDesire(), other.getAnniversaryYear());
    this.type = RelationshipType.PARTNER;
  }

  @Override
  public RelationshipType getType() {
    return this.type;
  }

  public RelationshipType progressRelationship() {
    switch (this.type) {
      case PARTNER:
        this.type = RelationshipType.FIANCE;
        break;
      case FIANCE:
        this.type = RelationshipType.SPOUSE;
        break;
    }
    return this.type;
  }

  public double breakUpChance() {
    double reasonsToStay  = super.romanticDesire() + super.regard(); // TODO: add how long they've been together
    double total = 2;

    return total - reasonsToStay;
  }
}
