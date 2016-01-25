package ar.com.inmune.deutsch;

public class Sentence {
	public Verb verb;
	public NounPhrase subject;
	public NounPhrase directObject;
	public NounPhrase indirectObject;
	public NounPhrase genitiveObject;
	public Tense tense;

	public String conjugate() {
		StringBuilder sb = new StringBuilder();
//		sb.append(subject.decline(p, Case.Nominative));
//		sb.append(" ");
//		if (tense == Tense.PastPerfect) {
//			sb.append(verb.auxiliar.conjugate(subject, tense));
//		} else {
//			sb.append(verb.conjugate(subject, tense));
//		}
//		if (directObject != null) {
//			sb.append(" ");
//			sb.append(directObject.decline(p, Case.Acusative));
//		}
//		if (indirectObject != null) {
//			sb.append(" ");
//			sb.append(indirectObject.decline(p, Case.Dative));
//		}
//		if (genitiveObject != null) {
//			sb.append(" ");
//			sb.append(genitiveObject.decline(p, Case.Genitive));
//		}
//		
		return sb.toString();
	}

}
