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
   * @return a number from -1 to 1, where -1 means they're worst enemies,
   *         1 means they're best friends, and 0 means they don't know each other
   */
  public double regard();

  /**
   * increments the regard between the people in this relationship
   */
  public void growRegard();

  /**
   * decrements the regard between the people in this relationship
   */
  public void lessenRegard();

  /**
   * @return a number from -1 to 1, where -1 means they'd never ever date, 1 is means they're completely in love
   */
  public double romanticDesire();

  /**
   * increments the romantic desire between the people in this relationship
   */
  public void growDesire();

  /**
   * decrements the romantic desire between the people in this relationship
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
