package model.relationship;


/**
 * Created by alice on 3/21/2018.
 */
public class SignificantOther extends RelationshipImpl {


  private RelationshipType type;

  public SignificantOther(IRelationship other) {
    super(other.withWhom(), other.regard(), other.romanticDesire());
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
}
