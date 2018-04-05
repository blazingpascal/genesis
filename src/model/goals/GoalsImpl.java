package model.goals;

import model.person.IPerson;
import model.relationship.IRelationship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alice on 4/4/2018.
 */
public class GoalsImpl implements IGoals {
  List<IRelationship> friendshipsToNurture;
  List<IRelationship> peopleToAntagonize;
  List<IRelationship> peopleToWoo;
  IRelationship relationship;

  private static final int NURTURE_FRIENDSHIP_COST = 1;
  private static final int ANTAGONIZE_COST = 1;
  private static final int WOO_COST = 1;
  private static final int ADVANCE_RELATIONSHIP_COST = 3;

  public GoalsImpl() {
    this.friendshipsToNurture = new ArrayList<>();
    this.peopleToAntagonize = new ArrayList<>();
    this.peopleToWoo = new ArrayList<>();
  }

  @Override
  public boolean meetNewPeople() {
    return false;
  }

  /**
   * @param friendship the friendship to nurture
   * @param actionPoints the number of action points available
   * @return the number of action points left after this action
   */
  public int nurtureFriendship(IRelationship friendship, int actionPoints) {
    if (actionPoints - NURTURE_FRIENDSHIP_COST > 0) {
      actionPoints -= NURTURE_FRIENDSHIP_COST;
      friendshipsToNurture.add(friendship);
    } // do we want to do anything if they try to do it but don't have any more points?
    return actionPoints;
  }

  @Override
  public List<IRelationship> friendshipsToNurture() {
    return friendshipsToNurture;
  }

  public int antagonize(IRelationship relationship, int actionPoints) {
    if (actionPoints - ANTAGONIZE_COST > 0) {
      actionPoints -= ANTAGONIZE_COST;
      peopleToAntagonize.add(relationship);
    }
    return actionPoints;
  }

  @Override
  public List<IRelationship> peopleToAntagonize() {
    return peopleToAntagonize;
  }

  /**
   * @param relationship the relationship in which you want to increase desire
   * @param actionPoints the number of action points available
   * @return the number of action points left after this action
   */
  public int woo(IRelationship relationship, int actionPoints) {
    if (actionPoints - WOO_COST > 0) {
      actionPoints -= WOO_COST;
      peopleToWoo.add(relationship);
    }
    return actionPoints;
  }

  @Override
  public List<IRelationship> peopleToWoo() {
    return peopleToWoo;
  }

  /**
   * @param relationship the relationship you want to try to take to the next stage
   * @param actionPoints the number of action points available
   * @return the number of action points left after this action
   */
  public int takeTheNextStep(IRelationship relationship, int actionPoints) {
    if (actionPoints - ADVANCE_RELATIONSHIP_COST < 0) {
      actionPoints -= ADVANCE_RELATIONSHIP_COST;
      this.relationship = relationship;
    }
    return actionPoints;
  }

  @Override
  public IRelationship relationshipToAdvance() {
    return relationship;
  }
}
