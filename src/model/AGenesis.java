package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AGenesis implements IGenesis {

	@Override
	public final void incrementTime(int yearsPast, Random r0) {
		for (int i = 0; i < yearsPast; i++) {
			incrementTime(new Random(r0.nextInt()));
		}
	}

	@Override
	public final long livingPopulationCount() {
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
		for(IPerson p : this.livingPopulation()){
			sb.append("\n");
			sb.append(p.basicPersonalInfo());
		}
		return sb.toString();
	}

	protected int timeInYears = 1900;

	@Override
	public void incrementTime(Random r) {
		timeInYears++;
		historicalPopulation().forEach(p -> p.incrementAge());
		pairOffCouples(r);
		tryForBabies(r);
		evaluateDeath(r);
	}

	private void tryForBabies(Random r) {
		List<IPerson> livingPeople = livingPopulation();
		for (int i = 0; i < livingPeople.size(); i++) {
			IPerson candidate1 = livingPeople.get(i);
			for (int j = i + 1; j < livingPeople.size(); j++) {
				IPerson candidate2 = livingPeople.get(j);
				double birthChance = GeneologyRules.fertilityChanceCouple(candidate1, candidate2);
				double birthRoll = r.nextDouble();
				if (birthRoll < birthChance) {
					IPerson child = candidate1.createChildWith(candidate2, timeInYears, new Random(r.nextInt()));
					addPerson(child);
					incrementIdCount();
				}
			}
		}
	}

	protected abstract void addPerson(IPerson child);

	@Override
	public List<IPerson> livingPopulation() {
		List<IPerson> livingPeople = new ArrayList<IPerson>();
		for (IPerson p : historicalPopulation()) {
			if (p.isLiving()) {
				livingPeople.add(p);
			}
		}
		return livingPeople;
	}

	private void pairOffCouples(Random r) {
		List<IPerson> livingPeople = livingPopulation();
		for (int i = 0; i < livingPeople.size(); i++) {
			IPerson candidate1 = livingPeople.get(i);
			for (int j = i + 1; j < livingPeople.size(); j++) {
				IPerson candidate2 = livingPeople.get(j);
				double marriageChance = GeneologyRules.marriageChance(candidate1, candidate2);
				double marriageRoll = r.nextDouble();
				if (marriageRoll < marriageChance) {
					candidate1.marry(candidate2);
				}
			}
		}
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
	
	public List<IPerson> getByIds(List<String> ids){
		List<IPerson> people = new ArrayList<IPerson>();
		for(IPerson p : this.livingPopulation()){
			if(ids.contains(p.getId())){
				people.add(p);
			}
		}
		return people;
	}

}
