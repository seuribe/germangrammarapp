package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.NounPhrase;
import ar.com.inmune.deutsch.PersonalPronoun;
import ar.com.inmune.deutsch.PersonalPronounPhrase;
import ar.com.inmune.deutsch.RegularVerb;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.Exercise.Type;


public class VerbConjugationPastPerfect extends ExerciseBuilder {

	private List<Verb> verbs;
	private List<Noun> objects;
	private static final Tense tense = Tense.PastPerfect;

	public VerbConjugationPastPerfect() {
		this.verbs = dataProvider.getVerbs();
		this.objects = Noun.Tag.IsObject.filter(dataProvider.getNouns());
	}
	
	@Override
	public Exercise getRandomExercise() {
		Verb verb = getRandomVerb(verbs);
		Subject subject = getRandomSubject();
		return makeExercise(verb, subject);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Verb verb = (Verb)options.get(TAG_VERB);
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		return makeExercise(verb, subject);
	}
	
	private Exercise makeExercise(Verb verb, Subject subject) {
		NounPhrase subjectPhrase = new PersonalPronounPhrase(new PersonalPronoun(subject));
		
		WordSequence indObjSeq = null;
		if (verb.reqDative) {
			NounPhrase indObjectPhrase = new NounNounPhrase(getRandomNoun(objects), getRandomArticle(), null);
			indObjSeq = indObjectPhrase.decline(Case.Dative);
		}

		WordSequence dirObj = null;
		if (verb.transitive()) {
			NounPhrase objectPhrase = new NounNounPhrase(getRandomNoun(objects), new Article.Definite(), null);
			dirObj = objectPhrase.decline(Case.Acusative);
		}

		WordSequence seq = new WordSequence()
			.hide(verb)
			.hide(verb.auxiliar)
			.append(subjectPhrase.decline(Case.Nominative))
			.append(verb.auxiliar, verb.auxiliar.conjugate(subject, Tense.Present))
			.append(indObjSeq)
			.append(dirObj)
			.append(verb, verb.conjugate(subject,  tense));
		
		String correctConjugation = verb.conjugate(subject, tense);
		String incorrectConjugation = (correctConjugation.endsWith(RegularVerb.PAST_PARTICIPLE_POSTFIX_WEAK)) ?
										verb.fakeStrongParticiple() : verb.fakeWeakParticiple();

		List<String> options = new ArrayList<String>();
		options.add(Verb.sein.conjugate(subject, Tense.Present) + " " + correctConjugation);
		options.add(Verb.haben.conjugate(subject, Tense.Present) + " " + correctConjugation);
		options.add(Verb.sein.conjugate(subject, Tense.Present) + " " + incorrectConjugation);
		options.add(Verb.haben.conjugate(subject, Tense.Present) + " " + incorrectConjugation);
		String correct = verb.auxiliar.conjugate(subject, Tense.Present) + " " + verb.conjugate(subject, tense);

		int index = prepareOptions(options, correct, 4);

		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_VERB, verb.toString());
		extra.put(TAG_SUBJECT, subject.toString());
		
		return new Exercise(seq, options, index, Type.VerbConjugationPastPerfect, verb, extra);
	}

}
