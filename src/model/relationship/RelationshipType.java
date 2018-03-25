package model.relationship;

/**
 * Created by alice on 3/21/2018.
 */
public enum RelationshipType {
  ENEMY(.3), ANNOYANCE(.1), UNKNOWN(0), ACQUAINTANCE(.1), FRIEND(.3), PARTNER(.3), FIANCE(.5), SPOUSE(.7);

  private double threshold;

  RelationshipType(double threshold) {
    this.threshold = threshold;
  }

  public double getThreshold() {
    return this.threshold;
  }

   /*
   do we want to automatically advance stages based on thresholds, and keep the thresholds in the enums themselves
   rather than the constants class?

   do we want the regard ones to be separate from the romantic ones?
    */
}
