package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import model.GeneologyRules;
import model.GenesisImpl;
import model.IGenesis;
import model.IPerson;
import model.IdBasedGenesisImpl;
import model.Person;
import model.Sex;

public class Driver {

	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		IGenesis genesis = new IdBasedGenesisImpl();
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

		genesis.addSinglePerson("Adam", "Godman", Sex.MALE, 18);
		genesis.addSinglePerson("Bob", "Bones", Sex.MALE, 18);
		genesis.addSinglePerson("Colin", "Carter", Sex.MALE, 18);
		genesis.addSinglePerson("David", "Darrell", Sex.MALE, 18);
		genesis.addSinglePerson("Fred", "Fontes", Sex.MALE, 18);
		genesis.addSinglePerson("Gino", "Giovanni", Sex.MALE, 18);
		genesis.addSinglePerson("Harold", "Harris", Sex.MALE, 18);
		genesis.addSinglePerson("Ivan", "Irvine", Sex.MALE, 18);
		genesis.addSinglePerson("Joe", "Jenkins", Sex.MALE, 18);
		int interval = 10;
		int i = 0;
		while (true) {
			genesis.incrementTime(new Random());
			System.out.println("Year: " + genesis.getYear());
			System.out.println(genesis.historicalPopulationCount());
			// System.out.println(genesis.livingPersonalPopulationSummary());
			System.out.println("Living Population: " + genesis.livingPopulationCount());
			System.out.println("----------------");
			if (i % interval == 0) {
				String input = s.nextLine();
				if (input.equalsIgnoreCase("END")) {
					break;
				}
			}
			i++;
		}

		System.out.println("Now printing output.");
		outputFamilyScript(genesis);
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
