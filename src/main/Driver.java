package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Sex;
import model.genesis.IGenesis;
import model.genesis.idbased.ILifeEventEnabledGenesis;
import model.genesis.idbased.LifeEventEnabledGenesisImpl;
import model.lifeevents.BirthLifeEvent;
import model.lifeevents.ILifeEvent;
import model.person.IPerson;
import model.spousehistory.ISpouseHistory;

public class Driver {

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		ILifeEventEnabledGenesis genesis = new LifeEventEnabledGenesisImpl();
		genesis.addSinglePerson("Eve", "Godwoman", Sex.FEMALE, 18);
		genesis.addSinglePerson("Amy", "Adams", Sex.FEMALE, 18);
		genesis.addSinglePerson("Bella", "Burke", Sex.FEMALE, 18);
		genesis.addSinglePerson("Catherine", "Collins", Sex.FEMALE, 18);
		genesis.addSinglePerson("Dina", "Driver", Sex.FEMALE, 18);
		genesis.addSinglePerson("Fiona", "Foreman", Sex.FEMALE, 18);
		genesis.addSinglePerson("Geraldine", "Gordon", Sex.FEMALE, 18);
		genesis.addSinglePerson("Hailey", "Henderson", Sex.FEMALE, 18);
		genesis.addSinglePerson("Illiana", "Irvin", Sex.FEMALE, 18);
		genesis.addSinglePerson("Jane", "Jones", Sex.FEMALE, 18);
		// More diversity
		genesis.addSinglePerson("Kathleen", "Knott", Sex.FEMALE, 18);
		genesis.addSinglePerson("Lily", "Locke", Sex.FEMALE, 18);
		genesis.addSinglePerson("Molly", "Merritt", Sex.FEMALE, 18);
		genesis.addSinglePerson("Natalie", "Nelson", Sex.FEMALE, 18);
		genesis.addSinglePerson("Ophelia", "Oakes", Sex.FEMALE, 18);
		genesis.addSinglePerson("Polly", "Peterson", Sex.FEMALE, 18);
		genesis.addSinglePerson("Quita", "Quest", Sex.FEMALE, 18);
		genesis.addSinglePerson("Rhonda", "Richardson", Sex.FEMALE, 18);
		genesis.addSinglePerson("Sally", "Saylor", Sex.FEMALE, 18);
		genesis.addSinglePerson("Tanya", "Thomas", Sex.FEMALE, 18);
		genesis.addSinglePerson("Uma", "Udell", Sex.FEMALE, 18);
		genesis.addSinglePerson("Valerie", "Vendell", Sex.FEMALE, 18);
		genesis.addSinglePerson("Wanda", "Walters", Sex.FEMALE, 18);
		genesis.addSinglePerson("Xena", "Xie", Sex.FEMALE, 18);
		genesis.addSinglePerson("Yolanda", "Yale", Sex.FEMALE, 18);
		genesis.addSinglePerson("Zarah", "Zimmerer", Sex.FEMALE, 18);

		genesis.addSinglePerson("Adam", "Godman", Sex.MALE, 18);
		genesis.addSinglePerson("Bob", "Bones", Sex.MALE, 18);
		genesis.addSinglePerson("Colin", "Carter", Sex.MALE, 18);
		genesis.addSinglePerson("David", "Darrell", Sex.MALE, 18);
		genesis.addSinglePerson("Fred", "Fontes", Sex.MALE, 18);
		genesis.addSinglePerson("Gino", "Giovanni", Sex.MALE, 18);
		genesis.addSinglePerson("Harold", "Harris", Sex.MALE, 18);
		genesis.addSinglePerson("Ivan", "Irvine", Sex.MALE, 18);
		genesis.addSinglePerson("Joe", "Jenkins", Sex.MALE, 18);
		// More Diversity
		genesis.addSinglePerson("Karl", "Kirk", Sex.MALE, 18);
		genesis.addSinglePerson("Leigh", "Larson", Sex.MALE, 18);
		genesis.addSinglePerson("Marvin", "Morrison", Sex.MALE, 18);
		genesis.addSinglePerson("Neil", "Neace", Sex.MALE, 18);
		genesis.addSinglePerson("Oliver", "O'Bryan", Sex.MALE, 18);
		genesis.addSinglePerson("Peter", "Polk", Sex.MALE, 18);
		genesis.addSinglePerson("Quinten", "Qua", Sex.MALE, 18);
		genesis.addSinglePerson("Richard", "Rockwell", Sex.MALE, 18);
		genesis.addSinglePerson("Samuel", "Sole", Sex.MALE, 18);
		genesis.addSinglePerson("Theodore", "Tucker", Sex.MALE, 18);
		genesis.addSinglePerson("Ulysses", "Ubel", Sex.MALE, 18);
		genesis.addSinglePerson("Victor", "Varaldi", Sex.MALE, 18);
		genesis.addSinglePerson("Winston", "Walker", Sex.MALE, 18);
		genesis.addSinglePerson("Xavier", "Xenos", Sex.MALE, 18);
		genesis.addSinglePerson("Yahir", "Yorrick", Sex.MALE, 18);
		genesis.addSinglePerson("Zachary", "Zuckerberg", Sex.MALE, 18);
		int target = Integer.parseInt(args[1]);
		StopCondition condition;
		String mode = args[0];
		if (mode.equalsIgnoreCase("population")) {
			condition = StopCondition.POPULATION;
		} else if (mode.equalsIgnoreCase("years")) {
			condition = StopCondition.YEARS;
		} else {
			System.out.printf("Invalid conditions: %s %s\n", args[0], args[1]);
			return;
		}
		int i = 0;
		boolean loop = true;
		while (loop) {
			genesis.incrementTime(new Random());
			System.out.println("Year: " + genesis.getYear());
			System.out.println(genesis.historicalPopulationCount());
			System.out.println("Living Population: " + genesis.livingPopulationCount());
			System.out.println("----------------");
			switch (condition) {
			case POPULATION:
				loop = genesis.historicalPopulationCount() < target;
				break;
			case YEARS:
				loop = genesis.getYear() < target;
				break;
			}
			i++;
		}

		System.out.println("Now printing output.");
		outputFamilyScript(genesis);
		System.out.println("Family script outputted");
		outputLifeEvents(genesis);
		System.out.println("Life events outputted.");
		outputPersonStats(genesis);
		System.out.println("Person stats outputted");

	}

	private static void outputPersonStats(ILifeEventEnabledGenesis genesis) throws IOException {
		File file = new File("personalInfoStats.csv");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(
				"ID, First Name, Last Name, Birth Last Name, Age, Birth Year, "
				+ "Spouse, Mother, Father, Generation, Living, Death Year, "
				+ "Spouse History, Number of Children\n");
		for (IPerson p : genesis.historicalPopulation()) {
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

			fileWriter.write(sb.toString() + "\n");
		}
		fileWriter.close();
	}

	private static void outputLifeEvents(ILifeEventEnabledGenesis genesis) throws IOException {
		List<ILifeEvent> lifeEvents = genesis.lifeEvents();
		// TODO Bandaid solution to the lack for birth events:
		for (IPerson p : genesis.historicalPopulation()) {
			lifeEvents.add(new BirthLifeEvent(p));
		}
		lifeEvents.sort((e1, e2) -> e1.getLifeEventDate().compareTo(e2.getLifeEventDate()));
		File file = new File("lifeEvents.csv");
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

	private static void outputFamilyScript(IGenesis genesis) throws IOException {
		File file = new File("familyTree.fs");
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

}
