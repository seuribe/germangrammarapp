package ar.com.inmune.deutsch.exercise.builder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ar.com.inmune.GermanPractice;
import ar.com.inmune.deutsch.Adjective;
import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.exercise.DataProvider;
import ar.com.inmune.deutsch.exercise.Exercise;

public abstract class ExerciseBuilder {
	protected static final String TAG_INDIRECT_OBJECT = "indirect_object";
	protected static final String TAG_DIRECT_OBJECT = "direct_object";
	protected static final String TAG_VERB = "verb";
	protected static final Object TAG_AUXILIARY_VERB = "auxiliary_verb";
	protected static final String TAG_SUBJECT = "subject";
	protected static final String TAG_NOUN = "noun";
	protected static final String TAG_CASE = "case";
	protected static final String TAG_ADJECTIVE = "adjective";
	protected static final String TAG_TENSE = "tense";
	protected static final String TAG_ARTICLE = "article";
	protected static final Object TAG_PREPOSITIONAL_OBJECT = "prep_object";

	protected final Random random = new Random();
	protected DataProvider dataProvider = GermanPractice.dataProvider;

	protected ExerciseBuilder() {
		dataProvider = GermanPractice.dataProvider;
	}
	
	public abstract Exercise getRandomExercise();
	public abstract Exercise getExercise(Map<String, Object> options);

	protected Noun getRandomNoun(List<Noun> nouns) {
		return nouns.get(random.nextInt(nouns.size()));
	}
	protected Verb getRandomVerb(List<Verb> verbs) {
		return verbs.get(random.nextInt(verbs.size()));
	}
	protected Adjective getRandomAdjective(List<Adjective> adjectives) {
		return adjectives.get(random.nextInt(adjectives.size()));
	}

	protected Article getRandomArticle() {
		switch(random.nextInt(3)) {
		case 0:
			return new Article.Definite();
		case 1:
			return new Article.Indefinite();
		case 2:
			return new Article.Negative();
		}
		return null;
	}

	protected Subject getRandomSubject() {
		return Subject.values()[random.nextInt(Subject.values().length)];
	}

	/**
	 * Shuffle, limit, ensure correct is present, and return index
	 * 
	 * @param options
	 * @param correct
	 * @param limit
	 * @return
	 */
	protected int prepareOptions(List<String> options, String correct, int limit) {
		while (options.remove(correct));
		Set<String> temp = new HashSet<String>(options);
		options.clear();
		options.addAll(temp);
		Collections.shuffle(options);
		limit--;
		while (options.size() > limit) {
			options.remove(0);
		}
		int index = random.nextInt(options.size() + 1);
		options.add(index, correct);
		return index;
	}
	
	protected <T> T randomFrom(T ... words) {
		return words[random.nextInt(words.length)];
	}

	protected <T> T randomFrom(List<T> objs) {
		return objs.get(random.nextInt(objs.size()));
	}
}
