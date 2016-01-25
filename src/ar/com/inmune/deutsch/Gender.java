package ar.com.inmune.deutsch;

public enum Gender {
	Masculine("m", Number.Singular),
	Feminine("f", Number.Singular),
	Neuter("n", Number.Singular),
	Plural("p", Number.Plural),
//	None("", null) // personalpronommen ich, du, ihr, wir, Sie
	;

	public String id;
	private Number number;

	private Gender(String id, Number number) {
		this.id = id;
		this.number = number;
	}
	
	public static Gender getGender(String string) {
		for (Gender g : values()) {
			if (g.id.equals(string)) {
				return g;
			}
		}
		return null;
	}

	public Number number() {
		return number;
	}

}