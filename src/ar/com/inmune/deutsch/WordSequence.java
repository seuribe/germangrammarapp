package ar.com.inmune.deutsch;

import java.util.ArrayList;
import java.util.List;

public class WordSequence {
	private List<Word> words;
	private List<String> strings;
	private List<Word> hidden;

	public WordSequence() {
		this.words = new ArrayList<Word>();
		this.strings = new ArrayList<String>();
		this.hidden = new ArrayList<Word>();
	}

	public WordSequence append(Word word, String string) {
		if (word == null || string == null) {
			return this;
		}
		this.words.add(word);
		this.strings.add(string);
		return this;
	}

	public WordSequence append(String str) {
		if (str == null) {
			return this;
		}
		this.words.add(new Word.Literal(str));
		this.strings.add(str);
		return this;
	}
	
	public WordSequence append(Word word) {
		if (word == null) {
			return this;
		}
		this.words.add(word);
		this.strings.add(word.toString());
		return this;
	}

	public WordSequence append(WordSequence seq) {
		if (seq == null) {
			return this;
		}
		this.words.addAll(seq.words);
		this.strings.addAll(seq.strings);
		return this;
	}

	public WordSequence hide(Word word) {
		if (word == null) {
			return this;
		}
		this.hidden.add(word);
		return this;
	}

	public String toHiddenString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < words.size() ; i++) {
			if (i > 0) {
				sb.append(" ");
			}
			Word word = words.get(i);
			if (hidden.contains(word)) {
				sb.append("___");
			} else {
				sb.append(strings.get(i));
			}
		}
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean useSpace = false;
		for (String str : strings) {
			if (useSpace) {
				sb.append(" ");
			}
			sb.append(str);
			useSpace = true;
		}
		return sb.toString();
	}
}
