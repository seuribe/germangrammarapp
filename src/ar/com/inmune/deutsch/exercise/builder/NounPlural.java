package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.Exercise.Type;


public class NounPlural extends ExerciseBuilder {

	private List<Noun> nouns;
	
	public NounPlural() {
		this.nouns = Noun.IsPlural.exclude(dataProvider.getNouns());
	}
	
	@Override
	public Exercise getExercise(Map<String, Object> options) {
		return makeExercise((Noun) options.get(TAG_NOUN));
	}

	@Override
	public Exercise getRandomExercise() {
		return makeExercise(getRandomNoun(nouns));
	}

	// noun.singular " -> " @noun.plural
	private Exercise makeExercise(Noun noun) {

		Article art = new Article.Definite();
		WordSequence seq = new WordSequence()
				.append(new NounNounPhrase(noun, art).decline(Case.Nominative));
		
		seq.append(" -> die _____ ");
		
		List<String> options = new ArrayList<String>();
		String correct = noun.plural();
		if (!correct.endsWith("en")) {
			options.add(noun.singular() + "en");
		}
		if (!correct.endsWith("es")) {
			options.add(noun.singular() + "es");
		}
		if (!correct.endsWith("r")) {
			options.add(noun.singular() + "r");
		}
		int index = prepareOptions(options, correct, 4);

		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_NOUN, noun.singular());

		return new Exercise(seq, options, index, Type.NounPlural, noun, extra);
	}


}
