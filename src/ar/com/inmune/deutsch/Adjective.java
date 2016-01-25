package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Adjective extends Word implements Serializable {
	private static final long serialVersionUID = 1L;

	public final String root;

	private static List<String> weakArticles = new ArrayList<String>();
	private static String STRONG_POSTFIX[][] = new String[][]{
		{"er", "e",  "es", "e"},
		{"en", "e",  "es", "e"},
		{"em", "er", "em", "en"},
		{"en", "er", "en", "er"}
		};

	private static String WEAK_POSTFIX[][] = new String[][]{
		{"e",  "e",  "e",  "en"},
		{"en", "e",  "e",  "en"},
		{"en", "en", "en", "en"},
		{"en", "en", "en", "en"},
		};

	static {
		weakArticles.add("ein");
		weakArticles.add("kein");
		weakArticles.add("unser");
		weakArticles.add("mein");
		weakArticles.add("viel");
		weakArticles.add("ein paar");
		weakArticles.add("manch");
		weakArticles.add("welch");
	}
	
	public Adjective(String root) {
		this.root = root;
	}
	
	private String strongDecline(Case c, Gender gender) {
		return root + STRONG_POSTFIX[c.ordinal()][gender.ordinal()];
	}
	
	private String weakDecline(Case c, Gender gender) {
		return root + WEAK_POSTFIX[c.ordinal()][gender.ordinal()];
	}
	
	public String decline(Case c, Gender gender, String article) {
		if (article == null || isWeakArticle(article)) {	// Strong
			return strongDecline(c, gender);
		}
		
		return weakDecline(c, gender);
	}

	private boolean isWeakArticle(String article) {
		return weakArticles.contains(article);
	}

	public String toString() {
		return root;
	}
}
