package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.List;

public class WeakVerb extends RegularVerb implements Serializable {

	private static final long serialVersionUID = 1L;

	public WeakVerb(String infinitive, String root, List<Noun.Tag> transitiveTags, boolean reqDative, Verb auxiliar) {
		super(infinitive, root, getPastRoot(root), transitiveTags, reqDative, auxiliar);
	}

	private static String getPastRoot(String root) {
		char lastChar = root.charAt(root.length() - 1);
		char preLastChar = root.charAt(root.length() - 2);

		if (lastChar == 'd' || lastChar == 't' || ((lastChar == 'm' || lastChar == 'n') && !isVowel(preLastChar))) {
			return root + "e";
		}
		return root;
	}

	@Override
	public String pastParticiple() {
		if (endsInIeren()) {
			return past + PAST_PARTICIPLE_POSTFIX_WEAK;
		}
		return PAST_PARTICIPLE_PREFIX + past + PAST_PARTICIPLE_POSTFIX_WEAK;
	}

}
