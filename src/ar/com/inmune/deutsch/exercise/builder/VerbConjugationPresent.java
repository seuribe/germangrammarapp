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
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.Exercise.Type;


public class VerbConjugationPresent extends ExerciseBuilder {

	private List<Verb> verbs;
	private List<Noun> objects;
	private final static Tense tense = Tense.Present;

	public VerbConjugationPresent() {
		this.verbs = dataProvider.getVerbs();
		this.objects = Noun.Tag.IsObject.filter(dataProvider.getNouns());
	}
	
	@Override
	public Exercise getRandomExercise() {
		Verb verb = getRandomVerb(verbs);
		Subject subject = getRandomSubject();
		Noun indirectObject = (Noun)getRandomNoun(objects);
		List<Noun> directObjectList = objects;
		
		if (verb.transitive()) {
			Noun.OrFilter orFilter = new Noun.OrFilter(Noun.filtersFromTags(verb.transitiveTags));
			directObjectList = orFilter.include(objects);
		}
		Noun directObject = (Noun)getRandomNoun(directObjectList);	
		
		
		return makeExercise(verb, subject, indirectObject, directObject);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Verb verb = (Verb)options.get(TAG_VERB);
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		Noun directObject = (Noun)options.get(TAG_DIRECT_OBJECT);
		Noun indirectObject = (Noun)options.get(TAG_INDIRECT_OBJECT);
		return makeExercise(verb, subject, indirectObject, directObject);
	}

	private Exercise makeExercise(Verb verb, Subject subject, Noun indirectObject, Noun directObject) {

		NounPhrase subjectPhrase = new PersonalPronounPhrase(new PersonalPronoun(subject));
	
		WordSequence indObj = null;
		if (verb.reqDative) {
			NounPhrase indObjectPhrase = new NounNounPhrase(indirectObject, new Article.Definite(), null);
			indObj = indObjectPhrase.decline(Case.Dative);
		}

		WordSequence dirObj = null;
		if (verb.transitive()) {
			NounPhrase objectPhrase = new NounNounPhrase(directObject, new Article.Definite(), null);
			dirObj = objectPhrase.decline(Case.Acusative);
		}

		WordSequence seq = new WordSequence().hide(verb)
			.append(subjectPhrase.decline(Case.Nominative))
			.append(verb, verb.conjugate(subject, tense))
			.append(indObj)
			.append(dirObj);
			
		List<String> options = new ArrayList<String>();
		for (Subject s : Subject.values()) {
			options.add(verb.conjugate(s, tense));
		}
		String str = verb.conjugate(subject, tense);
		int correct = prepareOptions(options, str, 5);
		
		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_VERB, verb.toString());
		extra.put(TAG_SUBJECT, subjectPhrase.getSubject().toString());
		extra.put(TAG_DIRECT_OBJECT, directObject.toString());
		extra.put(TAG_INDIRECT_OBJECT, indirectObject.toString());

		return new Exercise(seq, options, correct, Type.VerbConjugationPresent, verb, extra);
	}
}
