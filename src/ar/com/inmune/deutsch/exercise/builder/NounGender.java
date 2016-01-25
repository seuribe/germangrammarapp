package ar.com.inmune.deutsch.exercise.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Gender;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.NounPhrase;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;


public class NounGender extends ExerciseBuilder {

	private List<Noun> nouns;
	private final List<String> DEFAULT_OPTIONS = Arrays.asList("der", "die", "das");

	public NounGender() {
		this.nouns = dataProvider.getNouns();
	}

	@Override
	public Exercise getRandomExercise() {
		return makeExercise(getRandomNoun(nouns));
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		return makeExercise((Noun) options.get(TAG_NOUN));
	}

	private Exercise makeExercise(Noun noun) {
		Article art = new Article.Definite();

		NounPhrase np = new NounNounPhrase(noun, art, null);

		int correct = (noun.gender == Gender.Plural) ? 1 : noun.gender.ordinal();

		WordSequence seq = new WordSequence().hide(art)
			.append(np.decline(Case.Nominative));
		
		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_NOUN, noun.singular());

		return new Exercise(seq, DEFAULT_OPTIONS, correct, Exercise.Type.NounGender, noun, extra);
	}
	
}
