package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeneologyRules {
	private static final double BASE_MARRIAGE_CHANCE = .7;
	public static final int FEMALE_MIN_FERTILE_AGE = 18;
	public static final int FEMALE_MAX_FERTILE_AGE = 50;
	private static final int FEMALE_FERTILITY_AGE_RANGE = FEMALE_MAX_FERTILE_AGE - FEMALE_MIN_FERTILE_AGE;
	public static final int MALE_MIN_FERTILE_AGE = 18;
	public static final int MALE_MAX_FERTILE_AGE = 50;
	private static final int MALE_FERTILITY_AGE_RANGE = MALE_MAX_FERTILE_AGE - MALE_MIN_FERTILE_AGE;
	private static final int MAX_MARRIAGE_AGE_GAP = 20;
	private static final double BASE_PREGNANCY_CHANCE = .80;
	private static final double OUT_OF_WEDLOCK_CHANCE = 0.05;
	private static final double OUT_OF_WEDLOCK_AND_MARRIED_CHANCE = 0.001;
	private static final int MAX_CHILDREN = 6;
	private static final int PREFERRED_CHILD_GAP = 3;
	public static final int MIN_MARRIAGE_AGE = 18;
	private static List<String> maleNames;
	private static List<String> femaleNames;

	static{
		try {
			Scanner mReader = new Scanner(new File("resources/male.txt"));
			Scanner fReader = new Scanner(new File("resources/female.txt"));
			maleNames = new ArrayList<String>();
			while(mReader.hasNextLine()){
				maleNames.add(mReader.nextLine().trim());
			}
			femaleNames = new ArrayList<String>();
			while(fReader.hasNextLine()){
				femaleNames.add(fReader.nextLine().trim());
			}
			mReader.close();
			fReader.close();
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

/*		if (p1.relationLevelMax(p2, 2)) {
			return 0;
		}*/
		if(p1.sharesGrandparentWith(p2)){
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
		return coupleChance * BASE_PREGNANCY_CHANCE;
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
		modifier *= ((double) yearsSinceLastChild / (double) PREFERRED_CHILD_GAP);
		if (sex == Sex.MALE) {
			if (age < MALE_MIN_FERTILE_AGE || age > MALE_MAX_FERTILE_AGE) {
				return 0;
			}
			modifier *= (double) (MALE_FERTILITY_AGE_RANGE - (age - MALE_MIN_FERTILE_AGE))
					/ (double) MALE_FERTILITY_AGE_RANGE;
		} else if (sex == Sex.FEMALE) {
			if (p.getChildren().size() == MAX_CHILDREN) {
				modifier = 0;
			} else {
				if (age < FEMALE_MIN_FERTILE_AGE || age > FEMALE_MAX_FERTILE_AGE) {
					return 0;
				}
				modifier *= (double) (FEMALE_FERTILITY_AGE_RANGE - (age - FEMALE_MIN_FERTILE_AGE))
						/ (double) FEMALE_FERTILITY_AGE_RANGE;
			}
		}

		return Math.min(1, modifier);
	}

	public static String getMarriedLastName(IPerson p1, IPerson p2) {
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
		/*if (p1.relationLevelMax(p2, 2)) {
			return 0;
		}*/
		if(p1.sharesGrandparentWith(p2)){
			return 0;
		}

		// We're just gonna based this on age for right now.
		return (1.0 - ((double)Math.abs(p1.getAge() - p2.getAge()) / ((double)(p1.getAge() + p2.getAge()) / 2))) * BASE_MARRIAGE_CHANCE;

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
/*		String[] maleNames = new String[] { "John", "Jacob", "Cain", "Abel", "Isaac", "Abraham", "Joseph", "Samuel",
				"Ezekiel", "Moses" };*/
		int rIndex = r.nextInt(maleNames.size());
		return maleNames.get(rIndex);
	}

	private static String getRandomFemaleName(Random r) {
/*		String[] femaleNames = new String[] { "Lilith", "Mary", "Delilah", "Veronica", "Ruth", "Bethany", "Abigail",
				"Serah", "Leah", "Grace" };*/
		int rIndex = r.nextInt(femaleNames.size());
		return femaleNames.get(rIndex);
	}
}
