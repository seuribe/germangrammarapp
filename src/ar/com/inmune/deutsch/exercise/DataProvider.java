package ar.com.inmune.deutsch.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import ar.com.inmune.deutsch.Adjective;
import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Gender;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.Preposition;
import ar.com.inmune.deutsch.StrongVerb;
import ar.com.inmune.deutsch.Subject;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.WeakVerb;
import ar.com.inmune.deutsch.Word;
import ar.com.inmune.deutsch.Verb.PrepositionalObject;

import ar.com.inmune.R;
import ar.com.inmune.SplitLineReader;

public class DataProvider {
	private static final String TAGS_KEY = "tags";
	private static final String PLURAL_KEY = "pl";
	private static final String GENDER_KEY = "g";
	private static final String SINGULAR_KEY = "sing";

	private static final String PREP_OBJECT_KEY = "po";
	private static final String CASE_KEY = "case";
	private static final String PREPOSITION_KEY = "prep";
	private static final String NOUNTAGS_TAG_KEY = "nountags";
	
	private static final String INFINITIVE_KEY = "inf";
	private static final String ROOT_KEY = "root";
	private static final String ROOT2_KEY = "root2";
	private static final String PAST_KEY = "past";
	private static final String PAST_PARTICIPLE_KEY = "pastp";

	private static final String TRANSITIVE_KEY = "transitive";
	private static final String DATIVE_KEY = "dative";
	
	private static final String TYPE_KEY = "type";
	private static final String STRONG_VALUE = "strong";
	private static final String WEAK_VALUE = "weak";

	public DataProvider(Context ctx) {
		try {
			loadVerbs(ctx);
			loadNouns(ctx);
			loadAdjectives(ctx);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<Noun> nouns;
	private List<Verb> verbs;
	private List<Adjective> adjectives;
	
	public List<Noun> getNouns() {
		return nouns;
	}

	private List<Noun> loadNouns(Context ctx) throws IOException {
		nouns = new ArrayList<Noun>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getResources().openRawResource(R.raw.nouns)));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		
		try {
			JSONObject json = new JSONObject(sb.toString());
			
			JSONArray array = json.getJSONArray("nouns");
			for (int i =0 ; i < array.length() ; i++) {
				JSONObject jsNoun = array.getJSONObject(i);
				Gender gender = getGender(jsNoun.getString(GENDER_KEY));
				String singular = jsNoun.getString(SINGULAR_KEY);
				String plural = jsNoun.has(PLURAL_KEY) ? jsNoun.getString(PLURAL_KEY) : singular;
				Noun noun = new Noun(singular, plural, gender);
				noun.tags = jsNoun.has(TAGS_KEY) ? readNounTags(jsNoun.getString(TAGS_KEY)) : null; 
				nouns.add(noun);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nouns;
	}

	private static Gender getGender(String tags) {
		for (Gender g : Gender.values()) {
			if (tags.contains(g.id)) {
				return g;
			}
		}
		return null;
	}

	public Verb getVerb(String infinitive) {
		if (infinitive == null) {
			return null;
		}
		for (Verb verb : verbs) {
			if (verb.infinitive.equals(infinitive)) {
				return verb;
			}
		}
		return null;
	}
	
	public List<Verb> getVerbs() {
		return verbs;
	}

	private List<Noun.Tag> readNounTags(String tags) {
		List<Noun.Tag> ret = new ArrayList<Noun.Tag>();
		if (tags == null) {
			return ret;
		}
		
		for (Noun.Tag tag : Noun.Tag.values()) {
			if (tags.contains(tag.id)) {
				ret.add(tag);
			}
		}
		return ret;
	}
	
	private List<Verb> loadVerbs(Context ctx) throws IOException {
		verbs = new ArrayList<Verb>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getResources().openRawResource(R.raw.verbs)));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			try {
					
				JSONObject obj = new JSONObject(line);
				String type = (String)obj.get(TYPE_KEY);
				Verb auxiliar = (obj.has(Verb.sein.infinitive)) ? Verb.sein : Verb.haben;
				String infinitive = (String)obj.get(INFINITIVE_KEY);
				String root = (String) obj.get(ROOT_KEY);
				
				List<Noun.Tag> transitiveTags = null;
				if (obj.has(TRANSITIVE_KEY)) {
					transitiveTags = readNounTags(obj.getString(TRANSITIVE_KEY));
				}
				boolean dative = obj.has(DATIVE_KEY);
	
				Verb verb = null;
				if (type.equals(WEAK_VALUE)) {
					verb = new WeakVerb(infinitive, root, transitiveTags, dative, auxiliar);
				} else if (type.equals(STRONG_VALUE)) {
					String past = (String) obj.get(PAST_KEY);
					String pastParticiple = (String)obj.get(PAST_PARTICIPLE_KEY);
					if (obj.has(ROOT2_KEY)) {
						String root2 = (String)obj.get(ROOT2_KEY);
						verb = new StrongVerb(infinitive, root, root2, past, pastParticiple, transitiveTags, dative, auxiliar);
					} else {
						verb = new StrongVerb(infinitive, root, past, pastParticiple, transitiveTags, dative, auxiliar);
					}
				}
				if (obj.has(PREP_OBJECT_KEY)) {
					verb.pos = readPrepositionalObjects(obj.getJSONArray(PREP_OBJECT_KEY));
				}
				
				verbs.add(verb);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		reader.close();
		return verbs;
	}

	private List<PrepositionalObject> readPrepositionalObjects(JSONArray arr) throws JSONException {
		List<PrepositionalObject> pos = new ArrayList<Verb.PrepositionalObject>();
		for (int i = 0 ; i < arr.length() ; i++) {
			JSONObject obj = arr.getJSONObject(i);
			String prepForm = obj.getString(PREPOSITION_KEY);
			String cs = obj.getString(CASE_KEY);
			String applies = obj.getString(NOUNTAGS_TAG_KEY);
			Preposition prep = Preposition.get(prepForm, cs);
			List<Noun.Tag> tags = readNounTags(applies);
			pos.add(new PrepositionalObject(prep, tags));
		}
		return pos;
	}

	public List<Adjective> getAdjectives() {
		return adjectives;
	}

	private List<Adjective> loadAdjectives(Context ctx) throws IOException {
		adjectives = new ArrayList<Adjective>();

		SplitLineReader reader = new SplitLineReader(ctx.getResources().openRawResource(R.raw.adjectives));
		while (reader.hasMore()) {
			adjectives.add(new Adjective(reader.next()[0]));
		}
		reader.close();

		return adjectives;
	}

	public Noun getNoun(String singular) {
		if (singular == null) {
			return null;
		}
		for (Noun noun : nouns) {
			if (noun.singular().equals(singular)) {
				return noun;
			}
		}
		return null;
	}

	public static String readFile(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String ret = sb.toString();

		br.close();
		return ret;
	}

	public static String readAndReplace(InputStream is, Map<String, String> replace) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
inner:		for (String key : replace.keySet()) {
				if (!line.contains("$"))
					break inner;
				line = line.replace(key, new String(replace.get(key).getBytes(), "UTF8"));
			}
			sb.append(line);
		}
		String ret = sb.toString();

		br.close();
		return ret;
	}

	public Word getAdjective(String string) {
		for (Adjective adj : adjectives) {
			if (adj.root.equals(string)) {
				return adj;
			}
		}
		return null;
	}

	public List<Article> getArticles() {
		List<Article> articles = new ArrayList<Article>();

		articles.add(new Article.Definite());
		articles.add(new Article.Indefinite());
		articles.add(new Article.Dieser());
		articles.add(new Article.Etwas());
		articles.add(new Article.Demonstrative());
		articles.add(new Article.Jener());
		articles.add(new Article.Negative());
		for (Subject subject : Subject.values()) {
			articles.add(new Article.Possesive(subject));
		}
		return articles;
	}
}
