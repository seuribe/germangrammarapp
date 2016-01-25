package ar.com.inmune.deutsch;

import java.io.Serializable;

public class PersonalPronoun extends Word implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String[][] PRONOUN = {
		{"ich", "du", "er", "sie (f)", "es", "wir", "ihr", "sie (pl)", "Sie"},
		{"mich", "dich", "ihn", "sie (f)", "es", "uns", "euch", "sie (pl)", "Sie"},
		{"mir", "dir", "ihm (m)", "ihr", "ihm (n)", "uns", "euch", "ihnen", "Ihnen"},
		{"meiner", "deiner", "seiner (m)", "ihrer", "seiner (n)", "unser", "euer", "ihrer", "Ihrer"}};

	private final Subject subject;
	
	public PersonalPronoun(Subject subject) {
		this.subject = subject;
	}
	
	public String decline(Case c) {
		return PRONOUN[c.ordinal()][subject.ordinal()];
	}
	
	public Subject subject() {
		return subject;
	}

	public String toString() {
		return "Personal Pronoun " + subject.toString();
	}
}
