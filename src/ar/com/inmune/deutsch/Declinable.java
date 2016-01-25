package ar.com.inmune.deutsch;

public interface Declinable {
	public String decline(Case c);
	public String decline(Case c, Gender g);
	public String decline(Case c, Number g);
	public String decline(Case c, Gender g, Object extra);
}
