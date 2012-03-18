package tasks;

import model.Project;

import org.hibernate.Session;

import br.com.caelum.revolution.persistence.runner.SCMLogParser;
import br.com.caelum.revolution.persistence.runner.SCMLogParserFactory;

public class ParseGitLogTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Project project, Session session) {
		SCMLogParser logParser = new SCMLogParserFactory().basedOn(
				project.getMapConfig(), session, project);
		return new ParseGitLogTask(logParser, project);
	}

}
