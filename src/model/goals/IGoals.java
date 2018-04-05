package model.goals;

import model.relationship.IRelationship;

import java.util.List;

/**
 * Created by alice on 4/4/2018.
 */
public interface IGoals {

  boolean meetNewPeople();

  List<IRelationship> friendshipsToNurture();

  List<IRelationship> peopleToAntagonize();

  List<IRelationship> peopleToWoo();

  IRelationship relationshipToAdvance();

}
