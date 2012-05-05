package org.metricminer.tasks;

import org.metricminer.runner.RunnableTask;
import org.metricminer.tasks.parsegit.SCMLogParser;


public class ParseGitLogTask implements RunnableTask {

	private SCMLogParser logParser;

	public ParseGitLogTask(SCMLogParser logParser) {
		this.logParser = logParser;
	}

	@Override
	public void run() {
		logParser.start();
	}

}
