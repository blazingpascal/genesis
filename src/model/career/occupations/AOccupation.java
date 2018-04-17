package model.career.occupations;

import model.person.APersonalInfoPerson;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;

import java.util.Random;

public abstract class AOccupation {

    public static final Culinary CULINARY = new Culinary();
    public static final Engineering ENGINEERING = new Engineering();
    public static final Finance FINANCE = new Finance();
    public static final Journalism JOURNALISM = new Journalism();
    public static final LawEnforcement LAW_ENFORCEMENT = new LawEnforcement();
    public static final Medical MEDICAL = new Medical();
    public static final Politics POLITICS = new Politics();
    public static final Science SCIENCE = new Science();
    public static final Teaching TEACHING = new Teaching();
    public static final VisualArt VISUAL_ART = new VisualArt();


    public static final AOccupation[] opts = new AOccupation[10];

    static{
        opts[0] = CULINARY;
        opts[1] = ENGINEERING;
        opts[2] = FINANCE;
        opts[3] = JOURNALISM;
        opts[4] = LAW_ENFORCEMENT;
        opts[5] = MEDICAL;
        opts[6] = POLITICS;
        opts[7] = SCIENCE;
        opts[8] = TEACHING;
        opts[9] = VISUAL_ART;
    }

    protected Random r;
    private String name;
    private double evalAmount;

    public AOccupation(String name, double evalAmount) {
        this.r = new Random();
        this.name = name;
        this.evalAmount = evalAmount;
    }

    public String getName() {
        return name;
    }

    public boolean interview(APersonalInfoPerson person) {
        double chance = (evaluateFit(person) + r.nextDouble()) / 2;
        return chance > 0.55;
    }

    abstract public double evaluateFit(APersonalInfoPerson person);

    protected double addValue(IPersonality p, PersonalityTrait t) {
        if(p.getTraitValue(t) > 0.5) {
            return evalAmount;
        }
        return 0;
    }

    protected double addValueAdditional(IPersonality p, PersonalityTrait t) {
        if(p.getTraitValue(t) > 0.5) {
            if(p.getTraitValue(t) > 0.75) {
                return 2 * evalAmount;
            }
            return evalAmount;
        }
        return 0;
    }

    protected double addValueAbsence(IPersonality p, PersonalityTrait t) {
        if(p.getTraitValue(t) < 0.5) {
            return evalAmount;
        }
        return 0;
    }

    protected double addValueAbsenceAdditional(IPersonality p, PersonalityTrait t) {
        if(p.getTraitValue(t) < 0.5) {
            if(p.getTraitValue(t) < 0.25) {
                return 2 * evalAmount;
            }
            return evalAmount;
        }
        return 0;
    }

}
