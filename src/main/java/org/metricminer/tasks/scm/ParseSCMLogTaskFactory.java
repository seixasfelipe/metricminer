package org.metricminer.tasks.scm;

import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.dao.ProjectDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.parser.SCMLogParser;
import org.metricminer.tasks.parser.SCMLogParserFactory;

public class ParseSCMLogTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Task task, StatelessSession statelessSession,
			MetricMinerConfigs config) {
		SCMLogParser logParser = new SCMLogParserFactory().basedOn(
				task.getProject().getMapConfig(), statelessSession, task.getProject());
		return new ParseSCMLogTask(logParser, task.getProject(), new ProjectDao(statelessSession));
	}

}
