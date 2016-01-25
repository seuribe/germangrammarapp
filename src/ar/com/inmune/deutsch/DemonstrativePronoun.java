package ar.com.inmune.deutsch;

import java.io.Serializable;

public class DemonstrativePronoun extends Word implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DER_PRONOUN[][] = {
		{"der", "die", "das", "die"},
		{"den", "die", "das", "die"},
		{"dem", "der", "dem", "denen"},
		{"dessen", "deren", "dessen", "deren"}
	};
	
	public String decline(Case c, Gender gender) {
		return DER_PRONOUN[c.ordinal()][gender.ordinal()];
	}
	
}
