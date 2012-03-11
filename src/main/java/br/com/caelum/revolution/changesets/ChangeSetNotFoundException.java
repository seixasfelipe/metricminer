package br.com.caelum.revolution.changesets;

public class ChangeSetNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ChangeSetNotFoundException(Exception e) {
		super(e);
	}

}
