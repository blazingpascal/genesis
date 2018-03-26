package model.relationship;

/**
 * Created by alice on 3/21/2018.
 */
public enum RelationshipType {
  ENEMY(-1.1, -.3, false),
  ANNOYANCE(-.3, 0, false),
  UNKNOWN(0, 0, false),
  ACQUAINTANCE(0, .3, false),
  FRIEND(.3, 1, false),
  PARTNER(.3, .5, true),
  FIANCE(.5, .7, true),
  SPOUSE(.7, 1, true);

  private final double lowerThreshold;
  private final double upperThreshold;
  private final boolean isRomantic;


  RelationshipType(double lowerThreshold, double upperThreshold, boolean isRomantic) {
    this.lowerThreshold = lowerThreshold;
    this.upperThreshold = upperThreshold;
    this.isRomantic = isRomantic;
  }

  /**
   * @return lower threshold (exclusive) for this relationship type
   */
  public double getLowerThreshold() {
    return this.lowerThreshold;
  }

  /**
   * @return upper threshold (inclusive) for this relationship type
   */
  public double getUpperThreshold() {
    return this.upperThreshold;
  }

  public boolean isRomantic() {
    return this.isRomantic;
  }

  public RelationshipType getFromStats(double regard, double desire) {
    if (this.isRomantic) {
      return romanticType(desire);
    } else {
      return platonicType(regard);
    }
  }

  public static RelationshipType platonicType(double regard) {
    if (regard > FRIEND.getLowerThreshold()) {
      return FRIEND;
    } else if (regard > ACQUAINTANCE.getLowerThreshold()) {
      return  ACQUAINTANCE;
    } else if (regard > ANNOYANCE.getLowerThreshold()) {
      return ANNOYANCE;
    } else {
      return ENEMY;
    }
  }

  public static RelationshipType romanticType(double desire) {
    if (desire > SPOUSE.getLowerThreshold()) {
      return SPOUSE;
    } else if (desire > FIANCE.getLowerThreshold()) {
      return FIANCE;
    } else {
      return PARTNER;
    }
  }

}
