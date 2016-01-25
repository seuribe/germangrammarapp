package ar.com.inmune.deutsch;

import java.util.Random;

public enum Subject {
	FirstSingular(1, Number.Singular), // ich
	SecondSingular(2, Number.Singular), // du
//	ThirdSingular(3, Number.Singular), // er/sie/es

	ThirdSingularMasculine(3, Number.Singular, Gender.Masculine),
	ThirdSingularFeminine(3, Number.Singular, Gender.Feminine),
	ThirdSingularNeuter(3, Number.Singular, Gender.Neuter),
	
	FirstPlural(1, Number.Plural), // wir
	SecondPlural(2, Number.Plural),  // ihr
	ThirdPlural(3, Number.Plural),  // sie

	Formal(1, Number.Formal); // Sie

	private int person;
	private Number number;
	private Gender gender;
	
	private static Random random = new Random();

	private Subject(int person, Number number) {
		this(person, number, null);
	}

	private Subject(int person, Number number, Gender gender) {
		this.person = person;
		this.number = number;
		this.gender = gender;
	}
	public boolean useInfinitiveInPresent() {
		switch (this) {
			case FirstPlural:
			case ThirdPlural:
			case Formal:
				return true;
			default:
				return false;
		}
	}
	
	public Number number() {
		return number;
	}
	
	public Gender gender() {
		return gender;
	}
	
	public int person() {
		return person;
	}
	
	public static Subject random() {
		return values()[random.nextInt(Subject.values().length)];
	}

	public static Subject get(int person, Number number, Gender gender) {
		for (Subject s : values()) {
			if (s.person == person && s.number == number && s.gender == gender) {
				return s;
			}
		}
		return null;
	}
	
}