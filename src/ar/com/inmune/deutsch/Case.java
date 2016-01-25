package ar.com.inmune.deutsch;

public enum Case {
	Nominative("N"), Acusative("A"), Dative("D"), Genitive("G");
	
	public String key;
	private Case(String key) {
		this.key = key;
	}
}