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
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;


public class SeinHabenConjugation extends ExerciseBuilder {

	private List<Noun> objects;
	private List<Noun> professions;

	public SeinHabenConjugation() {
		this.objects = Noun.Tag.IsObject.filter(dataProvider.getNouns());
		this.professions = Noun.Tag.IsProfession.filter(dataProvider.getNouns());
		objects.addAll(Noun.Tag.IsAnimal.filter(dataProvider.getNouns()));
	}

	@Override
	public Exercise getRandomExercise() {
		Verb verb;
		Noun noun;
		Subject subject = getRandomSubject();
		Tense tense = random.nextBoolean() ? Tense.Present : Tense.Past;

		if (random.nextBoolean()) {
			verb = Verb.sein;
			noun = getRandomNoun(professions);
		} else {
			verb = Verb.haben;
			noun = getRandomNoun(objects);
		}
		
		return makeExercise(subject, verb, noun, tense);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Verb verb = (Verb)options.get(TAG_VERB);
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		Noun noun = (Noun)options.get(TAG_NOUN);
		Tense tense = (Tense)options.get(TAG_TENSE);
		return makeExercise(subject, verb, noun, tense);
	}

	private Exercise makeExercise(Subject subject, Verb verb, Noun noun, Tense tense) {
		/*
		 * 1. (pers. pronom) + sein + ein + profession
		 * 2. (pers. pronom) + haben + ein + object
		 */
//		Producer p = new Producer();
//		p.hide(verb);
		
		PersonalPronoun pp = new PersonalPronoun(subject);
		NounPhrase object;

		object = new NounNounPhrase(noun, new Article.Indefinite(), null);
		
		List<String> options = new ArrayList<String>();
		for (Subject subj : Subject.values()) {
			options.add(verb.conjugate(subj,  tense));
		}
		String correctStr = verb.conjugate(subject, tense);
		int correct = prepareOptions(options, correctStr, 5);
		
//		p.produce(pp.decline(Case.Nominative));
//		p.produce(verb, correctStr);
//		object.decline(p, c, subject.number());
		
		WordSequence seq = new WordSequence().hide(verb)
			.append(pp.decline(Case.Nominative))
			.append(verb, correctStr)
			.append(object.decline(verb.getDirectObjectCase(), subject.number()));
		
		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_VERB, verb.toString());
		extra.put(TAG_TENSE, tense.toString());
		extra.put(TAG_NOUN, noun.toString());
		extra.put(TAG_SUBJECT, subject.toString());
		
		return new Exercise(seq, options, correct, Exercise.Type.SeinHabenConjugation, verb, extra);
	}
	
}
