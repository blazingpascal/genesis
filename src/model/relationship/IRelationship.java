package model.relationship;

import model.person.IPerson;

/**
 * Created by alice on 3/21/2018.
 */
public interface IRelationship {

  public IPerson withWhom();

  /**
   * @return a number from -1 to 1, where -1 is their worst enemy, 1 is their best friend, and 0 is someone they don't know
   */
  public double regard();

  /**
   * increments the regard given to the person with whom this relationship is
   */
  public void growRegard();

  /**
   * decrements the regard given to the person with whom this relationship is
   */
  public void lessenRegard();

  /**
   * @return a number from -1 to 1, where -1 is someone they'd never ever date, 1 is someone they're completely in love with
   */
  public double romanticDesire();

  /**
   * increments the romantic desire felt for the person with whom this relationship is
   */
  public void growDesire();

  /**
   * decrements the romantic desire felt for the person with whom this relationship is
   */
  public void lessenDesire();

  public RelationshipType getType();

}
