package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Adjective;
import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Gender;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.NounPhrase;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.Exercise.Type;


/**
 * TODO: put sentences in context!
 * @author seu
 *
 */
public class AdjectiveDeclension extends ExerciseBuilder {

	private List<Adjective> adjectives;
	private List<Noun> nouns;
	private List<String> endings = Arrays.asList("e", "en", "es", "er", "em");

	public AdjectiveDeclension() {
		this.adjectives = dataProvider.getAdjectives();
		this.nouns = dataProvider.getNouns();
	}
	
	private Case getRandomCase() {
		switch (random.nextInt(4)) {
		case 0: return Case.Nominative;
		case 1: return Case.Acusative;
		case 2: return Case.Dative;
		case 3: return Case.Genitive;
		}
		return null;
	}
	
	@Override
	public Exercise getRandomExercise() {
		Noun noun = getRandomNoun(nouns);
		Adjective adjective = getRandomAdjective(adjectives);
		Case c = getRandomCase();
		return makeExercise(noun, adjective, c);
	}

	private Exercise makeExercise(Noun noun, Adjective adjective, Case c) {
		Gender gender = noun.gender;
		Article art = random.nextBoolean() ? null : new Article.Definite();
		String strArticle = (art != null) ? art.decline(c, gender) : null;

		/*
		 * Case nom. = subject + any verb + opt. akk. object
		 * Case akk = subject + transitive verb + akk. object
		 * Case dat = subject + verb w/Dative P.O. + prep.object
		 * Case gen = subject + genitiv. obj + verb + opt. akk object
		 */
		
		NounPhrase nomPhrase = new NounNounPhrase(noun, art, Arrays.asList(adjective));
		
		WordSequence seq = new WordSequence().hide(adjective)
			.append(nomPhrase.decline(c));
		seq.append("(" + c.name() + ")");
		
		List<String> options = new ArrayList<String>();
		for (String ending : endings) {
			options.add(adjective.root + ending);
		}
		String correct = adjective.decline(c, gender, strArticle);
		int index = prepareOptions(options, correct, 5);

		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_NOUN, noun.toString());
		extra.put(TAG_ADJECTIVE, adjective.toString());
		extra.put(TAG_CASE, c.toString());
		
		return new Exercise(seq, options, index, Type.AdjectiveDeclension, adjective, extra);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Noun noun = (Noun) options.get(TAG_NOUN);
		Adjective adjective = (Adjective) options.get(TAG_ADJECTIVE);
		Case c = (Case) options.get(TAG_CASE);
		return makeExercise(noun, adjective, c);
	}

}
