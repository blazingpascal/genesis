package model.relationship;

import model.person.IPerson;

import java.util.Random;

/**
 * Created by alice on 3/21/2018.
 */
public interface IRelationship {

  public IPerson p1();

  public  IPerson p2();

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

  public boolean isRomantic();

  /**
   *
   * @return
   */
  public static double meetingChance(IPerson p1, IPerson p2) {
    return 1; //TODO
  }

  public void progressRelationship(Random r);

  public int getAnniversaryYear();
}
