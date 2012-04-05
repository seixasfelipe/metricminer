package tasks;

import tasks.runner.RunnableTask;
import br.com.caelum.revolution.persistence.runner.SCMLogParser;

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
