package ar.com.inmune.deutsch.exercise;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.GermanPractice;
import ar.com.inmune.deutsch.Word;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.builder.*;


import ar.com.inmune.R;

public class Exercise implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static enum Type implements Serializable {
		NounGender(R.string.ex_noun_gender, R.string.expl_noun_gender,
				NounGender.class),
		NounPlural(R.string.ex_noun_plural,R.string.expl_noun_plural,
				NounPlural.class),
		AdjectiveDeclension(R.string.ex_adjective_declension,R.string.expl_adjective_declension,
				AdjectiveDeclension.class),
		VerbConjugationPresent(R.string.ex_verb_conjugation_present,R.string.expl_verb_conjugation_present,
				VerbConjugationPresent.class),
		VerbConjugationPastPerfect(R.string.ex_verb_conjugation_pastperfect,R.string.expl_verb_conjugation_pastperfect,
				VerbConjugationPastPerfect.class),
		SeinHabenConjugation(R.string.ex_verb_conjugation_seinhaben,R.string.expl_verb_conjugation_seinhaben,
				SeinHabenConjugation.class),
		PrepositionInUse(R.string.ex_preposition_in,R.string.expl_preposition_in,
				PrepositionInUse.class),
		PossesiveDeterminer(R.string.ex_possesive_determiner,R.string.expl_possesive_determiner,
				PossesiveDeterminers.class),
		IndefiniteArticle(R.string.ex_indefinite_article,R.string.expl_indefinite_article,
				IndefiniteArticle.class),
		AuxiliaryVerbConjugation(R.string.ex_auxiliary_verbs,R.string.expl_auxiliary_verbs,
				AuxiliaryVerbConjugation.class),
		;

		private int stringResource;
		private int helpResource;
		private Class<ExerciseBuilder> builderClass;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Type(int stringResource, int helpResource, Class builderClass) {
			this.stringResource = stringResource;
			this.helpResource = helpResource;
			this.builderClass = builderClass;
		}
		
		public String toString() {
			return GermanPractice.germanPractice.getString(stringResource);
		}
		
		public String help() {
			return GermanPractice.germanPractice.getString(helpResource);
		}

		public ExerciseBuilder getBuilder() {
			try {
				return (ExerciseBuilder) builderClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}
	};
	
	public final WordSequence seq; 
	public final Word help;
	public final List<String> options;
	public final int correct;
	public final Type type;
	public final Map<String, String> extra;
	
	public Exercise(WordSequence seq, List<String> options, int correct, Type type, Word help) {
		this(seq, options, correct, type, help, new HashMap<String, String>());
	}

	public Exercise(WordSequence seq, List<String> options, int correct, Type type, Word help, Map<String, String> extra) {
		this.seq = seq;
		this.help = help;
		this.options = options;
		this.correct = correct;
		this.type = type;
		this.extra = extra;
	}

	public String getQuestion() {
		return seq.toHiddenString();
	}

	public String getSentence() {
		return seq.toString();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(" - ");
		sb.append(seq.toString());
		sb.append("? ");
		for (String opt : options) {
			sb.append(opt);
			sb.append(",");
		}
		sb.append("[");
		sb.append(options.get(correct));
		sb.append("]");

		for (String key : extra.keySet()) {
			sb.append("{").append(key).append(":").append(extra.get(key)).append("}");
		}
		
		return sb.toString();
	}
	
	public String correctStr() {
		return options.get(correct);
	}
}
