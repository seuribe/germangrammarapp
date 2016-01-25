package ar.com.inmune.deutsch;

public enum Tense {
	Present,
	Past,
	PastPerfect,
	PastPluperfect,
	Future
	;

	public static Tense getTense(String str) {
		for (Tense tense : values()) {
			if (tense.toString().equals(str)) {
				return tense;
			}
		}
		return null;
	}
}
