package org.metricminer.changesets;

import java.util.Arrays;
import java.util.List;

public class OnlyOneChangeSet implements ChangeSetCollection{

	private final String cs;

	public OnlyOneChangeSet(String cs) {
		this.cs = cs;
	}
	
	public List<ChangeSet> get() {
		return Arrays.asList(new ChangeSet(cs, null));
	}

}
