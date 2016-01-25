package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.NounPhrase;
import ar.com.inmune.deutsch.PersonalPronoun;
import ar.com.inmune.deutsch.PersonalPronounPhrase;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;


public class AuxiliaryVerbConjugation extends ExerciseBuilder {

	private List<Verb> verbs;
	private List<Noun> nouns;

	public AuxiliaryVerbConjugation() {
		this.verbs = Verb.IsTransitive.include(dataProvider.getVerbs());
		this.nouns = dataProvider.getNouns();
	}

	@Override
	public Exercise getRandomExercise() {

		Verb aux = randomFrom(Verb.MODAL_AUXILIARY);
		Verb verb = getRandomVerb(
				Verb.IsTransitive.include(verbs));
		
		Subject subject = getRandomSubject();
		Noun object = getRandomNoun(
						new Noun.OrFilter(
								Noun.filtersFromTags(verb.transitiveTags)
						).include(nouns));
		
		return makeExercise(verb, aux, subject, object);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Verb verb = (Verb) options.get(TAG_VERB);
		Verb aux = (Verb) options.get(TAG_AUXILIARY_VERB);
		Subject subject = (Subject) options.get(TAG_SUBJECT);
		Noun object = (Noun) options.get(TAG_DIRECT_OBJECT);

		return makeExercise(verb, aux, subject, object);
	}

	private Exercise makeExercise(Verb verb, Verb aux, Subject subject, Noun object) {
		
//		Producer p = new Producer();
//		p.hide(aux);

		NounPhrase subj = new PersonalPronounPhrase(new PersonalPronoun(subject));
		NounPhrase dobj = new NounNounPhrase(object, new Article.Definite(), null);

		List<String> options = new ArrayList<String>();
		options = conjugateAll(aux, Subject.values(), Tense.Present);
		String correct = aux.conjugate(subj, Tense.Present);
		int index = prepareOptions(options, correct, 4);
		
//		subj.decline(p, Case.Nominative);
//		p.produce(aux, aux.conjugate(subj, Tense.Present));
//		dobj.decline(p, verb.getDirectObjectCase());
//		p.produce(verb, verb.infinitive);

		WordSequence seq = new WordSequence().hide(aux)
			.append(subj.decline(Case.Nominative))
			.append(aux, aux.conjugate(subj, Tense.Present))
			.append(dobj.decline(verb.getDirectObjectCase()))
			.append(verb, verb.infinitive);
		
		return new Exercise(seq, options, index, Exercise.Type.AuxiliaryVerbConjugation, verb);
	}

	private List<String> conjugateAll(Verb aux, Subject[] values, Tense tense) {
		List<String> ret = new ArrayList<String>();
		for (Subject subject : values) {
			ret.add(aux.conjugate(new PersonalPronounPhrase(subject), tense));
		}
		return ret;
	}

}
