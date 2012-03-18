package tasks;

import model.Project;
import br.com.caelum.revolution.persistence.runner.SCMLogParser;

public class ParseGitLogTask implements RunnableTask {

	private SCMLogParser logParser;
	private Project project;

	public ParseGitLogTask(SCMLogParser logParser, Project project) {
		this.logParser = logParser;
		this.project = project;
	}

	@Override
	public void run() {
		logParser.start();
	}

}
