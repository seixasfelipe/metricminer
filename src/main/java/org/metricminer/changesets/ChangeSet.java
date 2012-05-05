package org.metricminer.changesets;

import java.util.Calendar;

public class ChangeSet {
	
	private final Calendar date;
	private final String id;

	public ChangeSet(String id, Calendar date) {
		this.id = id;
		this.date = date;
		
	}

	public Calendar getTime() {
		return date;
	}

	public String getId() {
		return id;
	}

}
