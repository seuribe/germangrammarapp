package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.List;

public class NounNounPhrase extends NounPhrase implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Article article;
	private final List<Adjective> adjectives;
	private final Noun noun;

	public NounNounPhrase(Noun noun, Article article) {
		this(noun, article, null);
	}

	public NounNounPhrase(Noun noun, Article article, List<Adjective> adjectives) {
		this.noun = noun;
		this.article = article;
		this.adjectives = adjectives;
	}

	@Override
	public WordSequence decline(Case c, Number number) {
		WordSequence seq = new WordSequence();
		Gender gender = (number == Number.Plural) ? Gender.Plural : noun.gender;
		String declinedArticle = null;
		if (article != null && !(article instanceof Article.Indefinite && number == Number.Plural)) {
			declinedArticle = article.decline(c, gender);
			seq.append(article, declinedArticle);
		}
		if (adjectives != null) {
			for (Adjective adjective : adjectives) {
				seq.append(adjective, adjective.decline(c, gender, declinedArticle));
			}
		}
		seq.append(noun, noun.decline(c, number));
		return seq;
	}

	@Override
	public Subject getSubject() {
		return (noun.gender == Gender.Plural) ? Subject.ThirdPlural : Subject.get(3, Number.Singular, noun.gender);
	}

	@Override
	public WordSequence decline(Case c) {
		return decline(c, noun.gender.number());
	}

	@Override
	public Article article() {
		return article;
	}

	@Override
	public Gender gender() {
		return noun.gender;
	}

}