package model.person;

import java.util.Random;

public class CombinationRole extends ARole {

    private final RomanticRole romantic;
    private final PlatonicRole platonic;
    private final CareerRole career;
    private final NarrativeRole narrative;

    CombinationRole(String title, float strength, RomanticRole romantic, PlatonicRole platonic, CareerRole career,
                    NarrativeRole narrative) {
        super(title, strength);
        this.romantic = romantic;
        this.platonic = platonic;
        this.career = career;
        this.narrative = narrative;
    }

    CombinationRole(RomanticRole romantic, PlatonicRole platonic, CareerRole career, NarrativeRole narrative) {
        super("", 0);
        this.romantic = romantic;
        this.platonic = platonic;
        this.career = career;
        this.narrative = narrative;
        this.title = calculateTitle();
        this.strength = calculateStrength();
    }

    private float calculateStrength() {
        return (this.romantic.strength + this.platonic.strength + this.career.strength + this.narrative.strength) / 4;
    }

    private String calculateTitle() {
        return String.join(";", romantic.title, platonic.title, career.title, narrative.title);
    }

    ARole merge(ARole r2) {
        return r2.mergeCoR(this);
    }

    @Override
    public double computeRomanticCompatibility(ARole r) {
        if (r instanceof CombinationRole) {
            return this.romantic.computeRomanticCompatibility(((CombinationRole) r).romantic);
        }
        return this.romantic.computeRomanticCompatibility(r);
    }

    @Override
    public double computeCareerProgressionModifier() {
        return career.computeCareerProgressionModifier();
    }

    @Override
    public double getCareerFocus() {
        return career.getCareerFocus();
    }

    @Override
    public double getCareerTenacity() {
        return career.getCareerTenacity();
    }

    protected ARole mergeCoR(CombinationRole cr) {
        RomanticRole romantic = (RomanticRole) this.romantic.merge(cr.romantic);
        PlatonicRole platonic = (PlatonicRole) this.platonic.merge(cr.platonic);
        CareerRole career = (CareerRole) this.career.merge(cr.career);
        NarrativeRole narrative = (NarrativeRole) this.narrative.merge(cr.narrative);
        return new CombinationRole(romantic, platonic, career, narrative);
    }

    public RomanticRole getRomantic() {
        return romantic;
    }

    public PlatonicRole getPlatonic() {
        return platonic;
    }

    public CareerRole getCareer() {
        return career;
    }

    public NarrativeRole getNarrative() {
        return narrative;
    }
}
