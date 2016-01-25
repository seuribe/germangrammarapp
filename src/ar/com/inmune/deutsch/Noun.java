package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Noun extends Word implements Serializable, Comparable<Noun> {
	private static final long serialVersionUID = 1L;

	// TODO: Redefine this as multiple letter tags, with the first in capital letter (e.g. Human, Object, Profession, Animal, etc.) -- Seu
	public static enum Tag {
		IsLocation("L"),
		IsHuman("H"),
		IsObject("O"),
		IsProfession("P"),
		IsAnimal("A"),
		IsEdible("E"),
		IsWritable("W"),
		;
		
		public final String id;

		private Tag(String id) {
			this.id = id;
		}

		public List<Noun> filter(List<Noun> nouns) {
			return new TagFilter(this).include(nouns);
		}
	}

	public final Gender gender;
	private final String singularForm;
	private final String pluralForm;

	public List<Tag> tags;

	public void addTag(Tag tag) {
		tags.add(tag);
	}

	public static class TagFilter extends Filter<Noun> {
		private Tag tag;
		public TagFilter(Tag tag) {
			this.tag = tag;
		}
		@Override
		public boolean include(Noun noun) {
			return noun != null && noun.tags != null && noun.tags.contains(tag);
		}
	}
	
	public static List<Filter<Noun>> filtersFromTags(List<Tag> tags) {
		List<Filter<Noun>> ret = new ArrayList<Word.Filter<Noun>>();
		for (Tag tag : tags) {
			ret.add(new TagFilter(tag));
		}
		return ret;
	}
	
	public static final TagFilter IsLocationFilter = new TagFilter(Tag.IsLocation);
	public static final TagFilter IsObjectFilter = new TagFilter(Tag.IsObject);
	public static final TagFilter IsHumanFilter = new TagFilter(Tag.IsHuman);
	public static final TagFilter IsProfessionFilter = new TagFilter(Tag.IsProfession);

	public static class AndFilter extends Filter<Noun> {

		private List<Filter<Noun>> filters;
		public AndFilter(List<Filter<Noun>> filters) {
			this.filters = filters;
		}
		@Override
		public boolean include(Noun word) {
			for (Filter<Noun> filter : filters) {
				if (!filter.include(word)) {
					return false;
				}
			}
			return true;
		}
	};

	public static class OrFilter extends Filter<Noun> {

		private List<Filter<Noun>> filters;
		public OrFilter(List<Filter<Noun>> filters) {
			this.filters = filters;
		}
		@Override
		public boolean include(Noun word) {
			for (Filter<Noun> filter : filters) {
				if (filter.include(word)) {
					return true;
				}
			}
			return false;
		}
	};
	
	public static final class GenderFilter extends Filter<Noun> {
		private Gender gender;
		public GenderFilter(Gender gender) {
			this.gender = gender;
		}
		@Override
		public boolean include(Noun word) {
			return word.gender == gender;
		}
		
	}
	public static final GenderFilter IsPlural = new GenderFilter(Gender.Plural);
	public static final GenderFilter IsFeminine = new GenderFilter(Gender.Feminine);
	public static final GenderFilter IsMasculine = new GenderFilter(Gender.Masculine);
	public static final GenderFilter IsNeuter = new GenderFilter(Gender.Neuter);

	public Noun(String singular, String plural, Gender gender) {
		this.singularForm = singular;
		this.pluralForm = plural;
		this.gender = gender;
		this.tags = new ArrayList<Tag>();
	}

	public String form(Number number) {
		return (number == Number.Singular) ? singularForm : pluralForm;
	}

	public String singular() {
		return singularForm;
	}

	public String plural() {
		return pluralForm;
	}
	
	public String decline(Case c) {
		return decline(c, gender.number());
	}

	public String decline(Case c, Number number) {
		String root = (number == Number.Singular) ? singularForm : pluralForm;
		
		if (c == Case.Genitive && (gender == Gender.Masculine || gender == Gender.Neuter)) {
			return root + "(e)s";
		}
		if (c == Case.Dative && number == Number.Plural) {
			char last = pluralForm.charAt(pluralForm.length() - 1);
			if (last == 's' || last == 'n') {
				return pluralForm;
			}
			return pluralForm + "n";
		}
		return root;
	}

	public String toString() {
		return singularForm;
	}

	@Override public int compareTo(Noun another) {
		return singularForm.compareTo(another.singularForm);
	}

}
