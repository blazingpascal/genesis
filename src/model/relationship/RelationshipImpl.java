package model.relationship;

import model.person.IPerson;

/**
 * Created by alice on 3/21/2018.
 */
public class RelationshipImpl implements IRelationship {
  private double regard;
  private double desire;
  private IPerson person;

  public RelationshipImpl(IPerson other) {
    this(other, 0);
  }

  public RelationshipImpl(IPerson other, double regard) {
    this(other, regard, 0);
  }

  public RelationshipImpl(IPerson other, double regard, double desire) {
    this.regard = regard;
    this.desire = desire;
    this.person = other;
  }


  @Override
  public IPerson withWhom() {
    return this.person;
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
    if (this.regard > 0) {
      return RelationshipType.FRIEND;
    } else if (this.regard == 0) {
      return RelationshipType.UNKNOWN;
    } else {
      return RelationshipType.ENEMY;
    }
  }
}
