package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.List;

public abstract class RegularVerb extends Verb implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String root;
	protected String past;
	protected final char lastChar, preLastChar;

	public static final String PAST_PARTICIPLE_POSTFIX_WEAK = "t"; // if ends with 't', 'd', or 'm', 'n' after consonant, add 'e';
	public static final String PAST_PARTICIPLE_POSTFIX_STRONG = "en";
	protected static final String PAST_PARTICIPLE_PREFIX = "ge";

	protected static final String PRESENT_POSTFIXES[] = {"e", "st", "t", "t", "t", "en", "t", "en", "en"};

	public RegularVerb(String infinitive, String root, String past, List<Noun.Tag> transitiveTags, boolean reqDative, Verb auxiliar) {
		super(infinitive, transitiveTags, reqDative, auxiliar);
		this.root = root;
		this.past = past;
		
		this.lastChar = root.charAt(root.length() - 1);
		this.preLastChar = root.charAt(root.length() - 2);
	}
	
	@Override
	protected String present(Subject subject) {
		if (rootEndsInS() && subject == Subject.SecondSingular) {
			return root + "t";
		}
		if (rootEndsInElEr() && subject.useInfinitiveInPresent() && !endsInIeren()) {
			return root + "n";
		}
		if (rootEndsInT() &&  PRESENT_POSTFIXES[subject.ordinal()].equals("t")) {
			return root + "et";
		}
		
		return root + PRESENT_POSTFIXES[subject.ordinal()];
	}

	@Override
	protected String past(Subject subject) {
		return past + PAST_POSTFIXES[subject.ordinal()];
	}

	@Override
	public String presentParticiple() {
		return null;
	}
	
	protected static final boolean isVowel(char c) {
		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == '�' || c == '�' || c == '�';
	}

	protected boolean rootEndsInS() {
		return lastChar == 's' || lastChar == '�' || lastChar == 'x' || lastChar == 'z';
	}

	protected boolean rootEndsInT() {
		return lastChar == 't';
	}
	 
	protected boolean rootEndsInElEr() {
		return (preLastChar == 'e') && (lastChar == 'r' || lastChar == 'l');
	}

	protected boolean endsInIeren() {
		return infinitive.lastIndexOf("ieren") == infinitive.length() - "ieren".length();
	}
	public String fakeWeakParticiple() {
		return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_WEAK;
	}
	public String fakeStrongParticiple() {
		return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_STRONG;
	}
	
}
