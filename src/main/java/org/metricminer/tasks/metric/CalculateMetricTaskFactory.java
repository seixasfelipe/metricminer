package org.metricminer.tasks.metric;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.model.Task;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;
import org.metricminer.tasks.metric.common.Metric;
import org.metricminer.tasks.metric.common.MetricFactory;

public class CalculateMetricTaskFactory implements RunnableTaskFactory {

	@Override
	public RunnableTask build(Task task, Session session, StatelessSession statelessSession,
			MetricMinerConfigs config) {
		String metricFactoryName = task
				.getTaskConfigurationValueFor(TaskConfigurationEntryKey.METRIC_FACTORY_CLASS);
		try {
			MetricFactory metricFactory = (MetricFactory) Class.forName(metricFactoryName)
					.newInstance();

			Metric metricToCalculate = metricFactory.build();
			return new CalculateMetricTask(task, metricToCalculate, session, statelessSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
