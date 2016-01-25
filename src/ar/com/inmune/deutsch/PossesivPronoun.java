package ar.com.inmune.deutsch;

import java.io.Serializable;

/**
 * The Possesive, when used as a Pronoun, has the ending of dieser
 * @author seu
 *
 */
public class PossesivPronoun extends Word implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String[] FORMS = {"mein", "dein", "sein", "ihr", "sein", "unser", "euer", "ihr", "Ihr"};
	private static final String[][] PRONOUN_ENDING = {
		{"er", "e", "es", "e"},
		{"en", "e", "es", "e"},
		{"em", "er", "em", "en"},
		{"es", "er", "es", "er"},
	};

	private Subject subject;

	public PossesivPronoun(Subject subject) {
		this.subject = subject;
	}

	public String decline(Case c, Gender gender) {
		return FORMS[subject.ordinal()] + PRONOUN_ENDING[c.ordinal()][gender.ordinal()];
	}
}
