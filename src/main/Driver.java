package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import model.GeneologyRules;
import model.Sex;
import model.career.CareerManager;
import model.career.Job;
import model.career.occupations.AOccupation;
import model.genesis.IGenesis;
import model.genesis.idbased.ILifeEventEnabledGenesis;
import model.genesis.idbased.LifeEventEnabledGenesisImpl;
import model.genetics.GeneticsMap;
import model.genetics.JSONTraits;
import model.genetics.TraitData;
import model.lifeevents.BirthLifeEvent;
import model.lifeevents.ILifeEvent;
import model.person.IPerson;
import model.person.ARole;
import model.person.CombinationRole;
import model.personality.IPersonality;
import model.personality.PersonalityTrait;
import model.relationship.IRelationship;
import model.relationship.RelationshipType;
import model.spousehistory.ISpouseHistory;

public class Driver {
	public static final int STARTING_MALE_FOUNDERS = 250;
	public static final int STARTING_FEMALE_FOUNDERS = 250;
	public static final int MINIMUM_FOUNDER_AGE = 18;
	public static final int MAXIMUM_FOUNDER_AGE = 25;

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		List<IPerson> founders = new ArrayList<IPerson>();
		ILifeEventEnabledGenesis genesis = new LifeEventEnabledGenesisImpl();

		JSONTraits.loadJSONTraits("resources/genetics/traits.json");

		for (int i = 0; i < STARTING_MALE_FOUNDERS; i++) {
			String firstName = GeneologyRules.getRandomFirstName(Sex.MALE, new Random());
			String lastName = GeneologyRules.getRandomLastName(new Random());
			int age = MINIMUM_FOUNDER_AGE + new Random().nextInt(MAXIMUM_FOUNDER_AGE - MINIMUM_FOUNDER_AGE);
			founders.add(
					genesis.addSinglePerson(firstName, lastName, Sex.MALE, age, GeneticsMap.randomGenes(new Random()),
							ARole.getRandomRole(new Random(), false), IPersonality.randomPersonality(new Random())));
		}
		for (int i = 0; i < STARTING_FEMALE_FOUNDERS; i++) {
			String firstName = GeneologyRules.getRandomFirstName(Sex.FEMALE, new Random());
			String lastName = GeneologyRules.getRandomLastName(new Random());
			int age = MINIMUM_FOUNDER_AGE + new Random().nextInt(MAXIMUM_FOUNDER_AGE - MINIMUM_FOUNDER_AGE);
			founders.add(
					genesis.addSinglePerson(firstName, lastName, Sex.FEMALE, age, GeneticsMap.randomGenes(new Random()),
							ARole.getRandomRole(new Random(), false), IPersonality.randomPersonality(new Random())));
		}

		// genesis.addSinglePerson("Eve", "Godwoman", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Amy", "Adams", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Bella", "Burke", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Catherine", "Collins", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Dina", "Driver", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Fiona", "Foreman", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Geraldine", "Gordon", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Hailey", "Henderson", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Illiana", "Irvin", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Jane", "Jones", Sex.FEMALE, 18);
		// // More diversity
		// genesis.addSinglePerson("Kathleen", "Knott", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Lily", "Locke", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Molly", "Merritt", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Natalie", "Nelson", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Ophelia", "Oakes", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Polly", "Peterson", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Quita", "Quest", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Rhonda", "Richardson", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Sally", "Saylor", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Tanya", "Thomas", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Uma", "Udell", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Valerie", "Vendell", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Wanda", "Walters", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Xena", "Xie", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Yolanda", "Yale", Sex.FEMALE, 18);
		// genesis.addSinglePerson("Zarah", "Zimmerer", Sex.FEMALE, 18);
		//
		// genesis.addSinglePerson("Adam", "Godman", Sex.MALE, 18);
		// genesis.addSinglePerson("Bob", "Bones", Sex.MALE, 18);
		// genesis.addSinglePerson("Colin", "Carter", Sex.MALE, 18);
		// genesis.addSinglePerson("David", "Darrell", Sex.MALE, 18);
		// genesis.addSinglePerson("Fred", "Fontes", Sex.MALE, 18);
		// genesis.addSinglePerson("Gino", "Giovanni", Sex.MALE, 18);
		// genesis.addSinglePerson("Harold", "Harris", Sex.MALE, 18);
		// genesis.addSinglePerson("Ivan", "Irvine", Sex.MALE, 18);
		// genesis.addSinglePerson("Joe", "Jenkins", Sex.MALE, 18);
		// // More Diversity
		// genesis.addSinglePerson("Karl", "Kirk", Sex.MALE, 18);
		// genesis.addSinglePerson("Leigh", "Larson", Sex.MALE, 18);
		// genesis.addSinglePerson("Marvin", "Morrison", Sex.MALE, 18);
		// genesis.addSinglePerson("Neil", "Neace", Sex.MALE, 18);
		// genesis.addSinglePerson("Oliver", "O'Bryan", Sex.MALE, 18);
		// genesis.addSinglePerson("Peter", "Polk", Sex.MALE, 18);
		// genesis.addSinglePerson("Quinten", "Qua", Sex.MALE, 18);
		// genesis.addSinglePerson("Richard", "Rockwell", Sex.MALE, 18);
		// genesis.addSinglePerson("Samuel", "Sole", Sex.MALE, 18);
		// genesis.addSinglePerson("Theodore", "Tucker", Sex.MALE, 18);
		// genesis.addSinglePerson("Ulysses", "Ubel", Sex.MALE, 18);
		// genesis.addSinglePerson("Victor", "Varaldi", Sex.MALE, 18);
		// genesis.addSinglePerson("Winston", "Walker", Sex.MALE, 18);
		// genesis.addSinglePerson("Xavier", "Xenos", Sex.MALE, 18);
		// genesis.addSinglePerson("Yahir", "Yorrick", Sex.MALE, 18);
		// genesis.addSinglePerson("Zachary", "Zuckerberg", Sex.MALE, 18);


        if(args.length == 0) args = new String[] {"years", "100", "output"};

		int target = Integer.parseInt(args[1]);
		StopCondition condition;
		String mode = args[0];
		String prefix = args[2];
		if (mode.equalsIgnoreCase("population")) {
			condition = StopCondition.POPULATION;
		} else if (mode.equalsIgnoreCase("years")) {
			condition = StopCondition.YEARS;
		} else if (mode.equalsIgnoreCase("generation")) {
			condition = StopCondition.GENERATION;
		} else {
			System.out.printf("Invalid conditions: %s\n", Arrays.toString(args));
			return;
		}

		int i = 0;
		boolean loop = true;
		while (loop) {
			genesis.incrementTime(new Random());
			System.out.println("Year: " + genesis.getYear());
			System.out.println("Historical Population: " + genesis.historicalPopulationCount());
			System.out.println("Living Population: " + genesis.livingPopulationCount());
			System.out.println("Current Generation: " + genesis.maxGeneration());
			System.out.println("----------------");
			switch (condition) {
			case POPULATION:
				loop = genesis.historicalPopulationCount() < target;
				break;
			case YEARS:
				loop = i < target;
				break;
			case GENERATION:
				loop = genesis.maxGeneration() < target;
				break;
			default:
				break;
			}
			if (genesis.livingPopulationCount() == 0) {
				break;
			}
			i++;
		}

		System.out.println("Now printing output.");
		outputFamilyScript(prefix, genesis);
		System.out.println("Family script outputted");
		outputLifeEvents(prefix, genesis);
		System.out.println("Life events outputted.");
		outputPersonStats(prefix, genesis);
		System.out.println("Person stats outputted");
		outputFounderInfo(prefix, founders);
		System.out.println("Founder stats outputted");
		outputRelationshipInfo(prefix, genesis);
		System.out.println("Relationship info stats outputted");
		outputStats(prefix, genesis);
		System.out.println("Relationship stats outputted");
		outputCareerStats(prefix, genesis);
		System.out.println("Career stats outputted");
		System.out.println("All files outputted!");
		

	}

	private static void outputStats(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-statsInfo.csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("Relationship Type, Mean Attraction, Median Attraction, "
				+ "Mean Compatibility, Median Compatibility, Mean Regard, "
				+ "Median Regard, Mean Desire, Median Desire, Count\n");
		HashMap<RelationshipType, List<IRelationship>> map = new HashMap<RelationshipType, List<IRelationship>>();
		for (RelationshipType rt : RelationshipType.values()) {
			map.put(rt, new ArrayList<IRelationship>());
		}
		for (IPerson p : genesis.historicalPopulation()) {
			for (IRelationship r : p.getRelationships().values()) {
				map.get(r.getType()).add(r);
			}
		}
		for (RelationshipType rt : RelationshipType.values()) {
			List<IRelationship> rs = map.get(rt);
			List<Double> attractions = new ArrayList<Double>();
			List<Double> compatibilities = new ArrayList<Double>();
			List<Double> regards = new ArrayList<Double>();
			List<Double> desires = new ArrayList<Double>();
			for (IRelationship r : rs) {
				IPerson p1 = r.p1();
				IPerson p2 = r.p2();
				if (!p1.isSingle()) {
					p1.makeWidow(0);
					p1.stopMourning();
				}
				if (!p2.isSingle()) {
					p2.makeWidow(0);
					p2.stopMourning();
				}
				attractions.add(GeneologyRules.marriageChance(p1, p2));
				compatibilities.add(GeneologyRules.computeRegard(p1, p2));
				regards.add(r.regard());
				desires.add(r.romanticDesire());
			}
			fileWriter.write(rt.toString());
			fileWriter.write(",");
			// Mean Attraction
			fileWriter.write(Double.toString(mean(attractions)));
			fileWriter.write(",");
			// Median Attraction
			fileWriter.write(Double.toString(median(attractions)));
			fileWriter.write(",");
			// Mean Compatibility
			fileWriter.write(Double.toString(mean(compatibilities)));
			fileWriter.write(",");
			// Median Compatibility
			fileWriter.write(Double.toString(median(compatibilities)));
			fileWriter.write(",");
			// Mean Regard
			fileWriter.write(Double.toString(mean(regards)));
			fileWriter.write(",");
			// Median Regard
			fileWriter.write(Double.toString(median(regards)));
			fileWriter.write(",");
			// Mean Desire
			fileWriter.write(Double.toString(mean(desires)));
			fileWriter.write(",");
			// Median Desire
			fileWriter.write(Double.toString(median(desires)));
			fileWriter.write(",");
			// Number of Relationships of This Type
			fileWriter.write(Integer.toString(rs.size()));
			fileWriter.write("\n");
		}
		fileWriter.close();

	}

	private static double median(List<Double> lst) {
		if (lst.isEmpty()) {
			return Integer.MIN_VALUE;
		}
		List<Double> cloneLst = new ArrayList<Double>();
		cloneLst.addAll(lst);
		Collections.sort(cloneLst);
		return cloneLst.get(lst.size() / 2);

	}

	private static double mean(List<Double> lst) {
		double sum = sum(lst);
		return sum / lst.size();
	}

	private static double sum(List<Double> lst) {
		double sum = 0;
		for (Number n : lst) {
			sum += n.doubleValue();
		}
		return sum;
	}

	private static void outputRelationshipInfo(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-relationshipInfo.csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("Person1,Person2,Regard,Desire,StartYear,RelationshipType,Attraction,Compatability\n");
		List<IPerson> historicalPopulation = genesis.historicalPopulation();
		int i = 0;
		for (IPerson p : historicalPopulation) {
			Map<IPerson, IRelationship> relationships = p.getRelationships();
			for (Entry<IPerson, IRelationship> pair : relationships.entrySet()) {
				IPerson other = pair.getKey();
				IRelationship r = pair.getValue();
				// Person 1
				fileWriter.write(p.getId() + ",");
				// Person 2
				fileWriter.write(other.getId() + ",");
				// regard
				fileWriter.write(r.regard() + ",");
				// desire
				fileWriter.write(r.romanticDesire() + ",");
				// Start Year
				fileWriter.write(r.getAnniversaryYear() + ",");
				// Relationship Type
				fileWriter.write(r.getType() + ",");
				// Attraction for Statistical Analysis
				fileWriter.write(Double.toString(GeneologyRules.computeOverallAttraction(p, other, 1)) + ",");
				// Compatibility for Statistical Analysis
				fileWriter.write(Double.toString(GeneologyRules.computeRegard(p, other)));
				fileWriter.write("\n");
			}
			i++;
		}
		fileWriter.close();

	}

	private static void outputFounderInfo(String prefix, List<IPerson> founders) throws IOException {
		File file = new File("output/" + prefix + "-founderInfo.csv");
		FileWriter fileWriter = new FileWriter(file);
		writePersonInfoToFile(fileWriter, founders);
		fileWriter.close();
	}

	private static void outputPersonStats(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-personalInfoStats.csv");
		FileWriter fileWriter = new FileWriter(file);
		List<IPerson> population = genesis.historicalPopulation();
		writePersonInfoToFile(fileWriter, population);
		fileWriter.close();
	}

	private static void writePersonInfoToFile(FileWriter fileWriter, List<IPerson> population) throws IOException {
		fileWriter.write("ID, First Name, Last Name, Birth Last Name, Age, Birth Year, "
				+ "Spouse, Mother, Father, Generation, Living, Death Year, "
				+ "Spouse History, Number of Children, Founding Last Names, "
				+ "Number of Founding Heritages, Sex, Role,Job Type Title, Job Rank, Job Performance, Job Status");
		// Add Personality Traits Header
		for (PersonalityTrait pt : PersonalityTrait.values()) {
			fileWriter.write("," + pt);
		}
		// genes header component
		HashMap<String, TraitData> traits = JSONTraits.getTraits();
		List<String> traitsNames = new ArrayList<String>();
		traitsNames.addAll(traits.keySet());
		for (String t : traitsNames) {
			fileWriter.write(",");
			fileWriter.write(t);
		}
		fileWriter.write("\n");
		for (IPerson p : population) {
			StringBuilder sb = new StringBuilder();
			// PersonID
			sb.append(p.getId() + ",");
			// First Name
			sb.append(p.getFirstName() + ",");
			// Last Name
			sb.append(p.getCurrentLastName() + ",");
			// Birth Last Name
			sb.append(p.getBirthLastName() + ", ");
			// Age
			sb.append(p.getAge() + ", ");
			// Birth Year
			sb.append(p.getBirthYear() + ", ");
			// Spouse
			sb.append((p.getSpouse() == null ? "-1" : p.getSpouse().getId()) + ",");
			// Mother
			sb.append((p.getMother() == null ? "-1" : p.getMother().getId()) + ", ");
			// Father
			sb.append((p.getFather() == null ? "-1" : p.getFather().getId()) + ", ");
			// Generation
			sb.append(p.getGeneration() + ", ");
			// Living
			sb.append(p.isLiving() + ", ");
			// Death Year
			sb.append(p.getDeathYear() + ", ");
			// Spousal History
			List<ISpouseHistory> sh = p.getSpousalHistory();
			if (!sh.isEmpty()) {
				for (ISpouseHistory h : sh) {
					sb.append(String.format("(%s/%d/%s);", h.getSpouse().getId(), h.getAnniversaryYear(),
							h.getEndingYear() == -1 ? "Still Married" : Integer.toString(h.getEndingYear())));
				}
			} else {
				sb.append("nil");
			}
			sb.append(", ");
			// Number of children
			sb.append(String.format("%d", p.getChildren().size()));
			sb.append(", ");
			// Founding History
			Collection<String> foundingHistory = p.getFoundingLastNames();
			if (!foundingHistory.isEmpty()) {
				for (String name : foundingHistory) {
					sb.append(String.format("%s;", name));
				}
			} else {
				sb.append("nil");
			}
			sb.append(",");
			// Number of Heritages
			sb.append(foundingHistory.size());
			sb.append(",");
			// Sex
			sb.append(p.getSex());
			sb.append(",");
			// Role
			sb.append(p.getRole());
			sb.append(",");
			CareerManager cm = p.getCareer();
			Job job = cm.currentJob();
			// Job Type Title
			sb.append(job != null ? job.getJobTypeTitle() : "-");
			sb.append(",");
			// Job Rank
			sb.append(job != null ? job.getRank() : "-");
			sb.append(",");
			// Job Performance
			sb.append(job != null ? job.getPerformance() : "-");
			sb.append(",");
			// Job Status
			sb.append(job != null ? job.getStatus() : "-");

			// Personality Traits
			IPersonality personality = p.getPersonality();
			for (PersonalityTrait pt : PersonalityTrait.values()) {
				sb.append("," + personality.getTraitValue(pt));
			}
			// Genes
			GeneticsMap genes = p.getGenes();
			for (String t : traitsNames) {
				sb.append(",");
				sb.append(genes.getTraitName(t));
			}
			fileWriter.write(sb.toString() + "\n");
		}
		fileWriter.flush();
	}

	private static void outputLifeEvents(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		List<ILifeEvent> lifeEvents = genesis.lifeEvents();
		// TODO Bandaid solution to the lack for birth events:
		for (IPerson p : genesis.historicalPopulation()) {
			lifeEvents.add(new BirthLifeEvent(p));
		}
		lifeEvents.sort((e1, e2) -> e1.getLifeEventDate().compareTo(e2.getLifeEventDate()));
		File file = new File("output/" + prefix + "-lifeEvents.csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("Date\t Type\t Title\t Description\n");
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		for (ILifeEvent le : lifeEvents) {
			// Year
			fileWriter.write(df.format(le.getLifeEventDate()) + "\t");
			// Type
			fileWriter.write(le.getLifeEventType() + "\t");
			// Title
			fileWriter.write(le.getLifeEventTitle() + "\t");
			// Description
			fileWriter.write(le.getLifeEventDescription() + "\n");
		}
		fileWriter.flush();
		fileWriter.close();
	}

	private static void outputFamilyScript(String prefix, IGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-familyTree.fs");
		FileWriter fileWriter = new FileWriter(file);
		for (IPerson p : genesis.historicalPopulation()) {
			StringBuilder sb = new StringBuilder();
			// PersonID
			sb.append("i" + p.getId() + "\t");
			// First Name
			sb.append("p" + p.getFirstName() + "\t");
			// Last Name
			sb.append("l" + p.getCurrentLastName() + "\t");
			// Birth Last Name
			sb.append("q" + p.getBirthLastName() + "\t");
			// Spouse
			sb.append((p.getSpouse() == null ? "" : "s" + p.getSpouse().getId()) + "\t");
			// Mother
			sb.append((p.getMother() == null ? "" : "m" + p.getMother().getId()) + "\t");
			// Father
			sb.append((p.getFather() == null ? "" : "f" + p.getFather().getId()) + "\t");
			// Living
			if (!p.isLiving()) {
				sb.append("z1\t");
				sb.append("d" + p.getDeathYear() + "1231" + "\t");
			}
			// Gender
			sb.append((p.getSex() == Sex.MALE ? "gm" : "gf") + "\t");
			// BirthYear
			sb.append("b" + p.getBirthYear() + "0101");

			// Anniversary
			sb.append("\n");
			List<ISpouseHistory> spousalHistory = p.getSpousalHistory();
			for (ISpouseHistory h : spousalHistory) {
				sb.append("p" + p.getId() + " " + h.getSpouse().getId() + "\te1\tm" + h.getAnniversaryYear() + "0101"
						+ "\tgm" + ((h.getEndingYear() > -1) ? "\tz" + h.getEndingYear() + "1231" : "") + "\n");
			}
			fileWriter.write(sb.toString() + "\n");

		}
		fileWriter.close();

	}

	private static void outputCsv(IGenesis genesis) throws IOException {
		File file = new File("familyTree.csv");
		FileWriter fileWriter = new FileWriter(file);
		for (IPerson p : genesis.historicalPopulation()) {
			StringBuilder sb = new StringBuilder();
			// PersonID
			sb.append(p.getId() + ",");
			// First Name
			sb.append(p.getFirstName() + ",");
			// Last Name
			sb.append(p.getCurrentLastName() + ",");
			// Age
			sb.append(p.getAge() + ", ");
			// Spouse
			sb.append((p.getSpouse() == null ? "-1" : p.getSpouse().getId()) + ",");
			// Mother
			sb.append((p.getMother() == null ? "-1" : p.getMother().getId()) + ", ");
			// Father
			sb.append((p.getFather() == null ? "-1" : p.getFather().getId()) + ", ");
			// Generation
			sb.append(p.getGeneration() + ", ");
			// Living
			sb.append(p.isLiving());
			fileWriter.write(sb.toString() + "\n");
		}
		fileWriter.close();
	}

	private static void outputCareerStats(String prefix, IGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-careerStats.csv");
		FileWriter fileWriter = new FileWriter(file);
		List<IPerson> population = genesis.historicalPopulation();
		HashMap<String, List<IPerson>> map = new HashMap<String, List<IPerson>>();
		// TODO This is such a band-aid solution
		map.put("Journalism", new ArrayList<IPerson>());
		map.put("Unemployed", new ArrayList<IPerson>());
		for (IPerson p : population) {
			Job j = p.getCareer().currentJob();
			String jobName = j == null ? "Unemployed" : j.getJobTypeTitle();
			map.get(jobName).add(p);
		}
		fileWriter.write("Occupation,Average Age, Number of Men, Number of Women, "
				+ "Average Openness, "
				+ "Average Conscientiousness, Average Extraversion,"
				+ " Average Agressableness, Average Neuroticism, "
				+ "Average Tenacity, Average Focus\n");
		for (Entry<String, List<IPerson>> e : map.entrySet()) {
			List<IPerson> employees = e.getValue();
			List<Double> ageLst = new ArrayList<Double>();
			List<Double> oLst = new ArrayList<Double>();
			List<Double> cLst = new ArrayList<Double>();
			List<Double> eLst = new ArrayList<Double>();
			List<Double> agrLst = new ArrayList<Double>();
			List<Double> nLst = new ArrayList<Double>();
			List<Double> tLst = new ArrayList<Double>();
			List<Double> fLst = new ArrayList<Double>();
			for (IPerson p : employees) {
				ageLst.add((double)p.getAge());
				IPersonality personality = p.getPersonality();
				oLst.add(personality.getTraitValue(PersonalityTrait.OPENNESS));
				cLst.add(personality.getTraitValue(PersonalityTrait.CONSCIENTIOUSNESS));
				eLst.add(personality.getTraitValue(PersonalityTrait.EXTRAVERSION));
				agrLst.add(personality.getTraitValue(PersonalityTrait.AGREEABLENESS));
				nLst.add(personality.getTraitValue(PersonalityTrait.NEUROTICISM));
				CombinationRole role = (CombinationRole) p.getRole();
				tLst.add(role.getCareerTenacity());
				fLst.add(role.getCareerFocus());
			}
			fileWriter.write(e.getKey());
			fileWriter.write(",");
			// Average age
			fileWriter.write(Double.toString(mean(ageLst)));
			fileWriter.write(",");
			// Number of Men
			fileWriter.write(Long.toString(employees.stream().
					filter(empl -> empl.getSex() == Sex.MALE).count()));
			fileWriter.write(",");
			// Number of Women
			fileWriter.write(Long.toString(employees.stream().
					filter(empl -> empl.getSex() == Sex.FEMALE).count()));
			fileWriter.write(",");
			// Average Openness
			fileWriter.write(Double.toString(mean(oLst)));
			fileWriter.write(",");
			// Average Conscientiousness
			fileWriter.write(Double.toString(mean(cLst)));
			fileWriter.write(",");
			// Average Extraversion
			fileWriter.write(Double.toString(mean(eLst)));
			fileWriter.write(",");
			// Average Agreeableness
			fileWriter.write(Double.toString(mean(agrLst)));
			fileWriter.write(",");
			// Average Neuroticism
			fileWriter.write(Double.toString(mean(nLst)));
			fileWriter.write(",");
			// Average Role Tenacity
			fileWriter.write(Double.toString(mean(tLst)));
			fileWriter.write(",");
			// Average Role Focus
			fileWriter.write(Double.toString(mean(fLst)));
			fileWriter.write("\n");
		}
		fileWriter.close();
	}

}
