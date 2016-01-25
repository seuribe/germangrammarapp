package ar.com.inmune.deutsch;

public class PrepositionalObject {
	private Preposition prep;
	private NounPhrase np;

	public PrepositionalObject(Preposition prep, NounPhrase np) {
		this.prep = prep;
		this.np = np;
	}

	public WordSequence decline() {
		
		Article art = np.article();
		Gender gen = np.gender();
		
		String prepForm = (prep.contracts(art, gen)) ?
				(prep.contract(art, gen)) :
				(prep.toString() + " " + art.decline(prep.governingCase, gen));
		
		return new WordSequence()
			.append(prep, prepForm)
			.append(np.decline(prep.governingCase));
	}

}
