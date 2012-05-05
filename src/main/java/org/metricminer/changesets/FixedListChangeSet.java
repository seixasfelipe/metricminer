package org.metricminer.changesets;

import java.util.List;


public class FixedListChangeSet implements ChangeSetCollection {

	private final List<ChangeSet> cs;

	public FixedListChangeSet(List<ChangeSet> cs) {
		this.cs = cs;
	}
	
	public List<ChangeSet> get() {
		return cs;
	}

}
