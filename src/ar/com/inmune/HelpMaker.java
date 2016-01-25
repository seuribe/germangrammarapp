package ar.com.inmune;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import ar.com.inmune.deutsch.Adjective;
import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Case;
import ar.com.inmune.deutsch.Gender;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.NounNounPhrase;
import ar.com.inmune.deutsch.PersonalPronoun;
import ar.com.inmune.deutsch.RegularVerb;
import ar.com.inmune.deutsch.StrongVerb;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Tense;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WeakVerb;
import ar.com.inmune.deutsch.Word;
import ar.com.inmune.deutsch.Verb.ModalAuxiliaryVerb;
import ar.com.inmune.deutsch.Verb.SeinHabenWerdenVerb;
import ar.com.inmune.deutsch.WordSequence;
import ar.com.inmune.deutsch.exercise.DataProvider;

public abstract class HelpMaker <T extends Word> {
	private static Map<Class<? extends Word>, Class<? extends HelpMaker<? extends Word>>> helpers = new HashMap<Class<? extends Word>, Class<? extends HelpMaker<? extends Word>>>();

	private static final String ERROR_HTML = "html/error.html";

	private static final String VERB_HELP_HTML = "html/verb_conjugation.html";
	private static final String ARTICLE_HELP_HTML = "html/article_declension.html";
	private static final String NOUN_HELP_HTML = "html/noun.html";
	
	private static Random random = new Random();
	
	static {
		helpers.put(Verb.class, VerbHelpMaker.class);
		helpers.put(WeakVerb.class, VerbHelpMaker.class);
		helpers.put(StrongVerb.class, VerbHelpMaker.class);
		helpers.put(RegularVerb.class, VerbHelpMaker.class);
		helpers.put(SeinHabenWerdenVerb.class, VerbHelpMaker.class);
		helpers.put(ModalAuxiliaryVerb.class, VerbHelpMaker.class);

		helpers.put(Article.class, ArticleHelpMaker.class);
		helpers.put(Article.Definite.class, ArticleHelpMaker.class);
		helpers.put(Article.Indefinite.class, ArticleHelpMaker.class);
		helpers.put(Article.Demonstrative.class, ArticleHelpMaker.class);
		helpers.put(Article.Etwas.class, ArticleHelpMaker.class);
		helpers.put(Article.Possesive.class, ArticleHelpMaker.class);
		helpers.put(Article.Dieser.class, ArticleHelpMaker.class);
		helpers.put(Article.Jener.class, ArticleHelpMaker.class);
		helpers.put(Article.Negative.class, ArticleHelpMaker.class);
		helpers.put(Noun.class, NounHelpMaker.class);
	}
	
	public abstract String getHTML(Context ctx, T word) throws IOException;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getHelp(Context ctx, Word word) {
		try {
			Class<? extends HelpMaker<? extends Word>> makerClass = helpers.get(word.getClass());

			if (makerClass == null) {
				makerClass = ErrorMaker.class;
			}
			HelpMaker maker = makerClass.newInstance();

			return maker.getHTML(ctx, word);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected static class ErrorMaker extends HelpMaker<Word>{

		@Override
		public String getHTML(Context ctx, Word word) throws IOException {
			Map<String, String> rep = new HashMap<String, String>();
			rep.put("$word$", word.toString());
			return DataProvider.readAndReplace(ctx.getAssets().open(ERROR_HTML), rep);
		}
		
	}
	
	public static class VerbHelpMaker extends HelpMaker<Verb> {
		@Override
		public String getHTML(Context ctx, Verb verb) throws IOException {
			Map<String, String> rep = new HashMap<String, String>();
			for (Subject subject : Subject.values()) {
				PersonalPronoun pp = new PersonalPronoun(subject);
				rep.put("$" + Tense.Present.toString() + "." + pp.decline(Case.Nominative) + "$", verb.conjugate(subject, Tense.Present));
				rep.put("$" + Tense.Past.toString() + "." + pp.decline(Case.Nominative) + "$", verb.conjugate(subject, Tense.Past));
			}
			rep.put("$verb$",  verb.infinitive);
			rep.put("$PastParticiple$",  verb.pastParticiple());
			rep.put("$aux$",  (verb.auxiliar != null) ? verb.auxiliar.infinitive : "none");
			StringBuilder extra = new StringBuilder();
			if (verb.transitive()) {
				extra.append(R.string.html_transitive);
			}
			if (verb.reqDative) {
				extra.append(R.string.html_dative);
			}
			rep.put("$extra$", extra.toString());
			return DataProvider.readAndReplace(ctx.getAssets().open(VERB_HELP_HTML), rep);
		}
	}

	public static class ArticleHelpMaker extends HelpMaker<Article> {

		@Override
		public String getHTML(Context ctx, Article art) throws IOException {
			
			Map<String, String> rep = new HashMap<String, String>();
			rep.put("$art$", art.toString());
			for (Gender g : Gender.values()) {
				for (Case c : Case.values()) {
					rep.put("$" + c.toString() + "." + g.toString() + "$", art.decline(c, g));
				}
			}
			return DataProvider.readAndReplace(ctx.getAssets().open(ARTICLE_HELP_HTML), rep);
		}
	}
	
	public static class NounHelpMaker extends HelpMaker<Noun> {


		@Override
		public String getHTML(Context ctx, Noun noun) throws IOException {
			Map<String, String> rep = new HashMap<String, String>();
			rep.put("$art$", new Article.Definite().decline(Case.Nominative, noun.gender));
			rep.put("$noun$", noun.toString());
			rep.put("$singular$", noun.singular());
			rep.put("$plural$", noun.plural());
			rep.put("$gender$", noun.gender.toString());
			rep.put("$example$", makeExample(noun));
			
			return DataProvider.readAndReplace(ctx.getAssets().open(NOUN_HELP_HTML), rep);
		}
		
		private String makeExample(Noun noun) {
			WordSequence ws = new WordSequence();
			NounNounPhrase nnp = new NounNounPhrase(noun, new Article.Definite());
			ws.append(nnp.decline(Case.Nominative));
			ws.append(Verb.sein, Verb.sein.conjugate(nnp, Tense.Present));
			List<Adjective> adjectives = GermanPractice.dataProvider.getAdjectives();
			Adjective adj = adjectives.get(random.nextInt(adjectives.size()));
			ws.append(adj, adj.decline(Case.Nominative, noun.gender, null));
			return ws.toString();
		}
		
	}
}