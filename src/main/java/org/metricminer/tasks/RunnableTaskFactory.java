package org.metricminer.tasks;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;

public interface RunnableTaskFactory {
	public RunnableTask build(Task task, Session session, StatelessSession statelessSession,
			MetricMinerConfigs config);
}
