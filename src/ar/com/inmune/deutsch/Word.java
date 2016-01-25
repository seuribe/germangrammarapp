package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Word implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class Literal extends Word {
		private static final long serialVersionUID = 1L;
		private String str;
		public Literal(String str) {
			this.str = str;
		}
		public String toString() {
			return str;
		}
	}

	public static final Literal POINT = new Literal(".");
	public static final Literal COMMA = new Literal(",");
	public static final Literal QUESTION = new Literal("?");
	public static final Literal EXCLAMATION = new Literal("!");
	
	public static abstract class Filter<T extends Word> {
		public abstract boolean include(T word);
		public List<T> include(List<T> words) {
			List<T> ret = new ArrayList<T>();
			for (T w : words) {
				if (include(w)) {
					ret.add(w);
				}
			}
			return ret;
		}
		public List<T> exclude(List<T> words) {
			List<T> ret = new ArrayList<T>();
			for (T w : words) {
				if (!include(w)) {
					ret.add(w);
				}
			}
			return ret;
		}
	}

}
