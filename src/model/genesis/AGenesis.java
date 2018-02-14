package model.genesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.GeneologyRules;
import model.Sex;
import model.person.IPerson;

public abstract class AGenesis implements IGenesis {

	List<IPerson> livingCache;
	private long livingPopulationCacheCount;

	@Override
	public final void incrementTime(int yearsPast, Random r0) {
		for (int i = 0; i < yearsPast; i++) {
			incrementTime(new Random(r0.nextInt()));
		}
	}

	@Override
	public final long livingPopulationCount() {
		//if(livingCache == null) { livingPopulation();}
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

	protected int timeInYears = 1900;

	@Override
	public void incrementTime(Random r) {
		//reset cache.
		this.livingCache = null;
		timeInYears++;
		for (IPerson p : livingPopulation()) {
			p.incrementAge();
		}
		pairOffCouples(r);
		tryForBabies(r);
		evaluateDeath(r);
		cleanUp();
	}

	protected void cleanUp() {
		// Do nothing normally
	}

	protected void tryForBabies(Random r) {
		List<IPerson> fertilePopulation = fertilePopulation();
		for (int i = 0; i < fertilePopulation.size(); i++) {
			IPerson candidate1 = fertilePopulation.get(i);
			for (int j = i + 1; j < fertilePopulation.size(); j++) {
				IPerson candidate2 = fertilePopulation.get(j);
				double birthChance = GeneologyRules.fertilityChanceCouple(candidate1, candidate2);
				double birthRoll = r.nextDouble();
				if (birthRoll < birthChance) {
					IPerson child = candidate1.createChildWith(candidate2, timeInYears, new Random(r.nextInt()));
					addChild(child);
				}
			}
		}
	}

	protected abstract void addChild(IPerson child);

	@Override
	public List<IPerson> livingPopulation() {
		//if (livingCache == null) {
			List<IPerson> livingPopulation = new ArrayList<IPerson>();
			for (IPerson p : historicalPopulation()) {
				if (p.isLiving()) {
					livingPopulation.add(p);
				}
			}
		//}
		//this.livingPopulationCacheCount = livingCache.size();
		return livingPopulation;
	}

	private void pairOffCouples(Random r) {
		List<IPerson> pairablePeople = pairablePopulation();
		for (int i = 0; i < pairablePeople.size(); i++) {
			IPerson candidate1 = pairablePeople.get(i);
			for (int j = i + 1; j < pairablePeople.size(); j++) {
				IPerson candidate2 = pairablePeople.get(j);
				double marriageChance = GeneologyRules.marriageChance(candidate1, candidate2);
				double marriageRoll = r.nextDouble();
				if (marriageRoll < marriageChance) {
					candidate1.marry(candidate2, this.timeInYears);
				}
			}
		}
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

}
