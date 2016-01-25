package ar.com.inmune.deutsch.exercise.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Gender;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.NounPhrase;
import ar.com.inmune.deutsch.PersonalPronoun;
import ar.com.inmune.deutsch.PersonalPronounPhrase;
import ar.com.inmune.deutsch.Preposition;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.Verb.PrepositionalObject;
import ar.com.inmune.deutsch.exercise.Exercise;


public class IndefiniteArticle extends ExerciseBuilder {

	/*
	 * Casos:
	 * . <pers. pron.> + <verb> + <indefinite article> + <object>
	 * . das + ist + <indef. art.> + <profession>
	 * Verb: transitive verb
	 * 
	 * 
	 */

	private List<Noun> objects;
	private List<Noun> professions;
	private List<Noun> allNouns;

	public IndefiniteArticle() {
		this.allNouns = dataProvider.getNouns();
		this.objects = Noun.Tag.IsObject.filter(dataProvider.getNouns());
		objects.addAll(Noun.Tag.IsAnimal.filter(dataProvider.getNouns()));
		objects = Noun.IsPlural.exclude(objects);

		this.professions = Noun.Tag.IsProfession.filter(dataProvider.getNouns());
	}

	@Override
	public Exercise getRandomExercise() {
		Verb verb = null;
		PrepositionalObject po = null;
		Noun noun = null;
		Subject subject = randomFrom(Subject.FirstSingular, Subject.SecondSingular, Subject.ThirdSingularFeminine, Subject.ThirdSingularMasculine, Subject.ThirdSingularNeuter);

		switch (random.nextInt(3)) {
			case 0:
				verb = Verb.sein;
				noun = getRandomNoun(professions);
				break;
			case 1:
				verb = Verb.haben;
				noun = getRandomNoun(objects);
				break;
			case 2:
				verb = getRandomVerb(Verb.HasDativePO.include(dataProvider.getVerbs()));
				po = (PrepositionalObject) randomFrom(verb.pos);
				noun = getRandomNoun(
							Noun.IsPlural.exclude(new Noun.OrFilter(
									Noun.filtersFromTags(po.acceptedNouns)
							).include(allNouns))
						);
				break;
		}
		return makeExercise(subject, verb, po, noun);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Verb verb = (Verb)options.get(TAG_VERB);
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		Noun noun = (Noun)options.get(TAG_NOUN);
		PrepositionalObject po = (PrepositionalObject)options.get(TAG_PREPOSITIONAL_OBJECT);
		return makeExercise(subject, verb, po, noun);
	}

	private Exercise makeExercise(Subject subject, Verb verb, PrepositionalObject po, Noun noun) {
		/*
		 * 1. (pers. pronom) + sein + ein + profession
		 * 2. (pers. pronom) + haben + ein + object
		 */
		PersonalPronounPhrase ppp = new PersonalPronounPhrase(new PersonalPronoun(subject));
		NounPhrase object;
		Case c = Case.Nominative;
		Preposition prep = null;
		if (verb == Verb.sein) {
			c = Case.Nominative;
		} else if (verb == Verb.haben) {
			c = Case.Acusative;
		} else if (po != null) {
			prep = po.prep;
			c = po.prep.governingCase;
		}
		
		Article art = new Article.Indefinite();
		object = new NounNounPhrase(noun, art, null);
		
		List<String> options = new ArrayList<String>();
		options.add("(k)" + art.decline(Case.Acusative, Gender.Masculine));
		options.add("(k)" + art.decline(Case.Acusative, Gender.Feminine));
		options.add("(k)" + art.decline(Case.Acusative, Gender.Neuter));
		options.add("(k)" + art.decline(Case.Nominative, Gender.Masculine));
		options.add("(k)" + art.decline(Case.Nominative, Gender.Feminine));
		options.add("(k)" + art.decline(Case.Nominative, Gender.Neuter));
		options.add("(k)" + art.decline(Case.Dative, Gender.Masculine));
		options.add("(k)" + art.decline(Case.Dative, Gender.Feminine));
		options.add("(k)" + art.decline(Case.Dative, Gender.Neuter));

		String correctStr = "(k)" + art.decline(c, noun.gender);
		int correct = prepareOptions(options, correctStr, 4);
		
		WordSequence seq = new WordSequence().hide(art)
			.append(ppp.decline(Case.Nominative))
			.append(verb, verb.conjugate(ppp, Tense.Present))
			.append(prep) // can be null
			.append(object.decline(c, subject.number()));
		
		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_VERB, verb.toString());
		extra.put(TAG_NOUN, noun.toString());
		extra.put(TAG_SUBJECT, subject.toString());
		extra.put(TAG_ARTICLE, art.toString());
		
		return new Exercise(seq, options, correct, Exercise.Type.IndefiniteArticle, art, extra);
	}
}
