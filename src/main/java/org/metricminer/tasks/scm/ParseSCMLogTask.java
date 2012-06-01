package org.metricminer.tasks.scm;

import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.repo.SCMLogParser;


public class ParseSCMLogTask implements RunnableTask {

	private SCMLogParser logParser;

	public ParseSCMLogTask(SCMLogParser logParser) {
		this.logParser = logParser;
	}

	@Override
	public void run() {
		logParser.start();
	}

}
