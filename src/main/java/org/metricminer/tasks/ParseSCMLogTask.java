package org.metricminer.tasks;

import org.metricminer.runner.RunnableTask;
import org.metricminer.tasks.parsegit.SCMLogParser;


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
