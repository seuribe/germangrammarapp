package ar.com.inmune.deutsch;

import java.io.Serializable;

public abstract class Article extends Word implements Serializable, Comparable<Article> {
	private static final long serialVersionUID = 1L;
	public static final String[][] INDEFINITE_ENDINGS = {
		{"",   "e",  "",   "e"},
		{"en", "e",  "",   "e"},
		{"em", "er", "em", "en"},
		{"es", "er", "es", "er"}
	};
	public static final String[][] DIESER_ENDINGS = {
		{"er", "e",  "es", "e"},
		{"en", "e",  "es", "e"},
		{"em", "er", "em", "en"},
		{"es", "er", "es", "er"}
	};

	protected final String[][] ENDING_FORM;
	protected final String root;
	protected String name;

	protected Article( String name,String root, String[][] ENDING_FORM) {
		this.name = name;
		this.root = root;
		this.ENDING_FORM = ENDING_FORM;
	}

	public String toString() {
		return name;
	}

	public String decline(Case c, Gender gender) {
		return root + ((ENDING_FORM != null) ? ENDING_FORM[c.ordinal()][gender.ordinal()] : "");
	}
	
	@Override public int compareTo(Article another) {
		return name.compareTo(another.name);
	}

	public static class Indefinite extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Indefinite() {
			super("indefinite", "ein", INDEFINITE_ENDINGS);
		}
		@Override
		public String decline(Case c, Gender gender) {
			if (gender == Gender.Plural) {
				return "";
			}
			return super.decline(c, gender);
		}
	}

	public static class Negative extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Negative() {
			super("negative", "kein", INDEFINITE_ENDINGS);
		}
	}

	public static class Dieser extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Dieser() {
			super("dieser", "dies", DIESER_ENDINGS);
		}
	}

	public static class Jener extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Jener() {
			super("jener", "jen", DIESER_ENDINGS);
		}
	}

	public static class Definite extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String DEFINITE[][] = {
			{"der", "die", "das", "die"}, // Nom
			{"den", "die", "das", "die"}, // Akk
			{"dem", "der", "dem", "den"}, // Dat
			{"des", "der", "des", "der"} // Gen
		};
		public Definite() {
			super("definite", "", DEFINITE);
		}
	}

	public static class Demonstrative extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String DEMONSTRATIVE[][] = {
			{"der", "die", "das", "die"}, // Nom
			{"den", "die", "das", "die"}, // Akk
			{"dem", "der", "dem", "denen"}, // Dat
			{"dessen", "deren", "dessen", "derer"} // Gen
		};
		public Demonstrative() {
			super("demonstrative", "", DEMONSTRATIVE);
		}
	}
	
	public static class Possesive extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final String[] FORMS = {"mein", "dein", "sein", "ihr", "sein", "unser", "euer", "ihr", "Ihr"};

		public Possesive(Subject subject) {
			super("posesive", FORMS[subject.ordinal()], INDEFINITE_ENDINGS);
		}
		
		public String toString() {
			return this.root;
		}
	}
	
	public static class Etwas extends Article implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Etwas() {
			super("etwas", "etwas", null);
		}
	}
}
