package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.Arrays;
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
import ar.com.inmune.deutsch.Preposition;
import ar.com.inmune.deutsch.PrepositionalObject;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.Article.Definite;
import ar.com.inmune.deutsch.exercise.Exercise;


public class PrepositionInUse extends ExerciseBuilder {
	private List<Noun> locations;
	private List<String> regularOptions = Arrays.asList("ins", "im");
	private List<String> akkOptions = Arrays.asList("in die", "in das", "in den");
	private List<String> datOptions = Arrays.asList("in der", "in dem", "in den");
	private Verb gehen;

	public PrepositionInUse() {
		locations = Noun.Tag.IsLocation.filter(dataProvider.getNouns());
		gehen = dataProvider.getVerb("gehen");
	}

	@Override
	public Exercise getRandomExercise() {
		Subject subject = getRandomSubject();
		Noun location = getRandomNoun(locations);
		Verb verb = random.nextBoolean() ? Verb.sein : gehen;
		return makeExercise(subject, verb, location);
	}
	
	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		Noun location = (Noun)options.get(TAG_NOUN);
		Verb verb = (Verb)options.get(TAG_VERB);
		return makeExercise(subject, verb, location);
	}
	
	private Exercise makeExercise(Subject subject, Verb verb, Noun location) {
		// case 1: <human> + ist + (guess preposition + article) + location
		// case 2: <human> + gehen + (guess preposition + article) + location
		
		
		NounPhrase np = new PersonalPronounPhrase(new PersonalPronoun(subject));
		Tense tense = Tense.Present;
		
		Preposition prep;
		if (verb == Verb.sein) {
			prep = Preposition.IN_position;
		} else {
			prep = Preposition.IN_direction;
		}
		
		Article def = new Definite();
		NounPhrase locationPhrase = new NounNounPhrase(location, def, null);
		PrepositionalObject po = new PrepositionalObject(prep, locationPhrase);

		boolean contracts = prep.contracts(def, location.gender);
		boolean acusative = prep.governingCase == Case.Acusative;
		
		String correct = (contracts) ?
						(prep.contract(def, location.gender)) :
						(prep.toString() + " " + def.decline(prep.governingCase, location.gender));

		WordSequence seq = new WordSequence().hide(def)
			.hide(prep)
			.append(np.decline(Case.Nominative))
			.append(verb, verb.conjugate(np, tense))
			.append(po.decline());
						
		List<String> options = new ArrayList<String>();
		options.addAll(regularOptions);
		options.addAll(acusative ? akkOptions : datOptions);
		if (contracts) {
			if (acusative) {
				options.remove(akkOptions.get(1));
			} else {
				options.remove(datOptions.get(1));
			}
		}
		int index = prepareOptions(options, correct, 4);
		
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put(TAG_SUBJECT, subject);
		extra.put(TAG_NOUN, location);
		extra.put(TAG_VERB, verb);
		
		Exercise ex = new Exercise(seq, options, index, Exercise.Type.PrepositionInUse, prep);
		
		return ex;
	}

}
