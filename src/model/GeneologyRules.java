package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import model.genetics.subtypes.HairColorTrait;
import model.person.IPerson;

public class GeneologyRules {
	private static final double BASE_MARRIAGE_CHANCE = .7;
	public static final int FEMALE_MIN_FERTILE_AGE = 18;
	public static final int FEMALE_MAX_FERTILE_AGE = 50;
	private static final int FEMALE_FERTILITY_AGE_RANGE = FEMALE_MAX_FERTILE_AGE - FEMALE_MIN_FERTILE_AGE;
	public static final int MALE_MIN_FERTILE_AGE = 18;
	public static final int MALE_MAX_FERTILE_AGE = 50;
	private static final int MALE_FERTILITY_AGE_RANGE = MALE_MAX_FERTILE_AGE - MALE_MIN_FERTILE_AGE;
	private static final int MAX_MARRIAGE_AGE_GAP = 6;
	private static final double BASE_PREGNANCY_CHANCE = .50;
	private static final int PREFERRED_WIDOW_MOURNING_PERIOD = 3;

	/*
	 * private static final double OUT_OF_WEDLOCK_CHANCE = 0.05; private static
	 * final double OUT_OF_WEDLOCK_AND_MARRIED_CHANCE = 0.001;
	 */

	private static final double OUT_OF_WEDLOCK_CHANCE = .1;
	private static final double OUT_OF_WEDLOCK_AND_MARRIED_CHANCE = .01;

	private static final int PREFERRED_NUMBER_OF_KIDS = 3;
	private static final int MAX_CHILDREN = 10;
	private static final int PREFERRED_CHILD_GAP = 5;
	public static final int MIN_MARRIAGE_AGE = 18;
	private static List<String> maleNames;
	private static List<String> femaleNames;
	private static List<String> lastNames;
	private static HashSet<Integer> alreadyPickedLastNamesIndices = new HashSet<Integer>();

	static {
		try {
			Scanner mReader = new Scanner(new File("resources/male.txt"));
			Scanner fReader = new Scanner(new File("resources/female.txt"));
			Scanner lastNameReader = new Scanner(new File("resources/surnames.txt"));
			maleNames = new ArrayList<String>();
			while (mReader.hasNextLine()) {
				maleNames.add(mReader.nextLine().trim());
			}
			System.out.printf("%d male first names loaded\n", maleNames.size());
			femaleNames = new ArrayList<String>();
			while (fReader.hasNextLine()) {
				femaleNames.add(fReader.nextLine().trim());
			}
			System.out.printf("%d female first names loaded\n", femaleNames.size());
			lastNames = new ArrayList<String>();
			while (lastNameReader.hasNextLine()) {
				lastNames.add(lastNameReader.nextLine());
			}
			System.out.printf("%d last names loaded\n",lastNames.size());
			mReader.close();
			fReader.close();
			lastNameReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static double fertilityChanceCouple(IPerson p1, IPerson p2) {
		Sex p1sex = p1.getSex();
		Sex p2sex = p2.getSex();
		if (p1sex == p2sex) {
			return 0;
		}

		/*
		 * if (p1.relationLevelMax(p2, 2)) { return 0; }
		 */
		if (p1.atLeastCousins(p2)) {
			return 0;
		}

		double coupleChance = fertilityChance(p1) * fertilityChance(p2);
		boolean outOfWedlock = !p1.isMarriedTo(p2);
		if (outOfWedlock) {
			if (p1.isSingle() && p2.isSingle()) {
				coupleChance *= OUT_OF_WEDLOCK_CHANCE;
			} else {
				coupleChance *= OUT_OF_WEDLOCK_AND_MARRIED_CHANCE;
			}
		}
		return founderBasedAttractionModifier(p1, p2) * coupleChance * BASE_PREGNANCY_CHANCE;
	}

	/**
	 * Returns the likelihood that this IPerson can help with child conception.
	 * 
	 * @param p
	 * @return
	 */
	public static double fertilityChance(IPerson p) {
		int age = p.getAge();
		Sex sex = p.getSex();
		double modifier = 1;
		int yearsSinceLastChild = p.getYearsSinceLastChild();
		modifier *= Math.min(1, ((double) yearsSinceLastChild / (double) PREFERRED_CHILD_GAP));
		if (sex == Sex.MALE) {
			if (age < MALE_MIN_FERTILE_AGE || age > MALE_MAX_FERTILE_AGE) {
				return 0;
			}
			/*
			 * modifier *= ((double) (MALE_FERTILITY_AGE_RANGE - (age -
			 * MALE_MIN_FERTILE_AGE))) / (double) MALE_FERTILITY_AGE_RANGE;
			 */
			modifier *= (13.674192022749644 - 1.7885073342041384 * age + 0.09622799460742272 * Math.pow(age, 2)
					- 0.0024520153723827194 * Math.pow(age, 3) + 0.000029353412509580285 * Math.pow(age, 4)
					- 1.3346725220382162 * Math.pow(10, -7) * Math.pow(age, 5));
		} else if (sex == Sex.FEMALE) {
			int numChildren = p.getChildren().size();
			if (numChildren == MAX_CHILDREN) {
				modifier = 0;
			} else {
				if (age < FEMALE_MIN_FERTILE_AGE || age > FEMALE_MAX_FERTILE_AGE) {
					return 0;
				}
				/*
				 * modifier *= ((double) (FEMALE_FERTILITY_AGE_RANGE - (age -
				 * FEMALE_MIN_FERTILE_AGE))) / (double)
				 * FEMALE_FERTILITY_AGE_RANGE;
				 */
				modifier *= (13.674192022749644 - 1.7885073342041384 * age + 0.09622799460742272 * Math.pow(age, 2)
						- 0.0024520153723827194 * Math.pow(age, 3) + 0.000029353412509580285 * Math.pow(age, 4)
						- 1.3346725220382162 * Math.pow(10, -7) * Math.pow(age, 5));
				modifier *= ((double) (MAX_CHILDREN - (numChildren))) / MAX_CHILDREN;
				if(numChildren > PREFERRED_NUMBER_OF_KIDS){
					modifier *= (1 - ((double)numChildren - PREFERRED_NUMBER_OF_KIDS)/(MAX_CHILDREN - PREFERRED_NUMBER_OF_KIDS));
				}
			}
		}

		return Math.min(1, modifier);
	}

	public static String getMarriedLastName(IPerson p1, IPerson p2) {
		// Let's have age rules!
		int p1Age = p1.getAge();
		int p2Age = p2.getAge();
		if (p1Age != p2Age) {
			return p1Age > p2Age ? p1.getBirthLastName() : p2.getBirthLastName();
		}

		String p1LastNameContribution = p1.getBirthLastName();
		String p2LastNameContribution = p2.getBirthLastName();
		if (p1LastNameContribution.contains("-")) {
			p1LastNameContribution = p1LastNameContribution.substring(0, p1LastNameContribution.lastIndexOf("-"));
		}
		if (p2LastNameContribution.contains("-")) {
			p2LastNameContribution = p2LastNameContribution.substring(p2LastNameContribution.lastIndexOf("-") + 1,
					p2LastNameContribution.length());
		}
		return p1LastNameContribution + "-" + p2LastNameContribution;
	}

	public static double deathChance(IPerson p) {
		// This was calculated from US Gov't Actuarial Tables
		Sex sex = p.getSex();
		int age = p.getAge();
		if (sex == Sex.MALE) {
			// y = 2E-06x3 - 0.0002x2 + 0.0039x
			return 0.01 * (0.000002 * Math.pow(age, 3) + 0.0002 * Math.pow(age, 2) + 0.0039 * age);
		} else if (sex == Sex.FEMALE) {
			// y = 2E-06x3 - 0.0002x2 + 0.0047x
			return 0.01 * (0.000002 * Math.pow(age, 3) + 0.0002 * Math.pow(age, 2) + 0.0047 * age);
		}
		throw new IllegalArgumentException("Sex " + sex + " not supported");
	}

	public static double marriageChance(IPerson p1, IPerson p2) {
		Sex p1Sex = p1.getSex();
		Sex p2Sex = p2.getSex();

		if ((p1Sex == Sex.MALE && p1.getAge() < MALE_MIN_FERTILE_AGE)
				|| (p1Sex == Sex.FEMALE && p1.getAge() < FEMALE_MIN_FERTILE_AGE)
				|| (p2Sex == Sex.FEMALE && p2.getAge() < FEMALE_MIN_FERTILE_AGE)
				|| (p2Sex == Sex.MALE && p2.getAge() < MALE_MIN_FERTILE_AGE)) {
			return 0;
		}

		if (!p1.isSingle() || !p2.isSingle()) {
			return 0;
		}

		// Gay marriage is not supported at the moment. :(
		if (p1Sex == p2Sex) {
			return 0;
		}

		// No kissing cousins here
		/*
		 * if (p1.relationLevelMax(p2, 2)) { return 0; }
		 */
		if (p1.atLeastCousins(p2)) {
			return 0;
		}

		// Age factor
		int ageDiff = p1.getAge() - p2.getAge();
		double mChance;
		if (ageDiff == 0) {
			mChance = 1;
		} else {
			mChance = Math.pow(Math.abs(ageDiff) + 0.001, -2);
		}
		// Widowhood Factor
		if (p1.isMourningSpouse()) {
			mChance *= Math.min(1, ((double) (p1.getTimeMourningSpouse() / PREFERRED_WIDOW_MOURNING_PERIOD)));
		}
		if (p2.isMourningSpouse()) {
			mChance *= Math.min(1, ((double) (p2.getTimeMourningSpouse() / PREFERRED_WIDOW_MOURNING_PERIOD)));
		}

        // Trait Attraction
		double result = founderBasedAttractionModifier(p1, p2) * mChance * BASE_MARRIAGE_CHANCE;
        double p1Pref = modifyByTraitAttraction(result, p1, p2);
        double p2Pref = modifyByTraitAttraction(result, p2, p1);

        return (p1Pref + p2Pref) / 2;
	}

    private static double modifyByTraitAttraction(double chance, IPerson p1, IPerson p2) {
        Optional<HairColorTrait> pref = p1.getPreferredHair();
        if(!pref.isPresent()) return chance;

        double target = pref.get().getValue() == p2.getGenes().getHairColor().getValue() ? 1 : 0;
        return 0.8 * chance + 0.2 * target;
    }

	private static double founderBasedAttractionModifier(IPerson p1, IPerson p2) {
		Collection<String> p1Founders = p1.getFoundingLastNames();
		Collection<String> p2Founders = p2.getFoundingLastNames();
		int commonAncs = 0;
		for (String f : p1Founders) {
			if (p2Founders.contains(f)) {
				commonAncs++;
			}
		}
		int totalFounders = p1Founders.size() + p2Founders.size();
		if (totalFounders == 0) {
			return 1.0;
		}
		double mod = ((totalFounders - commonAncs)) / (double) totalFounders;
		return mod;
	}

	public static String getRandomLastName(Random r) {
		int rIndex = r.nextInt(lastNames.size());
		if (alreadyPickedLastNamesIndices.size() == lastNames.size()) {
			alreadyPickedLastNamesIndices.clear();
		}
		while (alreadyPickedLastNamesIndices.contains(rIndex)) {
			rIndex = r.nextInt(maleNames.size());
		}
		return lastNames.get(rIndex);
	}

	public static String getRandomFirstName(Sex sex, Random r) {
		switch (sex) {
		case FEMALE:
			return getRandomFemaleName(r);
		case MALE:
			return getRandomMaleName(r);
		default:
			throw new IllegalArgumentException("Sex " + sex + " not supported");
		}
	}

	private static String getRandomMaleName(Random r) {
		/*
		 * String[] maleNames = new String[] { "John", "Jacob", "Cain", "Abel",
		 * "Isaac", "Abraham", "Joseph", "Samuel", "Ezekiel", "Moses" };
		 */
		int rIndex = r.nextInt(maleNames.size());
		return maleNames.get(rIndex);
	}

	private static String getRandomFemaleName(Random r) {
		/*
		 * String[] femaleNames = new String[] { "Lilith", "Mary", "Delilah",
		 * "Veronica", "Ruth", "Bethany", "Abigail", "Serah", "Leah", "Grace" };
		 */
		int rIndex = r.nextInt(femaleNames.size());
		return femaleNames.get(rIndex);
	}
}
