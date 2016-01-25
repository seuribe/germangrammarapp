package ar.com.inmune.deutsch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Preposition extends Word implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String regularForm;
	public final Case governingCase;

	private final static String DAS = "das";
	private final static String DER = "dem";
	private final static String DEM = "dem";

	private Map<String, String> forms;
	private static Map<String, Preposition> prepositions = new HashMap<String, Preposition>();
	
	protected Preposition(String regularForm, Case governingCase) {
		this(regularForm, governingCase, null, null, null);
	}

	protected Preposition(String regularForm, Case governingCase, String demForm, String dasForm, String derForm) {
		this.regularForm = regularForm;
		this.governingCase = governingCase;
		forms = new HashMap<String, String>();
		if (demForm != null) {
			forms.put(DEM, demForm);
		}
		if (derForm != null) {
			forms.put(DER, derForm);
		}
		if (dasForm != null) {
			forms.put(DAS, dasForm);
		}
		prepositions.put(regularForm + "/" + governingCase.key, this);
	}

	public boolean contracts(Article article, Gender gender) {
		return forms.containsKey(article.decline(governingCase, gender));
	}

	public String toString() {
		return regularForm;
	}
	
	public String contract(Article article, Gender gender) {
		if (article == null) {
			return regularForm;
		}
		String form = forms.get(article.decline(governingCase, gender));
		if (form == null) {
			form = regularForm;
		}
		return form;
	}
	
	public static final Preposition BIS = new Preposition("bis", Case.Acusative);
	public static final Preposition DURCH = new Preposition("durch", Case.Acusative);
	public static final Preposition FÜR = new Preposition("für", Case.Acusative);
	public static final Preposition GEGEN = new Preposition("gegen", Case.Acusative);
	public static final Preposition OHNE = new Preposition("ohne", Case.Acusative);
	public static final Preposition UM = new Preposition("um", Case.Acusative);

	public static final Preposition AUS = new Preposition("aus", Case.Dative);
	public static final Preposition AUßER = new Preposition("außer", Case.Dative);
	public static final Preposition BEI = new Preposition("bei", Case.Dative, "beim", null, null);
	public static final Preposition GENGEÜBER = new Preposition("gegenüber", Case.Dative);
	public static final Preposition MIT = new Preposition("mit", Case.Dative);
	public static final Preposition NACH = new Preposition("nach", Case.Dative);
	public static final Preposition SEIT = new Preposition("seit", Case.Dative);
	public static final Preposition VON = new Preposition("von", Case.Dative, "vom", null, null);
	public static final Preposition ZU = new Preposition("zu", Case.Dative, "zum", null, "zur");

	public static final Preposition AB = new Preposition("ab", Case.Dative);

	public static final Preposition AN_position = new Preposition("an", Case.Dative, "am", "ams", null);
	public static final Preposition AN_direction = new Preposition("an", Case.Acusative, "am", "ams", null);
	
	public static final Preposition AUF_position = new Preposition("auf", Case.Dative, null, "aufs", null);
	public static final Preposition AUF_direction = new Preposition("auf", Case.Acusative, null, "aufs", null);

	public static final Preposition HINTER_position = new Preposition("hinter", Case.Dative, null, "hinters", null);
	public static final Preposition HINTER_direction = new Preposition("hinter", Case.Acusative, null, "hinters", null);
	
	public static final Preposition IN_position = new Preposition("in", Case.Dative, "im", "ins", null);
	public static final Preposition IN_direction = new Preposition("in", Case.Acusative, "im", "ins", null);

	public static final Preposition NEBEN_position = new Preposition("neben", Case.Dative);
	public static final Preposition NEBEN_direction = new Preposition("neben", Case.Acusative);

	public static final Preposition ÜBER_position = new Preposition("über", Case.Dative, null, "übers", null);
	public static final Preposition ÜBER_direction = new Preposition("über", Case.Acusative, null, "übers", null);
	
	public static final Preposition UNTER_position = new Preposition("unter", Case.Dative, null, "unters", null);
	public static final Preposition UNTER_direction = new Preposition("unter", Case.Acusative, null, "unters", null);

	public static final Preposition VOR_position = new Preposition("vor", Case.Dative, null, "vors", null);
	public static final Preposition VOR_direction = new Preposition("vor", Case.Acusative, null, "vors", null);

	public static final Preposition ZWISCHEN_position = new Preposition("zwischen", Case.Dative);
	public static final Preposition ZWISCHEN_direction = new Preposition("zwischen", Case.Acusative);

	public static Preposition get(String form) {
		return prepositions.get(form);
	}

	public static Preposition get(String form, String caseForm) {
		return prepositions.get(form + "/" + caseForm);
	}
}
