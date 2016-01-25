package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.List;

public abstract class Verb extends Word implements Serializable, Comparable<Verb> {

	protected static final String PAST_POSTFIXES[] = {"te", "test", "te", "te", "te", "ten", "tet", "ten", "ten"};
	protected static final String PAST_POSTFIXES_SEIN[] = {"", "st", "", "", "", "en", "t", "en", "en"};
	protected static final String PAST_POSTFIXES_HABEN_WERDEN[] = {"", "st", "", "", "", "n", "t", "n", "n"};

	private static final long serialVersionUID = 1L;
	public final List<Noun.Tag> transitiveTags;
	public final boolean reqDative;
	public final Verb auxiliar;
	public final String infinitive;

	public List<PrepositionalObject> pos;

	public static class PrepositionalObject {
		public Preposition prep;
		public List<Noun.Tag> acceptedNouns;

		public PrepositionalObject(Preposition prep, List<Noun.Tag> acceptedNouns) {
			this.prep = prep;
			this.acceptedNouns = acceptedNouns;
		}
	}
	
	public static final class SeinHabenWerdenVerb extends Verb implements Serializable {
	
		private String[] present;
		private String past;
		private String pastParticiple;
		private String pastPostfixes[];
		
		private static final long serialVersionUID = 1L;
		
		public SeinHabenWerdenVerb(String infinitive, String[] present, String past, String pastParticiple, String pastPostfixes[]) {
			super(infinitive);
			this.present = present;
			this.past = past;
			this.pastParticiple = pastParticiple;
			this.pastPostfixes = pastPostfixes;
		}
	
		@Override
		protected String present(Subject subject) {
			return present[subject.ordinal()];
		}
	
		@Override
		protected String past(Subject subject) {
			return past + pastPostfixes[subject.ordinal()];
		}
	
		@Override
		public String pastParticiple() {
			return pastParticiple;
		}
	
		@Override
		public String presentParticiple() {
			return null;
		}
		
		public String fakeWeakParticiple() {
			return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_WEAK;
		}
		public String fakeStrongParticiple() {
			return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_STRONG;
		}

	}
	
	public static final class ModalAuxiliaryVerb extends Verb implements Serializable {

		private static final long serialVersionUID = 1L;
		private String rootSingular;
		private String rootPlural;
		private String past;
		private String pastParticiple;

		private static final String[] PRESENT_POSTFIXES = {"", "st", "", "", "", "en", "t", "en", "en"};
		public ModalAuxiliaryVerb(String infinitive, String rootSingular, String rootPlural, String past) {
			super(infinitive);
			this.rootSingular = rootSingular;
			this.rootPlural = rootPlural;
			this.past = past;
			this.pastParticiple = "ge" + past + "t";
		}
		
		@Override
		protected String present(Subject subject) {		String root = (subject.number() == Number.Singular ? rootSingular : rootPlural); 
			if (root.endsWith("s") && subject == Subject.SecondSingular) {
				return root + "t";
			}
			return root + PRESENT_POSTFIXES[subject.ordinal()];
		}

		@Override
		protected String past(Subject subject) {
			return past + PAST_POSTFIXES[subject.ordinal()];
		}

		@Override
		public String pastParticiple() {
			return pastParticiple;
		}

		@Override
		public String presentParticiple() {
			return null;
		}

		public String fakeWeakParticiple() {
			return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_WEAK;
		}
		public String fakeStrongParticiple() {
			return RegularVerb.PAST_PARTICIPLE_PREFIX + past + WeakVerb.PAST_PARTICIPLE_POSTFIX_STRONG;
		}
	}


	public static final Verb sein = new SeinHabenWerdenVerb("sein",
			new String[]{"bin", "bist", "ist", "ist", "ist", "sind", "seid", "sind", "sind"}, "war", "gewesen",
			PAST_POSTFIXES_SEIN);
	public static final Verb haben = new SeinHabenWerdenVerb("haben",
			new String[]{"habe", "hast", "hat","hat", "hat",  "haben", "habt", "haben", "haben"}, "hatte", "gehabt",
			PAST_POSTFIXES_HABEN_WERDEN);
	public static final Verb werden = new SeinHabenWerdenVerb("werden",
			new String[]{"werde", "wirst", "wird", "wird", "wird", "werden", "werdet", "werden", "werden"}, "wurde", "geworden",
			PAST_POSTFIXES_HABEN_WERDEN);

	public static final Verb dürfen = new ModalAuxiliaryVerb("dürfen", "darf", "dürf", "durf");
	public static final Verb können = new ModalAuxiliaryVerb("können", "kann", "könn", "konn");
	public static final Verb mögen = new ModalAuxiliaryVerb("mögen", "mag", "mög", "moch");
	public static final Verb müssen = new ModalAuxiliaryVerb("müssen", "muss", "müss", "muss");
	public static final Verb sollen = new ModalAuxiliaryVerb("sollen", "soll", "soll", "soll");
	public static final Verb wollen = new ModalAuxiliaryVerb("wollen", "will", "woll", "woll");

	public static final Verb[] MODAL_AUXILIARY = {dürfen, können, mögen, müssen, sollen, wollen};

	public static final Filter<Verb> IsTransitive = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return verb.transitiveTags != null;
		}
	};
	public static final Filter<Verb> ReqDative = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return verb.reqDative;
		}
	};
	public static final Filter<Verb> IsWeak = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return (verb instanceof WeakVerb);
		}
	};
	public static final Filter<Verb> IsStrong = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return (verb instanceof StrongVerb);
		}
	};

	public static final Filter<Verb> IsRegular = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return (verb instanceof RegularVerb);
		}
	};
	
	public static class PrepositionaObjectFilter extends Filter<Verb> {
		private Case cs;
		public PrepositionaObjectFilter(Case cs) {
			this.cs = cs;
		}
		public boolean include(Verb verb) {
			if (verb.pos == null) {
				return false;
			}
			for (PrepositionalObject po : verb.pos) {
				if (po.prep.governingCase == cs) {
					return true;
				}
			}
			return false;
		}
	}

	public static final PrepositionaObjectFilter HasNominativePO = new PrepositionaObjectFilter(Case.Nominative);
	public static final PrepositionaObjectFilter HasAcusativePO = new PrepositionaObjectFilter(Case.Acusative);
	public static final PrepositionaObjectFilter HasDativePO = new PrepositionaObjectFilter(Case.Dative);
	public static final PrepositionaObjectFilter HasGenitivePO = new PrepositionaObjectFilter(Case.Genitive);

	public static final Filter<Verb> HasPropositionalObjects = new Filter<Verb>() {
		@Override
		public boolean include(Verb verb) {
			return (verb.pos != null);
		}
	};
	public Verb(String infinitive) {
		this(infinitive, null, false, Verb.haben);
	}

	public Verb(String infinitive, List<Noun.Tag> transitiveTags, boolean reqDative, Verb auxiliar) {
		this.infinitive = infinitive;
		this.transitiveTags = transitiveTags;
		this.reqDative = reqDative;
		this.auxiliar = auxiliar;
	}

	protected abstract String present(Subject subject);
	protected abstract String past(Subject subject);
	public abstract String pastParticiple();
	public abstract String presentParticiple();
	
	// TODO: centralize here the combination rules for root + postfix for avoiding things like "tt" or "stst" -- Seu
	public String combine(String root, String postfix) {
		return null;
	}

	public String conjugate(NounPhrase np, Tense tense) {
		return conjugate(np.getSubject(), tense);
	}
	public String conjugate(Subject subject, Tense tense) {
		switch (tense) {
			case Present:
				return present(subject);
			case Past:
				return past(subject);
			case PastPerfect:
				return pastParticiple();
			case PastPluperfect:
				return pastParticiple();
			case Future:
				return infinitive;
		}
		return null;
	}

	public String toString() {
		return infinitive;
	}

	public boolean transitive() {
		return transitiveTags != null;
	}

	public abstract String fakeWeakParticiple();
	public abstract String fakeStrongParticiple();

	public Case getDirectObjectCase() {
		return (this == Verb.sein) ? Case.Nominative :
				(reqDative ? Case.Dative : Case.Acusative);
	}

	@Override
	public int compareTo(Verb other) {
		return infinitive.compareTo(other.infinitive);
	}

}
