package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.List;

public class StrongVerb extends RegularVerb implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pastParticiple;
	private String root2;

	public StrongVerb(String infinitive, String root, String past, String pastParticiple, List<Noun.Tag> transitiveTags, boolean reqDative, Verb auxiliar) {
		super(infinitive, root, past, transitiveTags, reqDative, auxiliar);
		this.past = past;
		this.pastParticiple = pastParticiple;
	}

	public StrongVerb(String infinitive, String root, String root2, String past, String pastParticiple, List<Noun.Tag> transitiveTags, boolean reqDative, Verb auxiliar) {
		super(infinitive, root, past, transitiveTags, reqDative, auxiliar);
		this.past = past;
		this.root2 = root2;
		this.pastParticiple = pastParticiple;
	}

	@Override
	public String pastParticiple() {
		return pastParticiple;
	}

	@Override
	protected String past(Subject subject) {
		return past + PAST_POSTFIXES[subject.ordinal()];
	}
	
	@Override
	protected String present(Subject subject) {
		if (root2 != null &&
				subject.number() == Number.Singular && (subject.person() == 2 || subject.person() == 3)) {
			// TODO: revisar si no hay excepciones a esto -- Seu
			return root2 + PRESENT_POSTFIXES[subject.ordinal()];
		}
		return super.present(subject);
	}

}
