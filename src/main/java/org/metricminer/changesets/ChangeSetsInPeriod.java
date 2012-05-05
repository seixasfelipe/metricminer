package org.metricminer.changesets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.metricminer.scm.SCM;



public class ChangeSetsInPeriod implements ChangeSetCollection {

	private final Calendar startPeriod;
	private final Calendar endPeriod;
	private final SCM scm;

	public ChangeSetsInPeriod(SCM scm, Calendar startPeriod, Calendar endPeriod) {
		this.scm = scm;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}

	private boolean isInPeriod(ChangeSet currentChangeSetInfo) {
		return currentChangeSetInfo.getTime().after(startPeriod) && currentChangeSetInfo.getTime().before(endPeriod);
	}

	public Calendar getStartPeriod() {
		return startPeriod;
	}

	public Calendar getEndPeriod() {
		return endPeriod;
	}

	public List<ChangeSet> get() {
		List<ChangeSet> filteredList = new ArrayList<ChangeSet>();
		for(ChangeSet cs : scm.getChangeSets()) {
			if(isInPeriod(cs)) filteredList.add(cs);
		}
		return filteredList;
	}

}
