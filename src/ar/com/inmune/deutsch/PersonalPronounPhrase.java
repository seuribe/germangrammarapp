package ar.com.inmune.deutsch;

import java.io.Serializable;

public class PersonalPronounPhrase extends NounPhrase implements Serializable {
	private static final long serialVersionUID = 1L;
	private PersonalPronoun pp;

	public PersonalPronounPhrase(Subject subject) {
		this(new PersonalPronoun(subject));
	}

	public PersonalPronounPhrase(PersonalPronoun pp) {
		this.pp = pp;
	}

	@Override
	public WordSequence decline(Case c, Number number) {
		return new WordSequence().append(pp, pp.decline(c));
	}

	@Override
	public Subject getSubject() {
		return pp.subject();
	}

	@Override
	public WordSequence decline(Case c) {
		return decline(c, null);
	}

	@Override
	public Article article() {
		return null;
	}

	@Override
	public Gender gender() {
		return pp.subject().gender();
	}
}