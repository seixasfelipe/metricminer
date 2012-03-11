package br.com.caelum.revolution.changesets;

import java.util.List;

import br.com.caelum.revolution.scm.SCM;


public class AllChangeSets implements ChangeSetCollection {

	private List<ChangeSet> changeSets;

	public AllChangeSets(SCM scm) {
		this.changeSets = scm.getChangeSets();
	}

	public List<ChangeSet> get() {
		return changeSets;
	}


}
