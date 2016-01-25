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
import ar.com.inmune.deutsch.PossesivPronoun;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.Word;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.Exercise;


public class PossesiveDeterminers extends ExerciseBuilder {

	private List<Noun> objects;

	public PossesiveDeterminers() {
		objects = Noun.IsPlural.exclude(Noun.Tag.IsObject.filter(dataProvider.getNouns()));
	}
	
	@Override
	public Exercise getRandomExercise() {
		Subject subject = getRandomSubject();
		Noun object = getRandomNoun(objects);

		return makeExercise(subject, object);
	}

	@Override
	public Exercise getExercise(Map<String, Object> options) {
		Noun object = (Noun)options.get(TAG_DIRECT_OBJECT);
		Subject subject = (Subject)options.get(TAG_SUBJECT);
		return makeExercise(subject, object);
	}

	private Exercise makeExercise(Subject subject, Noun object) {
		/*
		 * case 1: <pers. pron.> + haben + ein + <object>. art + <object> + sein + <posesive determiner>
		 */

		PersonalPronounPhrase pp = new PersonalPronounPhrase(new PersonalPronoun(subject));
		NounPhrase np1 = new NounNounPhrase(object, new Article.Indefinite(), null);

		NounPhrase np2 = new NounNounPhrase(object, new Article.Definite(), null);
		PossesivPronoun det = new PossesivPronoun(subject);

//		Producer p = new Producer();
//		p.hide(det);
//
//		pp.decline(p, Case.Nominative);
//		p.produce(Verb.haben, Verb.haben.conjugate(pp, Tense.Present));
//		np1.decline(p, Case.Nominative);
//		p.point();

		String correct = det.decline(Case.Nominative, object.gender);

//		np2.decline(p,  Case.Nominative);
//		p.produce(Verb.sein, Verb.sein.conjugate(np2, Tense.Present));
//		p.produce(det, correct);
//		p.point();

		WordSequence seq = new WordSequence().hide(det)
			.append(pp.decline(Case.Nominative))
			.append(Verb.haben, Verb.haben.conjugate(pp, Tense.Present))
			.append(np1.decline(Case.Nominative))
			.append(Word.POINT)

			.append(np2.decline(Case.Nominative))
			.append(Verb.sein, Verb.sein.conjugate(np2, Tense.Present))
			.append(det, correct)
			.append(Word.POINT);
		
		List<String> options = new ArrayList<String>();
		addAllDeterminers(options, object);
		int index = prepareOptions(options, correct, 4);

		Map<String, String> extra = new HashMap<String, String>();
		extra.put(TAG_SUBJECT, subject.toString());
		extra.put(TAG_DIRECT_OBJECT, object.toString());

		return new Exercise(seq, options, index, Exercise.Type.PossesiveDeterminer, det, extra);
	}

	private void addAllDeterminers(List<String> options, Noun noun) {
		for (Subject s : Subject.values()) {
			Article pd = new Article.Possesive(s);
			options.add(pd.decline(Case.Nominative, noun.gender));
		}
	}

}
