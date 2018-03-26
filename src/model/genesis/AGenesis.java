package model.genesis;

import java.util.*;

import model.GeneologyRules;
import model.Sex;
import model.person.IPerson;
import model.relationship.IRelationship;

public abstract class AGenesis implements IGenesis {

	List<IPerson> livingCache;
	private long livingPopulationCacheCount;
	protected int timeInYears = 1900;
	int maxGen = 0;

	private static final int COUPLING_ATTEMPTS_PER_YEAR = 1;
	private static final int PREGNANCY_ATTEMPTS_PER_YEAR = 2;
	private static final int MEETING_ATTEMPTS_PER_YEAR = 1;
	private static final int RELATIONSHIP_CHANGES_PER_YEAR = 5;

	@Override
	public final void incrementTime(int yearsPast, Random r0) {
		for (int i = 0; i < yearsPast; i++) {
			incrementTime(new Random(r0.nextInt()));
		}
	}

	@Override
	public final long livingPopulationCount() {
		// if(livingCache == null) { livingPopulation();}
		return this.livingPopulation().size();
	}

	@Override
	public long deadPopulationCount() {
		return this.deadPopulation().size();
	}

	@Override
	public List<IPerson> deadPopulation() {
		List<IPerson> deadPeople = new ArrayList<IPerson>();
		for (IPerson p : historicalPopulation()) {
			if (!p.isLiving()) {
				deadPeople.add(p);
			}
		}
		return deadPeople;
	}

	@Override
	public long historicalPopulationCount() {
		return historicalPopulation().size();
	}

	@Override
	public String livingPersonalPopulationSummary() {
		StringBuilder sb = new StringBuilder("Personal Population Summary");
		for (IPerson p : this.livingPopulation()) {
			sb.append("\n");
			sb.append(p.basicPersonalInfo());
		}
		return sb.toString();
	}

	@Override
	public void incrementTime(Random r) {
		// reset cache.
		this.livingCache = null;
		timeInYears++;
		for (IPerson p : livingPopulation()) {
			p.incrementAge();
		}
		progressRelationships(r);
		meetPeople(r);
		pairOffCouples(r);
		tryForBabies(r);
		evaluateDeath(r);
		cleanUp();
	}

	protected void cleanUp() {
		// Do nothing normally
	}

	protected void tryForBabies(Random r) {
		/*
		 * List<IPerson> fertilePopulation = fertilePopulation(); for (int i =
		 * 0; i < fertilePopulation.size(); i++) { IPerson candidate1 =
		 * fertilePopulation.get(i); for (int j = i + 1; j <
		 * fertilePopulation.size(); j++) { IPerson candidate2 =
		 * fertilePopulation.get(j); double birthChance =
		 * GeneologyRules.fertilityChanceCouple(candidate1, candidate2); double
		 * birthRoll = r.nextDouble(); if (birthRoll < birthChance) { IPerson
		 * child = candidate1.createChildWith(candidate2, timeInYears, new
		 * Random(r.nextInt())); addChild(child); } } }
		 */
		// This is incredibly heteronormative but unfortunately
		// it may be more efficient
		List<IPerson> fathers = potentialFatherPopulation();
		List<IPerson> mothers = potentialMotherPopulation();
		// Check for married couples first
		List<IPerson> morePeople = fathers.size() > mothers.size() ? fathers : mothers;

		int max = Math.min(fathers.size(), mothers.size());
		for (int attempt = 0; attempt < PREGNANCY_ATTEMPTS_PER_YEAR; attempt++) {
			for (IPerson p : morePeople) {
				if (!p.isSingle()) {
					IPerson spouse = p.getSpouse();
					double chance = GeneologyRules.fertilityChanceCouple(p, spouse);
					double roll = r.nextDouble();
					if (roll < chance) {
						IPerson child = p.createChildWith(spouse, timeInYears, new Random(r.nextInt()));
						addChild(child);
						this.maxGen = Math.max(this.maxGen, child.getGeneration());
					}
				}
			}
			Collections.shuffle(fathers);
			Collections.shuffle(mothers);
			for (int i = 0; i < max; i++) {
				IPerson father = fathers.get(i);
				IPerson mother = mothers.get(i);
				double chance = GeneologyRules.fertilityChanceCouple(father, mother);
				double roll = r.nextDouble();
				if (roll < chance) {
					IPerson child = father.createChildWith(mother, timeInYears, new Random(r.nextInt()));
					addChild(child);
					this.maxGen = Math.max(this.maxGen, child.getGeneration());
				}
			}
		}
	}

	private List<IPerson> potentialMotherPopulation() {
		List<IPerson> potentialMothers = new ArrayList<IPerson>();
		for (IPerson p : this.fertilePopulation()) {
			if (p.getSex() == Sex.FEMALE) {
				potentialMothers.add(p);
			}
		}
		return potentialMothers;
	}

	private List<IPerson> potentialFatherPopulation() {
		List<IPerson> potentialFathers = new ArrayList<IPerson>();
		for (IPerson p : this.fertilePopulation()) {
			if (p.getSex() == Sex.MALE) {
				potentialFathers.add(p);
			}
		}
		return potentialFathers;
	}

	protected abstract void addChild(IPerson child);

	@Override
	public List<IPerson> livingPopulation() {
		// if (livingCache == null) {
		List<IPerson> livingPopulation = new ArrayList<IPerson>();
		for (IPerson p : historicalPopulation()) {
			if (p.isLiving()) {
				livingPopulation.add(p);
			}
		}
		// }
		// this.livingPopulationCacheCount = livingCache.size();
		return livingPopulation;
	}

	private void pairOffCouples(Random r) {
		// This is incredibly heteronormative but unfortunately
		// it may be more efficient
		List<IPerson> bachelors = bachelorPopulation();
		List<IPerson> bachelorettes = bachelorettePopulation();

		for (int attempt = 0; attempt < COUPLING_ATTEMPTS_PER_YEAR; attempt++) {
			int max = Math.min(bachelors.size(), bachelorettes.size());
			Collections.shuffle(bachelors);
			Collections.shuffle(bachelorettes);
			for (int i = 0; i < max; i++) {
				IPerson bachelor = bachelors.get(i);
				IPerson bachelorette = bachelorettes.get(i);
				double chance = GeneologyRules.marriageChance(bachelor, bachelorette);
				double roll = r.nextDouble();
				if (roll < chance) {
					bachelor.marry(bachelorette, this.timeInYears);
				}
			}
			bachelors.removeIf(b -> !b.isSingle());
			bachelorettes.removeIf(b -> !b.isSingle());
		}
	}

	private List<IPerson> bachelorettePopulation() {
		List<IPerson> bachelorettes = new ArrayList<IPerson>();
		for (IPerson p : this.pairablePopulation()) {
			if (p.getSex() == Sex.FEMALE) {
				bachelorettes.add(p);
			}
		}
		return bachelorettes;
	}

	private List<IPerson> bachelorPopulation() {
		List<IPerson> bachelors = new ArrayList<IPerson>();
		for (IPerson p : this.pairablePopulation()) {
			if (p.getSex() == Sex.MALE) {
				bachelors.add(p);
			}
		}
		return bachelors;
	}

	private List<IPerson> pairablePopulation() {
		List<IPerson> pairables = new ArrayList<IPerson>();
		for (IPerson p : this.livingPopulation()) {
			if (p.getAge() > GeneologyRules.MIN_MARRIAGE_AGE && p.isSingle()) {
				pairables.add(p);
			}
		}
		return pairables;
	}

	private List<IPerson> fertilePopulation() {
		List<IPerson> fertiles = new ArrayList<IPerson>();
		for (IPerson p : this.livingPopulation()) {
			if (p.getSex() == Sex.MALE) {
				if (p.getAge() <= GeneologyRules.MALE_MAX_FERTILE_AGE
						&& p.getAge() >= GeneologyRules.MALE_MIN_FERTILE_AGE) {
					fertiles.add(p);
				}
			} else {
				if (p.getAge() <= GeneologyRules.FEMALE_MAX_FERTILE_AGE
						&& p.getAge() >= GeneologyRules.FEMALE_MIN_FERTILE_AGE) {
					fertiles.add(p);
				}
			}
			if (p.getYearsSinceLastChild() > 0) {
				if (p.getSex() == Sex.MALE) {
					if (p.getAge() <= GeneologyRules.MALE_MAX_FERTILE_AGE
							&& p.getAge() >= GeneologyRules.MALE_MIN_FERTILE_AGE) {
						fertiles.add(p);
					}
				}
			} else {
				if (p.getAge() <= GeneologyRules.FEMALE_MAX_FERTILE_AGE
						&& p.getAge() >= GeneologyRules.FEMALE_MIN_FERTILE_AGE) {
					fertiles.add(p);
				}
			}
		}
		return fertiles;

	}

	private void evaluateDeath(Random r) {
		for (IPerson p : livingPopulation()) {
			double deathChance = GeneologyRules.deathChance(p);
			double deathRoll = r.nextDouble();
			if (deathRoll < deathChance) {
				p.kill(timeInYears);
			}
		}
	}

	public List<IPerson> getByIds(List<String> ids) {
		List<IPerson> people = new ArrayList<IPerson>();
		for (IPerson p : this.livingPopulation()) {
			if (ids.contains(p.getId())) {
				people.add(p);
			}
		}
		return people;
	}
	
	public int maxGeneration(){
		return this.maxGen;
	}

	private void meetPeople(Random r) {
		// efficiency... I got nothing. so this is a stopgap until we can talk about it some.
		List<IPerson> people = livingPopulation();

		for (int attempt = 0; attempt < MEETING_ATTEMPTS_PER_YEAR; attempt++) {
			int max = people.size()/2;
			Collections.shuffle(people);
			for (int i = 0; i < max; i++) {
				IPerson p1 = people.get(i);
				IPerson p2 = people.get(i * 2);
				double chance = IRelationship.meetingChance(p1, p2);
				double roll = r.nextDouble();
				if (roll < chance) {
					p1.meet(p2, this.timeInYears);
				}
			}
		}
	}

	private void progressRelationships(Random r) {
		// efficiency considerations? because this is gonna somehow have to go through every relationship in the pop
		// or maybe for every person, it picks a max number of relationships to change?
		// so like go through each persons' list of relationships, and for the ones that are chosen, progress relationship?

		for (IPerson p : this.livingPopulation()) {
			int numRelationships = Math.min(p.getRelationships().size(), RELATIONSHIP_CHANGES_PER_YEAR);
			Iterator<IRelationship> it = p.getRelationships().values().iterator();
			for (int i = 0; i < numRelationships; i++) { //
				it.next().progressRelationship(r);
			}
		}

	}

}
