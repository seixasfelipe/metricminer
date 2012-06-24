package org.metricminer.tasks.scm;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;

public class SCMCloneTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Task task, Session session, StatelessSession statelessSession,
			MetricMinerConfigs config) {
		return new SCMCloneTask(task.getProject());
	}
}
