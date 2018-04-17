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
import model.career.Job;
import model.career.occupations.AOccupation;
import model.genesis.IGenesis;
import model.genesis.idbased.ILifeEventEnabledGenesis;
import model.genesis.idbased.LifeEventEnabledGenesisImpl;
import model.genetics.GeneticsMap;
import model.genetics.JSONTraits;
import model.genetics.TraitData;
import model.goals.IGoal;
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
	public static final int STARTING_MALE_FOUNDERS = 25;
	public static final int STARTING_FEMALE_FOUNDERS = 25;
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

        if(args.length == 0) args = new String[] {"generation", "5", "output"};

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
		outputGoals(prefix, genesis);
		System.out.println("Goal Success Stats outputted");
        outputCareerStats(prefix, genesis);
        System.out.println("Career stats outputted");
		outputDiverseSampleActions(prefix, genesis);
		System.out.println("Diverse Samples Outputted");

		System.out.println("All files outputted!");

	}

	private static void outputDiverseSampleActions(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		File dir = new File("output/" + prefix + "-diverseSamples/");
		dir.mkdir();
		List<IPerson> historicalPopulation = genesis.historicalPopulation();
		historicalPopulation.removeIf(p -> p.getAge() > 50);
		Collections.shuffle(historicalPopulation);
		List<IPerson> sample = new ArrayList<IPerson>();
		for (int i = 0; i < 20; i++) {
			sample.add(historicalPopulation.get(i));
		}

		for (IPerson p : sample) {
			String personID = p.getId();
			File file = new File("output/" + prefix + "-diverseSamples/" + personID + "-lifeEvents.csv");
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("Date, Type, Title, Description\n");
			DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
			for (ILifeEvent le : p.getLifeEvents()) {
				// Year
				fileWriter.write(df.format(le.getLifeEventDate()) + ",");
				// Type
				fileWriter.write(le.getLifeEventType() + ",");
				// Title
				fileWriter.write(le.getLifeEventTitle() + ",");
				// Description
				fileWriter.write(le.getLifeEventDescription() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		}
	}

	private static void outputRelationshipInfo(String prefix, ILifeEventEnabledGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-relationshipInfo.csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("Person1,Person2,Regard,Desire,StartYear,RelationshipType,Desire\n");
		for (IPerson p : genesis.historicalPopulation()) {
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
				fileWriter.write(Double.toString(GeneologyRules.computeDesire(p, other)));
				fileWriter.write("\n");
			}
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
				+ "Number of Founding Heritages, Sex, Role");
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

	private static void outputGoals(String prefix, IGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-goalStats.csv");
		FileWriter writer = new FileWriter(file);
		List<IPerson> historicalPopulation = genesis.historicalPopulation();
		writer.write("PersonID,Age,Living,Goal Title,Goal Description,Success\n");
		for (IPerson p : historicalPopulation) {
			for (IGoal g : p.getGoalTracker().getGoals()) {
				List<String> lst = new ArrayList<String>();
				lst.add(p.getId());
				lst.add(Integer.toString(p.getAge()));
				lst.add(Boolean.toString(p.isLiving()));
				lst.add(g.getTitle());
				lst.add(g.getDescription());
				lst.add(Double.toString(g.computeProgress(genesis.getYear())));
				writer.write(String.join(",", lst));
				writer.flush();
				writer.write("\n");
				writer.flush();
			}
		}
		writer.close();
	}

	private static void outputCareerStats(String prefix, IGenesis genesis) throws IOException {
		File file = new File("output/" + prefix + "-careerStats.csv");
		FileWriter fileWriter = new FileWriter(file);
		List<IPerson> population = genesis.historicalPopulation();
		HashMap<String, List<IPerson>> map = new HashMap<String, List<IPerson>>();

		// TODO This is such a band-aid solution
        for(AOccupation oc : AOccupation.opts) {
            map.put(oc.getName(), new ArrayList<>());
        }
        map.put("Unemployed", new ArrayList<>());
        map.put("Retired", new ArrayList<>());

		for (IPerson p : population) {
			Job j = p.getCareer().currentJob();
            String jobName;
            if(j != null) {
                jobName = j.getJobTypeTitle();
            } else {
                jobName = p.getCareer().isRetired() ? "Retired" : "Unemployed";
            }
			map.get(jobName).add(p);
		}
		fileWriter.write("Occupation,Average Age, Number of Men, Number of Women, " + "Average Openness, "
				+ "Average Conscientiousness, Average Extraversion," + " Average Agressableness, Average Neuroticism, "
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
				ageLst.add((double) p.getAge());
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
			fileWriter.write(Long.toString(employees.stream().filter(empl -> empl.getSex() == Sex.MALE).count()));
			fileWriter.write(",");
			// Number of Women
			fileWriter.write(Long.toString(employees.stream().filter(empl -> empl.getSex() == Sex.FEMALE).count()));
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

}
