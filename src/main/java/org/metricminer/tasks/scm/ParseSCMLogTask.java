package org.metricminer.tasks.scm;

import org.metricminer.infra.dao.ProjectDao;
import org.metricminer.model.Project;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.parser.SCMLogParser;

public class ParseSCMLogTask implements RunnableTask {

	private SCMLogParser logParser;
	private final Project project;
	private final ProjectDao projectDao;

	public ParseSCMLogTask(SCMLogParser logParser, Project project,
			ProjectDao projectDao) {
		this.logParser = logParser;
		this.project = project;
		this.projectDao = projectDao;
	}

	@Override
	public void run() {
		logParser.start();
		project.setStats(projectDao.commitersCountFor(project),
				projectDao.commitCountFor(project),
				projectDao.firstCommitFor(project),
				projectDao.lastCommitFor(project));
	}

}
