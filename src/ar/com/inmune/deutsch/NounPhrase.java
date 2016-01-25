package ar.com.inmune.deutsch;

import java.io.Serializable;

public abstract class NounPhrase implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract WordSequence decline(Case c);
	public abstract WordSequence decline(Case c, Number number);
	public abstract Subject getSubject();
	public abstract Article article();
	public abstract Gender gender();

}
