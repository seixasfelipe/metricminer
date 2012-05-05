package org.metricminer.changesets;

import java.util.List;

import org.metricminer.scm.SCM;



public class AllChangeSets implements ChangeSetCollection {

	private List<ChangeSet> changeSets;

	public AllChangeSets(SCM scm) {
		this.changeSets = scm.getChangeSets();
	}

	public List<ChangeSet> get() {
		return changeSets;
	}


}
